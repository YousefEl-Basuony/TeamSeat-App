package com.stackmates.teamseat.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.model.Team
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.atomic.AtomicBoolean

class FirebaseTeamSeatRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : TeamSeatRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val teamsCollection = firestore.collection("teams")
    private val seatsCollection = firestore.collection("seats")
    private val seededOnce = AtomicBoolean(false)

    companion object {
        private const val FORTY_EIGHT_HOURS_MS = 48 * 60 * 60 * 1000L
        private const val MAX_CONCURRENT_PENDING = 3
    }

    override fun observeTeams(): Flow<List<Team>> = callbackFlow {
        val registration = teamsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(MockDataProvider.sampleTeamsList)
                return@addSnapshotListener
            }
            if (snapshot == null || snapshot.isEmpty) {
                if (seededOnce.compareAndSet(false, true)) {
                    scope.launch { seedInitialDataIfEmpty() }
                }
                trySend(MockDataProvider.sampleTeamsList)
                return@addSnapshotListener
            }
            val teams = snapshot.documents.mapNotNull { it.toTeam() }
            trySend(teams)
        }
        awaitClose { registration.remove() }
    }

    override fun observeTeamSeats(teamId: String): Flow<List<Seat>> = callbackFlow {
        val registration = seatsCollection
            .whereEqualTo("teamId", teamId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(MockDataProvider.sampleSeats)
                    return@addSnapshotListener
                }
                if (snapshot == null || snapshot.isEmpty) {
                    trySend(MockDataProvider.sampleSeats)
                    return@addSnapshotListener
                }
                val now = System.currentTimeMillis()
                val seats = snapshot.documents.mapNotNull { it.toSeat() }
                    .sortedBy { it.seatNumber }
                val expiredIds = seats
                    .filter { it.status == SeatStatus.PENDING && it.expiresAt != null && now > it.expiresAt }
                    .map { it.id }
                if (expiredIds.isNotEmpty()) {
                    scope.launch {
                        expiredIds.forEach { rejectOrCancelSeat(it) }
                    }
                }
                trySend(seats)
            }
        awaitClose { registration.remove() }
    }

    override suspend fun requestSeatBooking(
        seatId: String,
        studentId: String,
        studentName: String,
        note: String
    ): Result<Unit> = runCatching {
        val pendingSnap = seatsCollection
            .whereEqualTo("applicantId", studentId)
            .whereEqualTo("status", SeatStatus.PENDING.name)
            .get()
            .await()

        if (pendingSnap.size() >= MAX_CONCURRENT_PENDING) {
            throw IllegalStateException("لا يمكنك التقديم على أكثر من 3 مقاعد معلقة في نفس الوقت")
        }

        val seatRef = seatsCollection.document(seatId)
        firestore.runTransaction { transaction ->
            val snap = transaction.get(seatRef)
            val currentStatus = snap.getString("status") ?: SeatStatus.AVAILABLE.name
            if (currentStatus != SeatStatus.AVAILABLE.name) {
                throw IllegalStateException("المقعد لم يعد متاحاً للحجز")
            }
            transaction.update(
                seatRef,
                mapOf(
                    "status" to SeatStatus.PENDING.name,
                    "applicantId" to studentId,
                    "applicantName" to studentName,
                    "applicantNote" to note,
                    "expiresAt" to (System.currentTimeMillis() + FORTY_EIGHT_HOURS_MS)
                )
            )
        }.await()
        Unit
    }

    override suspend fun acceptApplicantBatch(seatId: String, applicantId: String): Result<Unit> = runCatching {
        val otherPendingSnap = seatsCollection
            .whereEqualTo("applicantId", applicantId)
            .whereEqualTo("status", SeatStatus.PENDING.name)
            .get()
            .await()

        val batch = firestore.batch()
        batch.update(
            seatsCollection.document(seatId),
            mapOf(
                "status" to SeatStatus.BOOKED.name,
                "expiresAt" to null
            )
        )

        otherPendingSnap.documents
            .filter { it.id != seatId }
            .forEach { doc ->
                batch.update(
                    doc.reference,
                    mapOf(
                        "status" to SeatStatus.AVAILABLE.name,
                        "applicantId" to null,
                        "applicantName" to null,
                        "applicantNote" to null,
                        "expiresAt" to null
                    )
                )
            }

        batch.commit().await()
        Unit
    }

    override suspend fun rejectOrCancelSeat(seatId: String): Result<Unit> = runCatching {
        seatsCollection.document(seatId).update(
            mapOf(
                "status" to SeatStatus.AVAILABLE.name,
                "applicantId" to null,
                "applicantName" to null,
                "applicantNote" to null,
                "expiresAt" to null
            )
        ).await()
        Unit
    }

    override suspend fun lockTeam(teamId: String): Result<Unit> = runCatching {
        teamsCollection.document(teamId).update("isLocked", true).await()
        Unit
    }

    private suspend fun seedInitialDataIfEmpty() {
        runCatching {
            val existing = teamsCollection.limit(1).get().await()
            if (!existing.isEmpty) return

            val batch = firestore.batch()
            MockDataProvider.sampleTeamsList.forEach { team ->
                batch.set(teamsCollection.document(team.teamId), team.toMap())
            }
            MockDataProvider.sampleSeats.forEach { seat ->
                batch.set(seatsCollection.document(seat.id), seat.toMap())
            }
            batch.commit().await()
        }
    }

    private fun DocumentSnapshot.toTeam(): Team? {
        val teamId = getString("teamId") ?: id
        val projectName = getString("projectName") ?: return null
        return Team(
            teamId = teamId,
            projectName = projectName,
            category = getString("category") ?: "مشروع تخرج",
            description = getString("description") ?: "",
            leaderId = getString("leaderId") ?: "",
            leaderName = getString("leaderName") ?: "",
            totalSeats = (getLong("totalSeats") ?: 6L).toInt(),
            isLocked = getBoolean("isLocked") ?: false,
            supervisorDoctor = getString("supervisorDoctor") ?: "د. محمد كاظم",
            teachingAssistant = getString("teachingAssistant") ?: "م. شهد أحمد عبدالسلام",
            githubRepo = getString("githubRepo") ?: "",
            driveFolder = getString("driveFolder") ?: "",
            createdAt = getLong("createdAt") ?: System.currentTimeMillis()
        )
    }

    private fun DocumentSnapshot.toSeat(): Seat? {
        val seatId = getString("id") ?: id
        val teamId = getString("teamId") ?: return null
        val statusName = getString("status") ?: SeatStatus.AVAILABLE.name
        val status = runCatching { SeatStatus.valueOf(statusName) }.getOrDefault(SeatStatus.AVAILABLE)
        return Seat(
            id = seatId,
            teamId = teamId,
            seatNumber = (getLong("seatNumber") ?: 1L).toInt(),
            roleRequired = getString("roleRequired") ?: "",
            status = status,
            applicantId = getString("applicantId"),
            applicantName = getString("applicantName"),
            applicantNote = getString("applicantNote"),
            expiresAt = getLong("expiresAt")
        )
    }

    private fun Team.toMap(): Map<String, Any?> = mapOf(
        "teamId" to teamId,
        "projectName" to projectName,
        "category" to category,
        "description" to description,
        "leaderId" to leaderId,
        "leaderName" to leaderName,
        "totalSeats" to totalSeats,
        "isLocked" to isLocked,
        "supervisorDoctor" to supervisorDoctor,
        "teachingAssistant" to teachingAssistant,
        "githubRepo" to githubRepo,
        "driveFolder" to driveFolder,
        "createdAt" to createdAt
    )

    private fun Seat.toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "teamId" to teamId,
        "seatNumber" to seatNumber,
        "roleRequired" to roleRequired,
        "status" to status.name,
        "applicantId" to applicantId,
        "applicantName" to applicantName,
        "applicantNote" to applicantNote,
        "expiresAt" to expiresAt
    )
}

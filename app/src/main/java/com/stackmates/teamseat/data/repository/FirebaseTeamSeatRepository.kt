package com.stackmates.teamseat.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.stackmates.teamseat.core.model.CalendarEvent
import com.stackmates.teamseat.core.model.NotificationItem
import com.stackmates.teamseat.core.model.NotificationType
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.model.TeamChatMessage
import com.stackmates.teamseat.core.model.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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

    companion object {
        private const val FORTY_EIGHT_HOURS_MS = 48 * 60 * 60 * 1000L
        private const val MAX_CONCURRENT_PENDING = 3
    }

    override fun observeTeams(): Flow<List<Team>> = callbackFlow {
        val registration = teamsCollection.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) {
                trySend(emptyList())
                return@addSnapshotListener
            }
            val teams = snapshot.documents.mapNotNull { it.toTeam() }
                .sortedByDescending { it.createdAt }
            trySend(teams)
        }
        awaitClose { registration.remove() }
    }

    override fun observeTeamSeats(teamId: String): Flow<List<Seat>> = callbackFlow {
        if (teamId.isBlank()) {
            trySend(emptyList())
            awaitClose { }
            return@callbackFlow
        }
        val registration = seatsCollection
            .whereEqualTo("teamId", teamId)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    trySend(emptyList())
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

    override fun observeApplicantSeats(applicantId: String): Flow<List<Seat>> = callbackFlow {
        if (applicantId.isBlank()) {
            trySend(emptyList())
            awaitClose { }
            return@callbackFlow
        }
        val registration = seatsCollection
            .whereEqualTo("applicantId", applicantId)
            .whereEqualTo("status", SeatStatus.PENDING.name)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val now = System.currentTimeMillis()
                val seats = snapshot.documents.mapNotNull { it.toSeat() }
                val validSeats = seats.filter { it.expiresAt == null || now <= it.expiresAt }
                val expiredIds = seats
                    .filter { it.expiresAt != null && now > it.expiresAt }
                    .map { it.id }
                if (expiredIds.isNotEmpty()) {
                    scope.launch {
                        expiredIds.forEach { rejectOrCancelSeat(it) }
                    }
                }
                trySend(validSeats)
            }
        awaitClose { registration.remove() }
    }

    override suspend fun createTeam(
        projectName: String,
        category: String,
        description: String,
        leaderId: String,
        leaderName: String,
        seatRoles: List<String>,
        supervisorDoctor: String,
        teachingAssistant: String,
        githubRepo: String,
        driveFolder: String
    ): Result<Team> = runCatching {
        val teamId = "team_${System.currentTimeMillis()}"
        val cleanRoles = if (seatRoles.isEmpty()) listOf("Lead Developer", "Developer") else seatRoles
        val newTeam = Team(
            teamId = teamId,
            projectName = projectName.ifBlank { "مشروع جديد" },
            category = category.ifBlank { "مشروع تخرج" },
            description = description.ifBlank { "مشروع تعاوني عبر TeamSeat" },
            leaderId = leaderId,
            leaderName = leaderName,
            totalSeats = cleanRoles.size,
            supervisorDoctor = supervisorDoctor.ifBlank { "د. محمد كاظم" },
            teachingAssistant = teachingAssistant.ifBlank { "م. شهد أحمد عبدالسلام" },
            githubRepo = githubRepo.trim(),
            driveFolder = driveFolder.trim()
        )

        val batch = firestore.batch()
        batch.set(teamsCollection.document(teamId), newTeam.toMap())

        cleanRoles.forEachIndexed { index, role ->
            val seatId = "${teamId}_seat_${index + 1}"
            val seat = Seat(
                id = seatId,
                teamId = teamId,
                seatNumber = index + 1,
                roleRequired = role,
                status = if (index == 0) SeatStatus.BOOKED else SeatStatus.AVAILABLE,
                applicantId = if (index == 0) leaderId else null,
                applicantName = if (index == 0) leaderName else null
            )
            batch.set(seatsCollection.document(seatId), seat.toMap())
        }

        batch.commit().await()

        runCatching {
            firestore.collection("users").document(leaderId).update(
                mapOf("isLeader" to true, "currentTeamId" to teamId)
            ).await()
        }

        newTeam
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
        var targetTeamId = ""
        var targetRole = ""
        firestore.runTransaction { transaction ->
            val snap = transaction.get(seatRef)
            val currentStatus = snap.getString("status") ?: SeatStatus.AVAILABLE.name
            if (currentStatus != SeatStatus.AVAILABLE.name) {
                throw IllegalStateException("المقعد لم يعد متاحاً للحجز")
            }
            targetTeamId = snap.getString("teamId").orEmpty()
            targetRole = snap.getString("roleRequired").orEmpty()
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

        if (targetTeamId.isNotBlank()) {
            runCatching {
                val teamDoc = teamsCollection.document(targetTeamId).get().await()
                val leaderId = teamDoc.getString("leaderId").orEmpty()
                val projectName = teamDoc.getString("projectName").orEmpty()
                if (leaderId.isNotBlank() && leaderId != studentId) {
                    sendUserNotification(
                        userId = leaderId,
                        title = "طلب انضمام جديد لفريقك",
                        message = "تقدم $studentName لحجز مقعد ($targetRole) في فريق $projectName.",
                        type = NotificationType.NEW_REQUEST,
                        teamId = targetTeamId
                    )
                }
                if (studentId.isNotBlank()) {
                    sendUserNotification(
                        userId = studentId,
                        title = "طلبك قيد الانتظار ⏳",
                        message = "تم إرسال طلبك لحجز مقعد ($targetRole) في فريق $projectName وهو قيد مراجعة القائد.",
                        type = NotificationType.NEW_REQUEST,
                        teamId = targetTeamId
                    )
                }
            }
        }
        Unit
    }

    override suspend fun acceptApplicantBatch(seatId: String, applicantId: String): Result<Unit> = runCatching {
        val seatDoc = seatsCollection.document(seatId).get().await()
        val teamId = seatDoc.getString("teamId")
        val roleRequired = seatDoc.getString("roleRequired").orEmpty()

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

        if (!teamId.isNullOrBlank()) {
            runCatching {
                firestore.collection("users").document(applicantId)
                    .update("currentTeamId", teamId)
                    .await()
                val teamDoc = teamsCollection.document(teamId).get().await()
                val projectName = teamDoc.getString("projectName").orEmpty()
                sendUserNotification(
                    userId = applicantId,
                    title = "تم قبول طلبك!",
                    message = "أهلاً بك في فريق $projectName! تم تأكيد مقعدك ($roleRequired).",
                    type = NotificationType.ACCEPTED,
                    teamId = teamId
                )
            }
        }
        Unit
    }

    override suspend fun rejectOrCancelSeat(seatId: String): Result<Unit> = runCatching {
        val seatDoc = seatsCollection.document(seatId).get().await()
        val applicantId = seatDoc.getString("applicantId")
        val teamId = seatDoc.getString("teamId")
        val roleRequired = seatDoc.getString("roleRequired").orEmpty()

        seatsCollection.document(seatId).update(
            mapOf(
                "status" to SeatStatus.AVAILABLE.name,
                "applicantId" to null,
                "applicantName" to null,
                "applicantNote" to null,
                "expiresAt" to null
            )
        ).await()

        if (!applicantId.isNullOrBlank() && !teamId.isNullOrBlank()) {
            runCatching {
                val teamDoc = teamsCollection.document(teamId).get().await()
                val projectName = teamDoc.getString("projectName").orEmpty()
                sendUserNotification(
                    userId = applicantId,
                    title = "تحديث بخصوص طلبك",
                    message = "لم يتم تأكيد مقعد ($roleRequired) في فريق $projectName وأصبح متاحاً من جديد.",
                    type = NotificationType.REJECTED
                )
            }
        }
        Unit
    }

    override suspend fun removeMemberFromTeam(seatId: String): Result<Unit> = runCatching {
        val seatDoc = seatsCollection.document(seatId).get().await()
        val applicantId = seatDoc.getString("applicantId")
        val teamId = seatDoc.getString("teamId")
        val roleRequired = seatDoc.getString("roleRequired").orEmpty()

        seatsCollection.document(seatId).update(
            mapOf(
                "status" to SeatStatus.AVAILABLE.name,
                "applicantId" to null,
                "applicantName" to null,
                "applicantNote" to null,
                "expiresAt" to null
            )
        ).await()

        if (!applicantId.isNullOrBlank()) {
            runCatching {
                firestore.collection("users").document(applicantId)
                    .update("currentTeamId", null)
                    .await()
            }
            if (!teamId.isNullOrBlank()) {
                runCatching {
                    val teamDoc = teamsCollection.document(teamId).get().await()
                    val projectName = teamDoc.getString("projectName").orEmpty()
                    sendUserNotification(
                        userId = applicantId,
                        title = "تم إزالتك من الفريق",
                        message = "قام قائد فريق $projectName بإزالة مقعدك ($roleRequired).",
                        type = NotificationType.REJECTED
                    )
                }
            }
        }
        Unit
    }

    override suspend fun lockTeam(teamId: String): Result<Unit> = runCatching {
        teamsCollection.document(teamId).update("isLocked", true).await()
        Unit
    }

    override suspend fun deleteTeam(teamId: String): Result<Unit> = runCatching {
        val teamDoc = teamsCollection.document(teamId).get().await()
        val projectName = teamDoc.getString("projectName").orEmpty()
        val leaderId = teamDoc.getString("leaderId").orEmpty()

        val seatsSnap = seatsCollection.whereEqualTo("teamId", teamId).get().await()
        val memberIds = seatsSnap.documents
            .mapNotNull { it.getString("applicantId") }
            .filter { it.isNotBlank() && it != leaderId }
            .distinct()

        val batch = firestore.batch()
        seatsSnap.documents.forEach { batch.delete(it.reference) }
        batch.delete(teamsCollection.document(teamId))
        batch.commit().await()

        if (leaderId.isNotBlank()) {
            runCatching {
                firestore.collection("users").document(leaderId).update(
                    mapOf("currentTeamId" to null, "isLeader" to false)
                ).await()
            }
        }
        memberIds.forEach { memberId ->
            runCatching {
                firestore.collection("users").document(memberId)
                    .update("currentTeamId", null)
                    .await()
                sendUserNotification(
                    userId = memberId,
                    title = "تم حذف الفريق",
                    message = "قام قائد فريق $projectName بحذف الفريق.",
                    type = NotificationType.REJECTED
                )
            }
        }
        Unit
    }

    override suspend fun updateTeamSeats(teamId: String, updatedSeats: List<Seat>): Result<Unit> = runCatching {
        val existingSnap = seatsCollection.whereEqualTo("teamId", teamId).get().await()
        val updatedIds = updatedSeats.map { it.id }.toSet()
        val batch = firestore.batch()

        existingSnap.documents
            .filter { it.id !in updatedIds }
            .forEach { batch.delete(it.reference) }

        updatedSeats.forEachIndexed { index, seat ->
            val fixedSeat = seat.copy(teamId = teamId, seatNumber = index + 1)
            batch.set(seatsCollection.document(fixedSeat.id), fixedSeat.toMap())
        }

        batch.update(teamsCollection.document(teamId), "totalSeats", updatedSeats.size)
        batch.commit().await()
        Unit
    }

    override fun observeTeamMessages(teamId: String): Flow<List<TeamChatMessage>> = callbackFlow {
        if (teamId.isBlank()) {
            trySend(emptyList())
            awaitClose { }
            return@callbackFlow
        }
        val registration = teamsCollection.document(teamId)
            .collection("messages")
            .orderBy("timestampMs", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val msgs = snapshot.documents.mapNotNull { doc ->
                    val text = doc.getString("text") ?: return@mapNotNull null
                    val readByList = (doc.get("readBy") as? List<*>)
                        ?.filterIsInstance<String>()
                        ?: emptyList()
                    TeamChatMessage(
                        id = doc.id,
                        teamId = teamId,
                        senderId = doc.getString("senderId") ?: "",
                        senderName = doc.getString("senderName") ?: "عضو",
                        senderAvatarEmoji = doc.getString("senderAvatarEmoji") ?: "👩‍💻",
                        senderPhotoBase64 = doc.getString("senderPhotoBase64") ?: "",
                        text = text,
                        timeLabel = doc.getString("timeLabel") ?: "",
                        timestampMs = doc.getLong("timestampMs") ?: 0L,
                        readBy = readByList
                    )
                }
                trySend(msgs)
            }
        awaitClose { registration.remove() }
    }

    override suspend fun sendTeamMessage(
        teamId: String,
        senderId: String,
        senderName: String,
        text: String,
        senderAvatarEmoji: String,
        senderPhotoBase64: String
    ): Result<Unit> = runCatching {
        val now = System.currentTimeMillis()
        val timeLabel = SimpleDateFormat("hh:mm a", Locale("ar")).format(Date(now))
        val data = hashMapOf(
            "teamId" to teamId,
            "senderId" to senderId,
            "senderName" to senderName,
            "senderAvatarEmoji" to senderAvatarEmoji,
            "senderPhotoBase64" to senderPhotoBase64,
            "text" to text.trim(),
            "timeLabel" to timeLabel,
            "timestampMs" to now,
            "readBy" to listOf(senderId)
        )
        teamsCollection.document(teamId).collection("messages").add(data).await()
        Unit
    }

    override suspend fun markTeamMessagesRead(teamId: String, readerId: String): Result<Unit> = runCatching {
        if (teamId.isBlank() || readerId.isBlank()) return@runCatching
        val snap = teamsCollection.document(teamId).collection("messages").get().await()
        val batch = firestore.batch()
        var hasUpdates = false
        snap.documents.forEach { doc ->
            val readBy = (doc.get("readBy") as? List<*>)?.filterIsInstance<String>() ?: emptyList()
            if (readerId !in readBy) {
                batch.update(doc.reference, "readBy", FieldValue.arrayUnion(readerId))
                hasUpdates = true
            }
        }
        if (hasUpdates) {
            batch.commit().await()
        }
        Unit
    }

    override fun observeTeamEvents(teamId: String): Flow<List<CalendarEvent>> = callbackFlow {
        if (teamId.isBlank()) {
            trySend(emptyList())
            awaitClose { }
            return@callbackFlow
        }
        val registration = teamsCollection.document(teamId)
            .collection("events")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val events = snapshot.documents.mapNotNull { doc ->
                    val title = doc.getString("title") ?: return@mapNotNull null
                    CalendarEvent(
                        id = doc.id,
                        teamId = teamId,
                        title = title,
                        date = doc.getString("date") ?: "",
                        time = doc.getString("time") ?: "",
                        type = doc.getString("type") ?: "ميتنج"
                    )
                }
                trySend(events)
            }
        awaitClose { registration.remove() }
    }

    override suspend fun addTeamEvent(
        teamId: String,
        title: String,
        date: String,
        time: String,
        type: String,
        addedByName: String,
        addedByUid: String
    ): Result<Unit> = runCatching {
        val data = hashMapOf(
            "teamId" to teamId,
            "title" to title.trim(),
            "date" to date.trim(),
            "time" to time.trim(),
            "type" to type,
            "addedByName" to addedByName,
            "createdAt" to System.currentTimeMillis()
        )
        teamsCollection.document(teamId).collection("events").add(data).await()

        runCatching {
            val teamDoc = teamsCollection.document(teamId).get().await()
            val projectName = teamDoc.getString("projectName").orEmpty()
            val leaderId = teamDoc.getString("leaderId").orEmpty()
            val seatsSnap = seatsCollection
                .whereEqualTo("teamId", teamId)
                .whereEqualTo("status", SeatStatus.BOOKED.name)
                .get()
                .await()

            val recipientIds = buildSet {
                if (leaderId.isNotBlank()) add(leaderId)
                seatsSnap.documents.mapNotNull { it.getString("applicantId") }
                    .filter { it.isNotBlank() }
                    .forEach { add(it) }
            }.filter { it != addedByUid }

            recipientIds.forEach { uid ->
                sendUserNotification(
                    userId = uid,
                    title = "موعد جديد في تقويم الفريق",
                    message = "أضاف $addedByName موعداً جديداً: «${title.trim()}» (${date.trim()} - ${time.trim()}) في فريق $projectName.",
                    type = NotificationType.TEAM_LOCKED
                )
            }
        }
        Unit
    }

    override fun observeUserNotifications(userId: String): Flow<List<NotificationItem>> = callbackFlow {
        if (userId.isBlank()) {
            trySend(emptyList())
            awaitClose { }
            return@callbackFlow
        }
        val registration = firestore.collection("users")
            .document(userId)
            .collection("notifications")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val items = snapshot.documents.mapNotNull { doc ->
                    val title = doc.getString("title") ?: return@mapNotNull null
                    val typeName = doc.getString("type") ?: NotificationType.NEW_REQUEST.name
                    val type = runCatching { NotificationType.valueOf(typeName) }
                        .getOrDefault(NotificationType.NEW_REQUEST)
                    NotificationItem(
                        id = doc.id,
                        title = title,
                        message = doc.getString("message") ?: "",
                        timeAgo = doc.getString("timeAgo") ?: "الآن",
                        type = type,
                        isRead = doc.getBoolean("isRead") ?: false,
                        teamId = doc.getString("teamId") ?: ""
                    )
                }
                trySend(items)
            }
        awaitClose { registration.remove() }
    }

    override suspend fun markAllNotificationsRead(userId: String): Result<Unit> = runCatching {
        if (userId.isBlank()) return@runCatching
        val notifCollection = firestore.collection("users").document(userId).collection("notifications")
        val unreadSnap = notifCollection.whereEqualTo("isRead", false).get().await()
        if (!unreadSnap.isEmpty) {
            val batch = firestore.batch()
            unreadSnap.documents.forEach { batch.update(it.reference, "isRead", true) }
            batch.commit().await()
        }
        Unit
    }

    override suspend fun clearUserNotifications(userId: String): Result<Unit> = runCatching {
        if (userId.isBlank()) return@runCatching
        val notifCollection = firestore.collection("users").document(userId).collection("notifications")
        val snap = notifCollection.get().await()
        if (!snap.isEmpty) {
            val batch = firestore.batch()
            snap.documents.forEach { batch.delete(it.reference) }
            batch.commit().await()
        }
        Unit
    }

    private suspend fun sendUserNotification(
        userId: String,
        title: String,
        message: String,
        type: NotificationType,
        teamId: String = ""
    ) {
        val now = System.currentTimeMillis()
        val timeLabel = SimpleDateFormat("hh:mm a", Locale("ar")).format(Date(now))
        val data = hashMapOf(
            "title" to title,
            "message" to message,
            "timeAgo" to timeLabel,
            "type" to type.name,
            "isRead" to false,
            "teamId" to teamId,
            "createdAt" to now
        )
        firestore.collection("users")
            .document(userId)
            .collection("notifications")
            .add(data)
            .await()
    }

    override fun observeAllUsers(): Flow<List<User>> = callbackFlow {
        val registration = firestore.collection("users")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val users = snapshot.documents.map { doc ->
                    User(
                        uid = doc.getString("uid") ?: doc.id,
                        name = doc.getString("name") ?: "",
                        studentId = doc.getString("studentId") ?: "",
                        email = doc.getString("email") ?: "",
                        roleTag = doc.getString("roleTag") ?: "Android Developer",
                        currentTeamId = doc.getString("currentTeamId"),
                        githubUrl = doc.getString("githubUrl") ?: "",
                        linkedinUrl = doc.getString("linkedinUrl") ?: "",
                        isLeader = doc.getBoolean("isLeader") ?: false,
                        avatarEmoji = doc.getString("avatarEmoji") ?: "👩‍💻",
                        major = doc.getString("major") ?: "علوم حاسب (Computer Science)",
                        isBanned = doc.getBoolean("isBanned") ?: false,
                        isAdmin = doc.getBoolean("isAdmin") ?: false,
                        photoBase64 = doc.getString("photoBase64") ?: ""
                    )
                }
                trySend(users)
            }
        awaitClose { registration.remove() }
    }

    override suspend fun updateTeamDetails(
        teamId: String,
        supervisorDoctor: String,
        teachingAssistant: String,
        githubRepo: String,
        driveFolder: String,
        isCompleted: Boolean
    ): Result<Unit> = runCatching {
        teamsCollection.document(teamId).update(
            mapOf(
                "supervisorDoctor" to supervisorDoctor.trim(),
                "teachingAssistant" to teachingAssistant.trim(),
                "githubRepo" to githubRepo.trim(),
                "driveFolder" to driveFolder.trim(),
                "isCompleted" to isCompleted
            )
        ).await()
        Unit
    }

    override suspend fun leaveTeam(teamId: String, memberId: String): Result<Unit> = runCatching {
        val seatsSnap = seatsCollection
            .whereEqualTo("teamId", teamId)
            .whereEqualTo("applicantId", memberId)
            .get()
            .await()

        val batch = firestore.batch()
        seatsSnap.documents.forEach { doc ->
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

        runCatching {
            firestore.collection("users").document(memberId)
                .update("currentTeamId", null)
                .await()
            val teamDoc = teamsCollection.document(teamId).get().await()
            val leaderId = teamDoc.getString("leaderId").orEmpty()
            val projectName = teamDoc.getString("projectName").orEmpty()
            val memberDoc = firestore.collection("users").document(memberId).get().await()
            val memberName = memberDoc.getString("name") ?: "أحد الأعضاء"
            if (leaderId.isNotBlank() && leaderId != memberId) {
                sendUserNotification(
                    userId = leaderId,
                    title = "مغادرة عضو للفريق",
                    message = "غادر $memberName فريق $projectName وأصبح مقعده متاحاً من جديد.",
                    type = NotificationType.REJECTED
                )
            }
        }
        Unit
    }

    override suspend fun setUserBanned(uid: String, banned: Boolean): Result<Unit> = runCatching {
        firestore.collection("users").document(uid).update("isBanned", banned).await()
        Unit
    }

    override suspend fun setUserAdmin(uid: String, isAdmin: Boolean): Result<Unit> = runCatching {
        firestore.collection("users").document(uid).update("isAdmin", isAdmin).await()
        Unit
    }

    override suspend fun getUserById(uid: String): User? {
        if (uid.isBlank()) return null
        return runCatching {
            val doc = firestore.collection("users").document(uid).get().await()
            if (!doc.exists()) return null
            User(
                uid = doc.getString("uid") ?: doc.id,
                name = doc.getString("name") ?: "",
                studentId = doc.getString("studentId") ?: "",
                email = doc.getString("email") ?: "",
                roleTag = doc.getString("roleTag") ?: "Android Developer",
                currentTeamId = doc.getString("currentTeamId"),
                githubUrl = doc.getString("githubUrl") ?: "",
                linkedinUrl = doc.getString("linkedinUrl") ?: "",
                isLeader = doc.getBoolean("isLeader") ?: false,
                avatarEmoji = doc.getString("avatarEmoji") ?: "👩‍💻",
                major = doc.getString("major") ?: "علوم حاسب (Computer Science)",
                isBanned = doc.getBoolean("isBanned") ?: false,
                isAdmin = doc.getBoolean("isAdmin") ?: false,
                photoBase64 = doc.getString("photoBase64") ?: ""
            )
        }.getOrNull()
    }

    override suspend fun deleteUserRecord(uid: String): Result<Unit> = runCatching {
        val seatsSnap = seatsCollection.whereEqualTo("applicantId", uid).get().await()
        val batch = firestore.batch()
        seatsSnap.documents.forEach { doc ->
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
        batch.delete(firestore.collection("users").document(uid))
        batch.commit().await()
        Unit
    }

    override suspend fun setTeamLocked(teamId: String, locked: Boolean): Result<Unit> = runCatching {
        teamsCollection.document(teamId).update("isLocked", locked).await()
        Unit
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
            isCompleted = getBoolean("isCompleted") ?: false,
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
        "isCompleted" to isCompleted,
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

package com.stackmates.teamseat.data.repository

import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.model.Team
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * 💻 خاص بالباك إند (أنت وحسام):
 * واجهة إدارة التيمات وحجز المقاعد وقواعد البيزنس لوجيك المتفق عليها:
 * 1. حد أقصى 3 طلبات معلقة (PENDING) للطالب الواحد.
 * 2. فحص صلاحية الـ 48 ساعة (Lazy Expiration Check).
 * 3. عملية القبول الموحدة (Batch Write): اعتماد مقعد وإلغاء باقي طلبات الطالب المعلقة فوراً.
 */
interface TeamSeatRepository {
    fun observeTeams(): Flow<List<Team>>
    fun observeTeamSeats(teamId: String): Flow<List<Seat>>

    suspend fun requestSeatBooking(seatId: String, studentId: String, studentName: String, note: String): Result<Unit>
    suspend fun acceptApplicantBatch(seatId: String, applicantId: String): Result<Unit>
    suspend fun rejectOrCancelSeat(seatId: String): Result<Unit>
    suspend fun lockTeam(teamId: String): Result<Unit>
}

class FakeTeamSeatRepository : TeamSeatRepository {
    private val _teams = MutableStateFlow(MockDataProvider.sampleTeamsList)
    private val _seats = MutableStateFlow(MockDataProvider.sampleSeats)

    companion object {
        private const val FORTY_EIGHT_HOURS_MS = 48 * 60 * 60 * 1000L
        private const val MAX_CONCURRENT_PENDING = 3
    }

    override fun observeTeams(): Flow<List<Team>> = _teams

    override fun observeTeamSeats(teamId: String): Flow<List<Seat>> {
        // تطبيق فحص الـ 48 ساعة التلقائي عند فتح صفحة التيم (Lazy Expiration)
        cleanupExpiredPendingSeats()
        return _seats.map { list -> list.filter { it.teamId == teamId || list.isNotEmpty() } }
    }

    private fun cleanupExpiredPendingSeats() {
        val now = System.currentTimeMillis()
        _seats.value = _seats.value.map { seat ->
            if (seat.status == SeatStatus.PENDING && seat.expiresAt != null && now > seat.expiresAt) {
                seat.copy(
                    status = SeatStatus.AVAILABLE,
                    applicantId = null,
                    applicantName = null,
                    applicantNote = null,
                    expiresAt = null
                )
            } else {
                seat
            }
        }
    }

    override suspend fun requestSeatBooking(
        seatId: String,
        studentId: String,
        studentName: String,
        note: String
    ): Result<Unit> {
        // TODO (Backend): استبدل بـ Firestore Transaction / Write
        val activePendingCount = _seats.value.count {
            it.status == SeatStatus.PENDING && it.applicantId == studentId
        }
        if (activePendingCount >= MAX_CONCURRENT_PENDING) {
            return Result.failure(IllegalStateException("لا يمكنك التقديم على أكثر من 3 مقاعد معلقة في نفس الوقت"))
        }

        _seats.value = _seats.value.map { seat ->
            if (seat.id == seatId && seat.status == SeatStatus.AVAILABLE) {
                seat.copy(
                    status = SeatStatus.PENDING,
                    applicantId = studentId,
                    applicantName = studentName,
                    applicantNote = note,
                    expiresAt = System.currentTimeMillis() + FORTY_EIGHT_HOURS_MS
                )
            } else {
                seat
            }
        }
        return Result.success(Unit)
    }

    override suspend fun acceptApplicantBatch(seatId: String, applicantId: String): Result<Unit> {
        // TODO (Backend): استبدل بـ FirebaseFirestore.getInstance().batch()
        // يقبل المقعد الحالي كـ BOOKED، ويلغي أي مقاعد PENDING أخرى لنفس الطالب ويعيدها AVAILABLE فوراً
        _seats.value = _seats.value.map { seat ->
            when {
                seat.id == seatId -> seat.copy(status = SeatStatus.BOOKED, expiresAt = null)
                seat.applicantId == applicantId && seat.status == SeatStatus.PENDING -> seat.copy(
                    status = SeatStatus.AVAILABLE,
                    applicantId = null,
                    applicantName = null,
                    applicantNote = null,
                    expiresAt = null
                )
                else -> seat
            }
        }
        return Result.success(Unit)
    }

    override suspend fun rejectOrCancelSeat(seatId: String): Result<Unit> {
        _seats.value = _seats.value.map { seat ->
            if (seat.id == seatId) {
                seat.copy(
                    status = SeatStatus.AVAILABLE,
                    applicantId = null,
                    applicantName = null,
                    applicantNote = null,
                    expiresAt = null
                )
            } else {
                seat
            }
        }
        return Result.success(Unit)
    }

    override suspend fun lockTeam(teamId: String): Result<Unit> {
        _teams.value = _teams.value.map { team ->
            if (team.teamId == teamId) team.copy(isLocked = true) else team
        }
        return Result.success(Unit)
    }
}

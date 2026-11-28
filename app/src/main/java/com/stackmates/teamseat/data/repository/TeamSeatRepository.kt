package com.stackmates.teamseat.data.repository

import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.CalendarEvent
import com.stackmates.teamseat.core.model.NotificationItem
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.model.TeamChatMessage
import com.stackmates.teamseat.core.model.User
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
    fun observeApplicantSeats(applicantId: String): Flow<List<Seat>>

    suspend fun createTeam(
        projectName: String,
        category: String,
        description: String,
        leaderId: String,
        leaderName: String,
        seatRoles: List<String>
    ): Result<Team>
    suspend fun requestSeatBooking(seatId: String, studentId: String, studentName: String, note: String): Result<Unit>
    suspend fun acceptApplicantBatch(seatId: String, applicantId: String): Result<Unit>
    suspend fun rejectOrCancelSeat(seatId: String): Result<Unit>
    suspend fun removeMemberFromTeam(seatId: String): Result<Unit>
    suspend fun lockTeam(teamId: String): Result<Unit>
    suspend fun deleteTeam(teamId: String): Result<Unit>
    suspend fun updateTeamSeats(teamId: String, updatedSeats: List<Seat>): Result<Unit>
    fun observeTeamMessages(teamId: String): Flow<List<TeamChatMessage>>
    suspend fun sendTeamMessage(teamId: String, senderId: String, senderName: String, text: String): Result<Unit>
    fun observeTeamEvents(teamId: String): Flow<List<CalendarEvent>>
    suspend fun addTeamEvent(teamId: String, title: String, date: String, time: String, type: String = "ميتنج"): Result<Unit>
    fun observeUserNotifications(userId: String): Flow<List<NotificationItem>>
    suspend fun markAllNotificationsRead(userId: String): Result<Unit>
    suspend fun clearUserNotifications(userId: String): Result<Unit>
    fun observeAllUsers(): Flow<List<User>>
    suspend fun setUserBanned(uid: String, banned: Boolean): Result<Unit>
    suspend fun deleteUserRecord(uid: String): Result<Unit>
    suspend fun setTeamLocked(teamId: String, locked: Boolean): Result<Unit>
}

class FakeTeamSeatRepository : TeamSeatRepository {
    private val _teams = MutableStateFlow(MockDataProvider.sampleTeamsList)
    private val _seats = MutableStateFlow(MockDataProvider.sampleSeats)
    private val _messages = MutableStateFlow<List<TeamChatMessage>>(emptyList())
    private val _events = MutableStateFlow(MockDataProvider.sampleCalendarEvents)
    private val _notifications = MutableStateFlow(MockDataProvider.sampleNotifications)

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

    override fun observeApplicantSeats(applicantId: String): Flow<List<Seat>> {
        cleanupExpiredPendingSeats()
        return _seats.map { list ->
            list.filter { it.applicantId == applicantId && it.status == SeatStatus.PENDING }
        }
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

    override suspend fun createTeam(
        projectName: String,
        category: String,
        description: String,
        leaderId: String,
        leaderName: String,
        seatRoles: List<String>
    ): Result<Team> {
        val teamId = "team_${System.currentTimeMillis()}"
        val newTeam = Team(
            teamId = teamId,
            projectName = projectName,
            category = category,
            description = description,
            leaderId = leaderId,
            leaderName = leaderName,
            totalSeats = seatRoles.size.coerceAtLeast(2)
        )
        val newSeats = seatRoles.mapIndexed { index, role ->
            Seat(
                id = "${teamId}_seat_${index + 1}",
                teamId = teamId,
                seatNumber = index + 1,
                roleRequired = role,
                status = if (index == 0) SeatStatus.BOOKED else SeatStatus.AVAILABLE,
                applicantId = if (index == 0) leaderId else null,
                applicantName = if (index == 0) leaderName else null
            )
        }
        _teams.value = listOf(newTeam) + _teams.value
        _seats.value = _seats.value + newSeats
        return Result.success(newTeam)
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

    override suspend fun removeMemberFromTeam(seatId: String): Result<Unit> = rejectOrCancelSeat(seatId)

    override suspend fun lockTeam(teamId: String): Result<Unit> {
        _teams.value = _teams.value.map { team ->
            if (team.teamId == teamId) team.copy(isLocked = true) else team
        }
        return Result.success(Unit)
    }

    override suspend fun deleteTeam(teamId: String): Result<Unit> {
        _teams.value = _teams.value.filterNot { it.teamId == teamId }
        _seats.value = _seats.value.filterNot { it.teamId == teamId }
        return Result.success(Unit)
    }

    override suspend fun updateTeamSeats(teamId: String, updatedSeats: List<Seat>): Result<Unit> {
        _seats.value = _seats.value.filter { it.teamId != teamId } + updatedSeats
        _teams.value = _teams.value.map { team ->
            if (team.teamId == teamId) team.copy(totalSeats = updatedSeats.size) else team
        }
        return Result.success(Unit)
    }

    override fun observeTeamMessages(teamId: String): Flow<List<TeamChatMessage>> =
        _messages.map { list -> list.filter { it.teamId == teamId } }

    override suspend fun sendTeamMessage(
        teamId: String,
        senderId: String,
        senderName: String,
        text: String
    ): Result<Unit> {
        val msg = TeamChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            teamId = teamId,
            senderId = senderId,
            senderName = senderName,
            text = text,
            timeLabel = "الآن"
        )
        _messages.value = _messages.value + msg
        return Result.success(Unit)
    }

    override fun observeTeamEvents(teamId: String): Flow<List<CalendarEvent>> =
        _events.map { list -> list.filter { it.teamId == teamId || it.teamId == MockDataProvider.sampleTeam.teamId } }

    override suspend fun addTeamEvent(
        teamId: String,
        title: String,
        date: String,
        time: String,
        type: String
    ): Result<Unit> {
        val event = CalendarEvent(
            id = "ev_${System.currentTimeMillis()}",
            teamId = teamId,
            title = title,
            date = date,
            time = time,
            type = type
        )
        _events.value = listOf(event) + _events.value
        return Result.success(Unit)
    }

    override fun observeUserNotifications(userId: String): Flow<List<NotificationItem>> = _notifications

    override suspend fun markAllNotificationsRead(userId: String): Result<Unit> {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
        return Result.success(Unit)
    }

    override suspend fun clearUserNotifications(userId: String): Result<Unit> {
        _notifications.value = emptyList()
        return Result.success(Unit)
    }

    override fun observeAllUsers(): Flow<List<User>> =
        MutableStateFlow(listOf(MockDataProvider.ahmedLeader, MockDataProvider.saraStudent))

    override suspend fun setUserBanned(uid: String, banned: Boolean): Result<Unit> = Result.success(Unit)

    override suspend fun deleteUserRecord(uid: String): Result<Unit> = Result.success(Unit)

    override suspend fun setTeamLocked(teamId: String, locked: Boolean): Result<Unit> = Result.success(Unit)
}

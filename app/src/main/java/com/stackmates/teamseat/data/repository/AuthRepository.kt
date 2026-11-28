package com.stackmates.teamseat.data.repository

import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.DemoPersona
import com.stackmates.teamseat.core.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 💻 خاص بالباك إند (أنت وحسام):
 * واجهة إدارة الحسابات والمصادقة (تُربط مع FirebaseAuth + Firestore users collection)
 */
interface AuthRepository {
    val currentUser: StateFlow<User?>
    val activePersona: StateFlow<DemoPersona>

    suspend fun login(emailOrId: String, password: String): Result<User>
    suspend fun register(
        name: String,
        studentId: String,
        email: String,
        password: String,
        roleTag: String = "Android Developer",
        major: String = "CS"
    ): Result<User>
    suspend fun signInWithGoogle(idToken: String): Result<User>
    suspend fun completeProfile(
        name: String,
        studentId: String,
        roleTag: String,
        major: String = "CS"
    ): Result<User>
    suspend fun updateProfile(roleTag: String, githubUrl: String, linkedinUrl: String): Result<User>
    fun logout()
    fun switchDemoPersona()
}

class FakeAuthRepository : AuthRepository {
    private val _activePersona = MutableStateFlow(DemoPersona.SARA_STUDENT)
    override val activePersona: StateFlow<DemoPersona> = _activePersona.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(MockDataProvider.saraStudent)
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    override suspend fun login(emailOrId: String, password: String): Result<User> {
        // TODO (Backend): استبدل بـ FirebaseAuth.getInstance().signInWithEmailAndPassword(...)
        val user = MockDataProvider.saraStudent
        _currentUser.value = user
        return Result.success(user)
    }

    override suspend fun register(
        name: String,
        studentId: String,
        email: String,
        password: String,
        roleTag: String,
        major: String
    ): Result<User> {
        // TODO (Backend): استبدل بـ FirebaseAuth + إنشاء Document في users collection
        val newUser = User(
            uid = "uid_${System.currentTimeMillis()}",
            name = name,
            studentId = studentId,
            email = email,
            roleTag = roleTag,
            major = major
        )
        _currentUser.value = newUser
        return Result.success(newUser)
    }

    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        val user = MockDataProvider.saraStudent
        _currentUser.value = user
        return Result.success(user)
    }

    override suspend fun completeProfile(
        name: String,
        studentId: String,
        roleTag: String,
        major: String
    ): Result<User> {
        val updated = (_currentUser.value ?: MockDataProvider.saraStudent).copy(
            name = name,
            studentId = studentId,
            roleTag = roleTag,
            major = major
        )
        _currentUser.value = updated
        return Result.success(updated)
    }

    override suspend fun updateProfile(
        roleTag: String,
        githubUrl: String,
        linkedinUrl: String
    ): Result<User> {
        val updated = (_currentUser.value ?: MockDataProvider.saraStudent).copy(
            roleTag = roleTag,
            githubUrl = githubUrl,
            linkedinUrl = linkedinUrl
        )
        _currentUser.value = updated
        return Result.success(updated)
    }

    override fun logout() {
        _currentUser.value = null
    }

    override fun switchDemoPersona() {
        if (_activePersona.value == DemoPersona.SARA_STUDENT) {
            _activePersona.value = DemoPersona.AHMED_LEADER
            _currentUser.value = MockDataProvider.ahmedLeader
        } else {
            _activePersona.value = DemoPersona.SARA_STUDENT
            _currentUser.value = MockDataProvider.saraStudent
        }
    }
}

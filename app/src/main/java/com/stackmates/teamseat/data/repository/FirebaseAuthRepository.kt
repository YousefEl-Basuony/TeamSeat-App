package com.stackmates.teamseat.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.DemoPersona
import com.stackmates.teamseat.core.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val usersCollection = firestore.collection("users")

    private val _activePersona = MutableStateFlow(DemoPersona.SARA_STUDENT)
    override val activePersona: StateFlow<DemoPersona> = _activePersona.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            scope.launch {
                loadUserProfile(firebaseUser.uid)?.let { user ->
                    _currentUser.value = user
                }
            }
        }
    }

    override suspend fun login(emailOrId: String, password: String): Result<User> {
        return runCatching {
            val trimmedInput = emailOrId.trim()
            require(trimmedInput.isNotEmpty() && password.isNotEmpty()) {
                "من فضلك ادخل البريد الإلكتروني أو الرقم الجامعي وكلمة المرور"
            }

            val resolvedEmail = if (trimmedInput.contains("@")) {
                trimmedInput
            } else {
                val query = usersCollection
                    .whereEqualTo("studentId", trimmedInput)
                    .limit(1)
                    .get()
                    .await()
                query.documents.firstOrNull()?.getString("email")
                    ?: throw IllegalArgumentException("لم يتم العثور على حساب بهذا الرقم الجامعي")
            }

            val authResult = auth.signInWithEmailAndPassword(resolvedEmail, password).await()
            val uid = authResult.user?.uid ?: error("تعذر الحصول على معرف المستخدم")

            val profile = loadUserProfile(uid) ?: User(
                uid = uid,
                name = authResult.user?.displayName ?: resolvedEmail.substringBefore("@"),
                studentId = "",
                email = resolvedEmail,
                roleTag = "Android Developer"
            ).also { saveUserProfile(it) }

            _currentUser.value = profile
            profile
        }
    }

    override suspend fun register(
        name: String,
        studentId: String,
        email: String,
        password: String
    ): Result<User> {
        return runCatching {
            val cleanEmail = email.trim()
            val cleanName = name.trim()
            val cleanId = studentId.trim()
            require(cleanName.isNotEmpty() && cleanEmail.isNotEmpty() && password.length >= 6) {
                "تأكد من إدخال الاسم والبريد وكلمة مرور من 6 أحرف على الأقل"
            }

            val authResult = auth.createUserWithEmailAndPassword(cleanEmail, password).await()
            val uid = authResult.user?.uid ?: error("تعذر إنشاء الحساب")

            val newUser = User(
                uid = uid,
                name = cleanName,
                studentId = cleanId,
                email = cleanEmail,
                roleTag = "Android Developer"
            )
            saveUserProfile(newUser)
            _currentUser.value = newUser
            newUser
        }
    }

    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        return runCatching {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            val firebaseUser = authResult.user ?: error("تعذر تسجيل الدخول بحساب Google")
            val uid = firebaseUser.uid

            val profile = loadUserProfile(uid) ?: User(
                uid = uid,
                name = firebaseUser.displayName ?: firebaseUser.email?.substringBefore("@") ?: "طالب",
                studentId = "",
                email = firebaseUser.email ?: "",
                roleTag = "Android Developer"
            ).also { saveUserProfile(it) }

            _currentUser.value = profile
            profile
        }
    }

    override fun logout() {
        auth.signOut()
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

    private suspend fun loadUserProfile(uid: String): User? {
        val doc = usersCollection.document(uid).get().await()
        if (!doc.exists()) return null
        return User(
            uid = doc.getString("uid") ?: uid,
            name = doc.getString("name") ?: "",
            studentId = doc.getString("studentId") ?: "",
            email = doc.getString("email") ?: "",
            roleTag = doc.getString("roleTag") ?: "Android Developer",
            currentTeamId = doc.getString("currentTeamId"),
            githubUrl = doc.getString("githubUrl") ?: "",
            linkedinUrl = doc.getString("linkedinUrl") ?: "",
            isLeader = doc.getBoolean("isLeader") ?: false,
            avatarEmoji = doc.getString("avatarEmoji") ?: "👩‍💻",
            major = doc.getString("major") ?: "علوم حاسب (Computer Science)"
        )
    }

    private suspend fun saveUserProfile(user: User) {
        val data = hashMapOf(
            "uid" to user.uid,
            "name" to user.name,
            "studentId" to user.studentId,
            "email" to user.email,
            "roleTag" to user.roleTag,
            "currentTeamId" to user.currentTeamId,
            "githubUrl" to user.githubUrl,
            "linkedinUrl" to user.linkedinUrl,
            "isLeader" to user.isLeader,
            "avatarEmoji" to user.avatarEmoji,
            "major" to user.major
        )
        usersCollection.document(user.uid).set(data).await()
    }
}

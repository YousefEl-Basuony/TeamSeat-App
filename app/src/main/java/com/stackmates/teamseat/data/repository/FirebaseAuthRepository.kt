package com.stackmates.teamseat.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
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

    private var userProfileListener: ListenerRegistration? = null

    init {
        auth.addAuthStateListener { firebaseAuth ->
            val uid = firebaseAuth.currentUser?.uid
            if (uid != null) {
                startObservingUserProfile(uid)
            } else {
                userProfileListener?.remove()
                userProfileListener = null
                _currentUser.value = null
            }
        }
    }

    private fun startObservingUserProfile(uid: String) {
        userProfileListener?.remove()
        userProfileListener = usersCollection.document(uid).addSnapshotListener { doc, _ ->
            if (doc != null && doc.exists()) {
                val user = doc.toUser(uid)
                if (user.isBanned) {
                    auth.signOut()
                    _currentUser.value = null
                } else {
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

            if (profile.isBanned) {
                auth.signOut()
                throw IllegalStateException("تم تعطيل هذا الحساب من قبل إدارة التطبيق")
            }

            _currentUser.value = profile
            profile
        }
    }

    override suspend fun register(
        name: String,
        studentId: String,
        email: String,
        password: String,
        roleTag: String,
        major: String
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
                roleTag = roleTag,
                major = major
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
                roleTag = "Developer"
            ).also { saveUserProfile(it) }

            _currentUser.value = profile
            profile
        }
    }

    override suspend fun completeProfile(
        name: String,
        studentId: String,
        roleTag: String,
        major: String
    ): Result<User> {
        return runCatching {
            val cleanName = name.trim()
            val cleanId = studentId.trim()
            require(cleanName.isNotEmpty() && cleanId.isNotEmpty()) {
                "من فضلك ادخل الاسم والرقم الجامعي"
            }
            val uid = auth.currentUser?.uid ?: _currentUser.value?.uid
                ?: error("المستخدم غير مسجل الدخول")

            val baseUser = _currentUser.value ?: loadUserProfile(uid) ?: User(
                uid = uid,
                name = cleanName,
                studentId = cleanId,
                email = auth.currentUser?.email ?: "",
                roleTag = roleTag,
                major = major
            )
            val updated = baseUser.copy(
                name = cleanName,
                studentId = cleanId,
                roleTag = roleTag,
                major = major
            )
            saveUserProfile(updated)
            _currentUser.value = updated
            updated
        }
    }

    override suspend fun updateProfile(
        roleTag: String,
        githubUrl: String,
        linkedinUrl: String,
        name: String?
    ): Result<User> {
        return runCatching {
            val uid = auth.currentUser?.uid ?: _currentUser.value?.uid
                ?: error("المستخدم غير مسجل الدخول")
            val cleanGithub = githubUrl.trim()
            val cleanLinkedin = linkedinUrl.trim()
            val cleanName = name?.trim()?.takeIf { it.isNotBlank() }
            val updates = mutableMapOf<String, Any>(
                "roleTag" to roleTag,
                "githubUrl" to cleanGithub,
                "linkedinUrl" to cleanLinkedin
            )
            if (cleanName != null) {
                updates["name"] = cleanName
            }
            usersCollection.document(uid).update(updates).await()
            val base = _currentUser.value ?: loadUserProfile(uid) ?: error("تعذر تحديث الحساب")
            val updated = base.copy(
                name = cleanName ?: base.name,
                roleTag = roleTag,
                githubUrl = cleanGithub,
                linkedinUrl = cleanLinkedin
            )
            _currentUser.value = updated
            updated
        }
    }

    override suspend fun updateAvatar(avatarEmoji: String, photoBase64: String): Result<User> {
        return runCatching {
            val uid = auth.currentUser?.uid ?: _currentUser.value?.uid
                ?: error("المستخدم غير مسجل الدخول")
            usersCollection.document(uid).update(
                mapOf(
                    "avatarEmoji" to avatarEmoji,
                    "photoBase64" to photoBase64
                )
            ).await()
            val updated = (_currentUser.value ?: loadUserProfile(uid))?.copy(
                avatarEmoji = avatarEmoji,
                photoBase64 = photoBase64
            ) ?: error("تعذر تحديث الصورة")
            _currentUser.value = updated
            updated
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
        return doc.toUser(uid)
    }

    private fun DocumentSnapshot.toUser(fallbackUid: String): User = User(
        uid = getString("uid") ?: fallbackUid,
        name = getString("name") ?: "",
        studentId = getString("studentId") ?: "",
        email = getString("email") ?: "",
        roleTag = getString("roleTag") ?: "Android Developer",
        currentTeamId = getString("currentTeamId"),
        githubUrl = getString("githubUrl") ?: "",
        linkedinUrl = getString("linkedinUrl") ?: "",
        isLeader = getBoolean("isLeader") ?: false,
        avatarEmoji = getString("avatarEmoji") ?: "👩‍💻",
        major = getString("major") ?: "علوم حاسب (Computer Science)",
        isBanned = getBoolean("isBanned") ?: false,
        isAdmin = getBoolean("isAdmin") ?: false,
        photoBase64 = getString("photoBase64") ?: ""
    )

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
            "major" to user.major,
            "isBanned" to user.isBanned,
            "isAdmin" to user.isAdmin,
            "photoBase64" to user.photoBase64
        )
        usersCollection.document(user.uid).set(data).await()
    }
}

package com.stackmates.teamseat.core.model

/**
 * بيانات مشروع التيم
 */
data class Team(
    val teamId: String,
    val projectName: String,
    val category: String,           // مثال: "مشروع تخرج", "هندسة برمجيات"
    val description: String,
    val leaderId: String,
    val leaderName: String,
    val totalSeats: Int = 6,        // من 2 إلى 8 مقاعد
    val isLocked: Boolean = false,  // هل تم اعتماد وقفل التيم نهائياً للمعيد؟
    val isCompleted: Boolean = false, // هل تم تنفيذ المشروع بالكامل؟
    val supervisorDoctor: String = "د. محمد كاظم",
    val teachingAssistant: String = "م. شهد أحمد عبدالسلام",
    val githubRepo: String = "https://github.com/example/team-repo",
    val driveFolder: String = "https://drive.google.com/example",
    val createdAt: Long = System.currentTimeMillis()
)

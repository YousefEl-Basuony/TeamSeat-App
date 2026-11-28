package com.stackmates.teamseat.data.local

import android.content.Context
import androidx.compose.runtime.compositionLocalOf

data class AppSettings(
    val notificationsEnabled: Boolean = true,
    val isDarkMode: Boolean = false,
    val languageCode: String = "ar"
)

val LocalAppSettings = compositionLocalOf { AppSettings() }

class AppSettingsManager(context: Context) {
    private val prefs = context.getSharedPreferences("teamseat_app_settings", Context.MODE_PRIVATE)

    fun getSettings(): AppSettings = AppSettings(
        notificationsEnabled = prefs.getBoolean("notifications_enabled", true),
        isDarkMode = prefs.getBoolean("dark_mode", false),
        languageCode = prefs.getString("language_code", "ar") ?: "ar"
    )

    fun saveSettings(settings: AppSettings) {
        prefs.edit()
            .putBoolean("notifications_enabled", settings.notificationsEnabled)
            .putBoolean("dark_mode", settings.isDarkMode)
            .putString("language_code", settings.languageCode)
            .apply()
    }
}

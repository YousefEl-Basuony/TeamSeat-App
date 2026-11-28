package com.stackmates.teamseat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.data.local.AppSettingsManager
import com.stackmates.teamseat.data.local.LocalAppSettings
import com.stackmates.teamseat.navigation.AppNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsManager = remember { AppSettingsManager(applicationContext) }
            var appSettings by remember { mutableStateOf(settingsManager.getSettings()) }

            CompositionLocalProvider(LocalAppSettings provides appSettings) {
                TeamSeatTheme(
                    darkTheme = appSettings.isDarkMode,
                    rtl = appSettings.languageCode != "en"
                ) {
                    AppNavGraph(
                        appSettings = appSettings,
                        onUpdateAppSettings = { updated ->
                            appSettings = updated
                            settingsManager.saveSettings(updated)
                        }
                    )
                }
            }
        }
    }
}

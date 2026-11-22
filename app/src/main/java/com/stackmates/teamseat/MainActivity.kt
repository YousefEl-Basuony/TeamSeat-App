package com.stackmates.teamseat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.stackmates.teamseat.Screens.admin_youssef.LockTeamDialog
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.navigation.AppNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TeamSeatTheme {
                LockTeamDialog()
            }
        }
    }
}

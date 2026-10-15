package com.chaos.orcplayer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.chaos.orcplayer.domain.model.MediaItem
import com.chaos.orcplayer.ui.home.HomeScreen
import com.chaos.orcplayer.ui.player.PlayerScreen
import com.chaos.orcplayer.ui.settings.SettingsScreen
import com.chaos.orcplayer.ui.theme.OrcPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OrcPlayerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { pad ->
                    var destination by remember { mutableStateOf<Destination>(HomeDest) }
                    when (destination) {
                        is HomeDest -> HomeScreen(onMediaSelected = { destination = PlayerDest(it) })
                        is PlayerDest -> PlayerScreen(destination.item, onBack = { destination = HomeDest })
                        SettingsDest -> SettingsScreen(onBack = { destination = HomeDest })
                    }
                }
            }
        }
        handleIntent(intent)
    }
    override fun onNewIntent(intent: Intent) { super.onNewIntent(intent); handleIntent(intent) }
    private fun handleIntent(intent: Intent?) { intent?.data?.let { uri -> if (intent.type?.startsWith("audio/") == true || intent.type?.startsWith("video/") == true) android.util.Log.d(TAG, "Opening: $uri") } }
    companion object { const val TAG = "MainActivity" }
}

sealed interface Destination { object HomeDest : Destination; data class PlayerDest(val item: MediaItem) : Destination; object SettingsDest : Destination }

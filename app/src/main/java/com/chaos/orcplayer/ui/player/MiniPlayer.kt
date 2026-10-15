package com.chaos.orcplayer.ui.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.chaos.orcplayer.domain.model.MediaItem
import com.chaos.orcplayer.ui.common.PlaybackSlider

/** Persistent mini player bar at bottom of screen. */
@Composable
fun MiniPlayer(visible: Boolean, item: MediaItem?, posMs: Long = 0L, durMs: Long = 0L, isPlaying: Boolean = false, onToggle: () -> Unit, onOpen: () -> Unit) {
    AnimatedVisibility(visible = visible && item != null, enter = expandVertically(), exit = shrinkVertically()) {
        Surface(shadowElevation = 8.dp, color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f), shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)) {
            Column(Modifier.padding(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, Modifier.fillMaxWidth()) {
                    IconButton(onClick = onToggle) { Icon(if (isPlaying) androidx.compose.material.icons.Icons.Filled.Pause else androidx.compose.material.icons.Icons.Filled.PlayArrow, null, tint = MaterialTheme.colorScheme.primary) }
                    Column(Modifier.weight(1f)) {
                        Text(item.title, style = MaterialTheme.typography.bodyMedium, maxLines = 1, color = MaterialTheme.colorScheme.onSurface)
                        Text("${com.chaos.orcplayer.ui.common.formatTime(posMs)} / ${com.chaos.orcplayer.ui.common.formatTime(durMs)}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                    IconButton(onClick = onOpen) { Icon(androidx.compose.material.icons.Icons.Filled.Fullscreen, null, tint = MaterialTheme.colorScheme.primary) }
                }
                Spacer(Modifier.height(4.dp))
                PlaybackSlider(positionMs = posMs, durationMs = durMs, onSeek = {})
            }
        }
    }
}

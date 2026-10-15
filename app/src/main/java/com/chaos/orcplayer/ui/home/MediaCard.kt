package com.chaos.orcplayer.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.chaos.orcplayer.domain.model.MediaItem

@Composable
fun MediaCard(item: MediaItem, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(12.dp)) {
        Row(Modifier.padding(12.dp).fillMaxWidth(), verticalAlignment = Alignment.Top) {
            Box(Modifier.size(80.dp)
                .background(if (item.type == MediaItem.MediaType.Video) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center) {
                Icon(imageVector = if (item.type == MediaItem.MediaType.Video) androidx.compose.material.icons.Icons.Filled.PlayCircle else androidx.compose.material.icons.Icons.Filled.AudioFile,
                    null, tint = if (item.type == MediaItem.MediaType.Video) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(40.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(item.title, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                if (!item.artist.isNullOrEmpty()) Text(item.artist, style = MaterialTheme.typography.bodySmall, color = Color.Gray, maxLines = 1, modifier = Modifier.padding(top = 2.dp))
                Text(formatTime(item.durationMs), style = MaterialTheme.typography.labelSmall, color = Color.Gray, modifier = Modifier.padding(top = 2.dp))
            }
        }
    }
}

private fun formatTime(ms: Long): String {
    if (ms <= 0L) return "0:00"
    val totalSec = ms / 1000
    val h = totalSec / 3600; val m = (totalSec % 3600) / 60; val s = totalSec % 60
    return if (h > 0) String.format("%d:%02d:%02d", h, m, s) else String.format("%d:%02d", m, s)
}

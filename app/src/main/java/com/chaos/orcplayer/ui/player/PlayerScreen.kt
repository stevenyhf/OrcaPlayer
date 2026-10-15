package com.chaos.orcplayer.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.chaos.orcplayer.domain.model.MediaItem
import com.chaos.orcplayer.ui.common.PlaybackSlider
import com.chaos.orcplayer.ui.common.formatTime

/** Full-screen player with controls overlay (auto-hiding). */
@Composable
fun PlayerScreen(item: MediaItem, onBack: () -> Unit) {
    var showControls by remember { mutableStateOf(true) }
    val config = LocalConfiguration.current

    // Auto-hide after 3s
    LaunchedEffect(showControls) { if (showControls) kotlinx.coroutines.delay(3000L); showControls = false }

    Box(Modifier.fillMaxSize()
        .pointerInput(Unit) { androidx.compose.foundation.gestures.detectTapGestures(onTap = { showControls = !showControls }) },
        contentAlignment = Alignment.Center) {

        Surface(Modifier.fillMaxSize(), color = Color.Black) {}

        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, Modifier.padding(horizontal = 32.dp)) {
            // Video surface placeholder / album art
            Box(Modifier.size(config.smallestScreenWidthDp.dp.coerceAtMost(400.dp)).clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
                if (item.type == MediaItem.MediaType.Video) {
                    Icon(androidx.compose.material.icons.Icons.Filled.VideoLibrary, null, tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), modifier = Modifier.size(120.dp))
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(androidx.compose.material.icons.Icons.Filled.AudioFile, null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(180.dp))
                        Text("音频", style = MaterialTheme.typography.labelMedium, color = Color.Gray.copy(alpha = 0.7f), modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // Title
            Column(horizontalAlignment = Alignment.CenterHorizontally, Modifier.fillMaxWidth()) {
                Text(item.title, style = MaterialTheme.typography.titleLarge, color = Color.White, maxLines = 2, overflow = TextOverflow.Ellipsis)
                if (!item.artist.isNullOrEmpty()) Text(item.artist, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.6f), maxLines = 1, modifier = Modifier.padding(top = 4.dp))
            }

            Spacer(Modifier.height(24.dp))
            PlaybackSlider(positionMs = 0, durationMs = item.durationMs, onSeek = {})
            Spacer(Modifier.height(16.dp))

            // Controls row
            Row(horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically, Modifier.fillMaxWidth()) {
                IconButton(onClick = {}) { Icon(androidx.compose.material.icons.Icons.Filled.Shuffle, null, tint = Color.White.copy(alpha = 0.6f)) }
                IconButton(onClick = {}) { Icon(androidx.compose.material.icons.Icons.Filled.SkipPrevious, null, tint = Color.White, modifier = Modifier.size(36.dp)) }
                CircleIcon(onClick = {}) { Icon(androidx.compose.material.icons.Icons.Filled.PlayArrow, null, tint = Color.White, modifier = Modifier.size(56.dp)) }
                IconButton(onClick = {}) { Icon(androidx.compose.material.icons.Icons.Filled.SkipNext, null, tint = Color.White, modifier = Modifier.size(36.dp)) }
                Surface(modifier = Modifier.clickable {}, shape = RoundedCornerShape(12.dp), color = Color.White.copy(alpha = 0.1f)) {
                    Text("1.0x", style = MaterialTheme.typography.labelMedium, color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }

            Spacer(Modifier.height(40.dp))

            // Back button — top-left of screen
            Surface(Modifier.align(Alignment.TopStart).padding(16.dp).clickable(onBack), shape = CircleShape, tonalElevation = 4.dp) {
                Icon(androidx.compose.material.icons.Icons.Filled.ArrowBack, "返回", tint = Color.White, modifier = Modifier.padding(12.dp))
            }
        }
    }
}

@Composable
private fun CircleIcon(onClick: () -> Unit, content: @Composable RowScope.() -> Unit) {
    Surface(modifier = Modifier.size(64.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)).clickable(onClick), content = content)
}

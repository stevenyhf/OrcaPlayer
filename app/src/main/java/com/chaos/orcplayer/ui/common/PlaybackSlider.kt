package com.chaos.orcplayer.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.roundToLong

@Composable
fun PlaybackSlider(positionMs: Long, durationMs: Long, onSeek: (Long) -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, Modifier.fillMaxWidth().padding(horizontal = 12.dp)) {
            Text(formatTime(positionMs), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Spacer(Modifier.width(8.dp))
            SeekBar(progress = if (durationMs > 0f) positionMs.toFloat() / durationMs else 0f, onSeek = onSeek, Modifier.weight(1f))
            Spacer(Modifier.width(8.dp))
            Text(formatTime(durationMs), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}

@Composable
private fun SeekBar(progress: Float, onSeek: (Float) -> Unit, modifier: Modifier = Modifier) {
    var currentValue by remember { mutableFloatStateOf(progress) }
    Box(modifier.height(40.dp).clip(RoundedCornerShape(2.dp))
        .background(Color.White.copy(alpha = 0.2f))
        .pointerInput(Unit) { detectDragGestures(onDragStart = {}, onDragEnd = { currentValue = progress },
            onDrag = { _, drag -> currentValue = (currentValue + drag.x / size.width.toFloat()).coerceIn(0f, 1f); onSeek(currentValue) }) }
        .pointerInput(Unit) { detectTapGestures(onPress = { offset ->
            currentValue = (offset.x / size.width.toFloat()).coerceIn(0f, 1f); onSeek(currentValue); awaitRelease()
        }) }) {
        Surface(Modifier.fillMaxSize().wrapContentWidth(Alignment.Start).fillMaxHeight().width((size.width * currentValue).toDp()),
            shape = RoundedCornerShape(2.dp), color = MaterialTheme.colorScheme.primary) {}
    }
}

fun formatTime(ms: Long): String {
    if (ms <= 0L) return "0:00"
    val s = ms / 1000; val h = s / 3600; val m = (s % 3600) / 60; val sec = s % 60
    return if (h > 0) String.format("%d:%02d:%02d", h, m, sec) else String.format("%d:%02d", m, sec)
}

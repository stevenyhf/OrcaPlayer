package com.chaos.orcplayer.ui.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilterChipRow(current: HomeFilter, onSelected: (HomeFilter) -> Unit) {
    val items = listOf(HomeFilter.All to "全部", HomeFilter.Audio to "🎵 音乐", HomeFilter.Video to "🎬 视频", HomeFilter.Recent to "📅 最近")
    Row(verticalAlignment = Alignment.CenterVertically) {
        items.forEachIndexed { i, (f, label) ->
            if (i > 0) Spacer(Modifier.width(6.dp))
            FilterChip(selected = current::class == f::class, onClick = { onSelected(f) },
                label = { Text(label, style = MaterialTheme.typography.labelMedium) }, shape = RoundedCornerShape(16.dp))
        }
    }
}

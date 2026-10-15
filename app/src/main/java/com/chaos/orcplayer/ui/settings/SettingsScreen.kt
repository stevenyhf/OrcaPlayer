package com.chaos.orcplayer.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class SettingsItem(val title: String, val subtitle: String, val onClick: () -> Unit)

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val items = listOf(
        SettingsItem("播放速度", "默认 1.0x", {}),
        SettingsItem("字幕字体大小", "默认中等", {}),
        SettingsItem("深色模式", "跟随系统", {}),
        SettingsItem("扫描排除路径", "0 条规则", {}),
        SettingsItem("关于", "OrcaPlayer v0.1.0", {}),
    )
    Scaffold(topBar = { TopAppBar(title = { Text("设置") }, navigationIcon = { IconButton(onClick = onBack) { Icon(androidx.compose.material.icons.Icons.Filled.ArrowBack, null) } }) }) { pad ->
        LazyColumn(contentPadding = pad, verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.padding(horizontal = 16.dp)) {
            items(items.size) { i -> SettingsRow(items[i]) }
        }
    }
}

@Composable
private fun SettingsRow(item: SettingsItem) {
    Surface(modifier = Modifier.fillMaxWidth().clickable(item.onClick).padding(vertical = 12.dp), shape = RoundedCornerShape(8.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, Modifier.padding(horizontal = 16.dp)) {
            Column { Text(item.title, style = MaterialTheme.typography.bodyLarge); Text(item.subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            Icon(androidx.compose.material.icons.Icons.Filled.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
        }
    }
}

package com.chaos.orcplayer.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chaos.orcplayer.domain.model.MediaItem

@Composable
fun HomeScreen(onMediaSelected: (MediaItem) -> Unit, vm: HomeViewModel = hiltViewModel()) {
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("OrcaPlayer", style = MaterialTheme.typography.titleLarge) },
                actions = { IconButton(onClick = { vm.loadMedia() }) { Icon(androidx.compose.material.icons.Icons.Filled.Refresh, "刷新") } })
        },
        bottomBar = {
            Surface(shadowElevation = 4.dp) {
                FilterChipRow(state.filter) { vm.setFilter(it) }
            }
        }
    ) { pad ->
        Column(Modifier.padding(pad)) {
            OutlinedTextField(value = state.searchQuery, onValueChange = { vm.setSearch(it) },
                placeholder = { Text("搜索...") }, leadingIcon = { Icon(androidx.compose.material.icons.Icons.Filled.Search, null) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp), singleLine = true)

            when {
                state.isLoading && state.items.isEmpty() -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).fillMaxSize())
                state.error != null -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(state.error ?: "Error", color = MaterialTheme.colorScheme.error)
                        Button(onClick = { vm.loadMedia() }) { Text("重试") }
                    }
                }
                state.filteredItems.isEmpty() -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text(if (state.searchQuery.isNotBlank()) "没有匹配" else "未找到媒体文件", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                else -> LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(state.filteredItems, key = { it.uri }) { item ->
                        MediaCard(item = item, onClick = { onMediaSelected(item) })
                    }
                }
            }
        }
    }
}

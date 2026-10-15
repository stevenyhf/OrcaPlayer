package com.chaos.orcplayer.domain.usecase

import com.chaos.orcplayer.data.repository.PlaybackRepository
import com.chaos.orcplayer.domain.model.MediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Determines where to resume playback. Returns 0L for first play or completed items. */
class ResumedPlayUseCase(private val repo: PlaybackRepository) {
    suspend operator fun invoke(item: MediaItem): Long = withContext(Dispatchers.IO) {
        val rec = repo.getRecord(item.uri) ?: return@withContext 0L
        if (rec.playedPercent >= 0.9f || rec.positionMs < item.durationMs * 0.03f) return@withContext 0L
        rec.positionMs
    }
}

// SavePositionUseCase.kt
cat > /home/fangxia/projects/orcplayer/app/src/main/java/com/chaos/orcplayer/domain/usecase/SavePositionUseCase.kt << 'EOF'
package com.chaos.orcplayer.domain.usecase

import com.chaos.orcplayer.data.repository.PlaybackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Saves current playback position. */
class SavePositionUseCase(private val repo: PlaybackRepository) {
    suspend operator fun invoke(uri: String, positionMs: Long, durationMs: Long, type: String) = withContext(Dispatchers.IO) {
        if (positionMs <= 0L || positionMs >= durationMs) return@withContext
        val pct = if (durationMs > 0) (positionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f) else 0f
        repo.upsertRecord(uri, positionMs, durationMs, pct, type.lowercase())
    }
}

package com.chaos.orcplayer.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Database entity tracking last playback position for each media item. */
@Entity(tableName = "playback_records")
data class PlaybackEntity(
    @PrimaryKey val mediaUri: String,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val playedPercent: Float = 0f,
    val lastPlayedAt: Long = System.currentTimeMillis(),
    val type: String = "audio"
)

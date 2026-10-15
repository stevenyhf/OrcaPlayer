package com.chaos.orcplayer.domain.model

sealed interface PlaybackState {
    val isPlaying: Boolean
    val currentMediaIndex: Int
}
data class PlayingState(
    override val isPlaying: Boolean = true,
    override val currentMediaIndex: Int = 0,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val bufferPercentage: Int = 0,
    val speed: Float = 1.0f,
) : PlaybackState
data object IdleState : PlaybackState {
    override val isPlaying = false
    override val currentMediaIndex = -1
}

package com.chaos.orcplayer.domain.model

/** A subtitle track that can be rendered over video playback. */
data class SubtitleTrack(
    val index: Int,
    val label: String,
    val language: String?,
    val uri: String?,
) {
    companion object {
        val OFF = SubtitleTrack(index = -1, label = "关闭", language = null, uri = null)
    }
}

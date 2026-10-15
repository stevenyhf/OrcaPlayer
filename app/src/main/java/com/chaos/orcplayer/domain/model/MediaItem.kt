package com.chaos.orcplayer.domain.model

/** A media item found on device storage with metadata for display and playback. */
data class MediaItem(
    val uri: String,
    val title: String,
    val artist: String? = null,
    val album: String? = null,
    val durationMs: Long,
    val sizeBytes: Long,
    val mimeType: String,
    val type: MediaType,
    val bucketName: String? = null,
    val lastModified: Long = 0L,
) {
    enum class MediaType { Audio, Video }

    /** File stem (name without extension) — used for matching subtitle files. */
    val stem: String get() = title.substringBeforeLast('.')

    companion object {
        val AUDIO_MIMES = setOf(
            "audio/mpeg", "audio/flac", "audio/ogg", "audio/aac", "audio/wav",
            "audio/x-wav", "audio/mp4", "audio/webm", "audio/x-m4a"
        )
        val VIDEO_MIMES = setOf(
            "video/mp4", "video/x-msvideo", "video/webm", "video/x-matroska",
            "video/quicktime", "video/3gpp", "video/ogg"
        )
        fun fromMimeType(mime: String): MediaType = when {
            AUDIO_MIMES.contains(mime) -> MediaType.Audio
            VIDEO_MIMES.contains(mime) -> MediaType.Video
            else -> MediaType.Audio // fallback
        }
    }
}

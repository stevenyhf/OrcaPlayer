package com.chaos.orcplayer.domain.usecase

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.chaos.orcplayer.domain.model.MediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Scans device MediaStore for audio and video files. */
class ScanMediaUseCase(private val context: Context) {
    private val AUDIO_PROJ = arrayOf(
        MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME,
        MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.MIME_TYPE,
        MediaStore.Audio.Media.BUCKET_DISPLAY_NAME, MediaStore.Audio.Media.DATE_MODIFIED,
    )
    private val VIDEO_PROJ = arrayOf(
        MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media.TITLE, MediaStore.Video.Media.ARTIST,
        MediaStore.Video.Media.ALBUM, MediaStore.Video.Media.DURATION,
        MediaStore.Video.Media.SIZE, MediaStore.Video.Media.MIME_TYPE,
        MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATE_MODIFIED,
    )

    suspend fun execute(): List<MediaItem> = withContext(Dispatchers.IO) {
        val results = mutableListOf<MediaItem>()
        scanMedia(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, AUDIO_PROJ) { id, c ->
            MediaItem(
                uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id).toString(),
                title = c.getStringOrNull(MediaStore.Audio.Media.TITLE) ?: c.getStringOrNull(MediaStore.Audio.Media.DISPLAY_NAME) ?: "Unknown",
                artist = c.getStringOrNull(MediaStore.Audio.Media.ARTIST),
                album = c.getStringOrNull(MediaStore.Audio.Media.ALBUM),
                durationMs = c.getLong(MediaStore.Audio.Media.DURATION),
                sizeBytes = c.getLong(MediaStore.Audio.Media.SIZE),
                mimeType = c.getStringOrNull(MediaStore.Audio.Media.MIME_TYPE) ?: "audio/mpeg",
                type = MediaItem.MediaType.Audio,
                bucketName = c.getStringOrNull(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME),
                lastModified = c.getLong(MediaStore.Audio.Media.DATE_MODIFIED) * 1000L,
            )
        }?.let { results.addAll(it) }

        scanMedia(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEO_PROJ) { id, c ->
            MediaItem(
                uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id).toString(),
                title = c.getStringOrNull(MediaStore.Video.Media.TITLE) ?: c.getStringOrNull(MediaStore.Video.Media.DISPLAY_NAME) ?: "Unknown",
                artist = c.getStringOrNull(MediaStore.Video.Media.ARTIST),
                album = c.getStringOrNull(MediaStore.Video.Media.ALBUM),
                durationMs = c.getLong(MediaStore.Video.Media.DURATION),
                sizeBytes = c.getLong(MediaStore.Video.Media.SIZE),
                mimeType = c.getStringOrNull(MediaStore.Video.Media.MIME_TYPE) ?: "video/mp4",
                type = MediaItem.MediaType.Video,
                bucketName = c.getStringOrNull(MediaStore.Video.Media.BUCKET_DISPLAY_NAME),
                lastModified = c.getLong(MediaStore.Video.Media.DATE_MODIFIED) * 1000L,
            )
        }?.let { results.addAll(it) }

        results
    }

    private inline fun <T : MediaItem.MediaType> scanMedia(
        uri: android.net.Uri,
        proj: Array<String>,
        crossinline mapper: (Long, android.database.Cursor) -> MediaItem
    ): List<MediaItem>? {
        context.contentResolver.query(uri, proj, "${MediaStore.MediaColumns.DURATION} > 0", null, "${MediaStore.MediaColumns.DATE_MODIFIED} DESC")
            ?.use { cursor ->
                return cursor.toList { id, c -> mapper(id, c) }
            }
        return emptyList()
    }
}

// Cursor helpers
private inline fun <reified T> android.database.Cursor.getStringOrNull(colName: String): String? {
    val idx = this.getColumnIndexOrThrow(colName)
    return if (this.isNull(idx)) null else this.getString(idx)
}
private inline fun android.database.Cursor.getLong(colName: String): Long {
    return this.getLong(this.getColumnIndexOrThrow(colName))
}
private inline fun <R> android.database.Cursor.toList(mapper: (Long, android.database.Cursor) -> R): List<R> {
    val list = mutableListOf<R>()
    while (this.moveToNext()) {
        list.add(mapper(this.getLong(MediaStore.MediaColumns._ID), this))
    }
    return list
}

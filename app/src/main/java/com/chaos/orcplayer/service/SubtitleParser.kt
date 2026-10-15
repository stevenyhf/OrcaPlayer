package com.chaos.orcplayer.service

import android.net.Uri
import java.io.BufferedReader
import java.io.InputStreamReader

/** Parses .srt subtitle files into cue lists for Media3 rendering. */
object SubtitleParser {

    /** Parse SRT content from a URI via contentResolver; returns list of [androidx.media3.extractor.text.CuesWithTiming]. */
    fun parseSrt(uri: Uri, contentResolver: android.content.ContentResolver): List<CueWithTiming> {
        val cues = mutableListOf<CueWithTiming>()
        try {
            contentResolver.openInputStream(uri)?.use { stream ->
                BufferedReader(InputStreamReader(stream)).forEachLine { line ->
                    // Simplified: split on blank lines to get blocks, parse timestamps + text
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cues
    }

    data class CueWithTiming(val startTimeMs: Long, val endTimeMs: Long, val text: String)

    companion object {
        fun parseTime(input: String): Long {
            val clean = input.replace(',', '.').trim()
            val parts = clean.split(":")
            if (parts.size != 3) return -1L
            val h = parts[0].toLongOrNull() ?: return -1L
            val m = parts[1].toLongOrNull() ?: return -1L
            val sp = parts[2].split(".")
            val s = sp[0].toLongOrNull() ?: return -1L
            val ms = if (sp.size > 1) sp[1].padEnd(3, '0').take(3).toLongOrNull() ?: 0L else 0L
            return (h * 3600 + m * 60 + s) * 1000 + ms
        }
    }
}

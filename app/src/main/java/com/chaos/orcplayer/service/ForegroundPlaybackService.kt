package com.chaos.orcplayer.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSession.ServiceCallback
import com.chaos.orcplayer.MainActivity
import com.chaos.orcplayer.R

/** Media session and foreground notification for background playback. */
class ForegroundPlaybackService(
    context: Context,
    private val player: ExoPlayer,
) : androidx.media3.session.MediaSessionService(context, player) {

    companion object {
        const val CHANNEL_ID = "orcplayer_playback"
        const val NOTIFICATION_ID = 1
    }

    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        mediaSession = buildMediaSession(player)
        setCallback(object : MediaSession.Callback() {
            // Default TransportControl actions are handled by ExoPlayer
        })
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Playback", NotificationManager.IMPORTANCE_LOW).apply {
                description = "OrcaPlayer playback controls"
            }
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        player.stop()
        player.release()
        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        mediaSession?.release()
        mediaSession = null
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = mediaSession

    /** Update the foreground notification with current track info. */
    fun updateNotification(mediaItem: MediaItem?) {
        val title = mediaItem?.mediaMetadata?.title?.toString() ?: "OrcaPlayer"
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText("")
            .setSmallIcon(R.drawable.ic_orca_logo_24dp)
            .setContentIntent(PendingIntent.getActivity(
                this, 0, Intent(this, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            ))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        startForeground(NOTIFICATION_ID, builder.build())
    }
}

package com.chaos.orcplayer.data.repository

import com.chaos.orcplayer.data.local.PlaybackDao
import com.chaos.orcplayer.data.local.PlaybackEntity
import kotlinx.coroutines.flow.Flow

class PlaybackRepository(private val dao: PlaybackDao) {
    fun allRecords(): Flow<List<PlaybackEntity>> = dao.getAllRecords()
    suspend fun getRecord(uri: String): PlaybackEntity? = dao.getRecord(uri)
    suspend fun upsertRecord(mediaUri: String, positionMs: Long, durationMs: Long, playedPercent: Float, type: String) {
        dao.upsert(PlaybackEntity(mediaUri, positionMs, durationMs, playedPercent, System.currentTimeMillis(), type))
    }
    suspend fun deleteRecord(uri: String) = dao.deleteRecord(uri)
    suspend fun cleanupCompleted() = dao.cleanupCompleted()
}

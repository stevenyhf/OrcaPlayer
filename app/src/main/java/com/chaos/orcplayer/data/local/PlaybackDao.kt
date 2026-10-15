package com.chaos.orcplayer.data.local

import androidx.room.*

@Dao
interface PlaybackDao {
    @Query("SELECT * FROM playback_records WHERE mediaUri = :uri LIMIT 1")
    suspend fun getRecord(uri: String): PlaybackEntity?

    @Query("SELECT * FROM playback_records ORDER BY lastPlayedAt DESC")
    fun getAllRecords(): kotlinx.coroutines.flow.Flow<List<PlaybackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(record: PlaybackEntity): Long

    @Query("DELETE FROM playback_records WHERE mediaUri = :uri")
    suspend fun deleteRecord(uri: String): Int

    @Query("DELETE FROM playback_records WHERE playedPercent >= 0.9 OR positionMs = 0")
    suspend fun cleanupCompleted(): Int
}

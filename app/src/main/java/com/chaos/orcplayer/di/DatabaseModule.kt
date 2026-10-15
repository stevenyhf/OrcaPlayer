package com.chaos.orcplayer.di

import android.content.Context
import androidx.room.Room
import com.chaos.orcplayer.data.local.PlaybackDao
import com.chaos.orcplayer.data.local.PlayerDatabase
import com.chaos.orcplayer.data.repository.PlaybackRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun providePlayerDatabase(@ApplicationContext ctx: Context): PlayerDatabase =
        Room.databaseBuilder(ctx.applicationContext, PlayerDatabase::class.java, "orcplayer.db")
            .fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun providePlaybackDao(db: PlayerDatabase): PlaybackDao = db.playbackDao()

    @Singleton
    @Provides
    fun providePlaybackRepository(dao: PlaybackDao): PlaybackRepository = PlaybackRepository(dao)
}

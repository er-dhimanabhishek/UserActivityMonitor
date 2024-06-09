package com.erabhidman.useractivitymonitor.di

import android.content.Context
import androidx.room.Room
import com.erabhidman.useractivitymonitor.roomdb.database.RoomDataDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideRoomDB(@ApplicationContext context: Context): RoomDataDB {
        return Room.databaseBuilder(context, RoomDataDB::class.java, "application_usage")
            .build()
    }

}
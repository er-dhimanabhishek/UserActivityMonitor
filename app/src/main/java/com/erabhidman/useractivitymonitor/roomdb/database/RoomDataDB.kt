package com.erabhidman.useractivitymonitor.roomdb.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.erabhidman.useractivitymonitor.model.AppUsageEntity
import com.erabhidman.useractivitymonitor.roomdb.dao.AppUsageDao

@Database(entities = [AppUsageEntity::class], version = 1, exportSchema = false)
abstract class RoomDataDB: RoomDatabase() {

    abstract fun appUsageDao(): AppUsageDao

    companion object{
        @Volatile
        private var INSTANCE: RoomDataDB? = null

        fun getInstance(context: Context): RoomDataDB{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context,
                    RoomDataDB::class.java,
                "application_usage").build()
                INSTANCE = instance
                instance
            }
        }
    }

}
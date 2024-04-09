package com.erabhidman.useractivitymonitor.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.erabhidman.useractivitymonitor.model.AppUsageEntity
import com.erabhidman.useractivitymonitor.model.AppUsageLastTimeAndSessionTimeEntity

@Dao
interface AppUsageDao {

    //new table to store events with app usage frequency
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAppUsageData(appUsageEntity: List<AppUsageEntity>)

    @Query("SELECT DISTINCT `package_name` FROM appusagedatatrack WHERE `date` = :date")
    fun getDailyUsageEventUniquePackageNameList(date: String): List<String>

    @Query("SELECT SUM(session_time_foreground) AS totalSessionTimeForeground FROM appusagedatatrack WHERE package_name = :packageName AND date = :date")
    fun getSessionTimeForegroundSumForAppEvents(packageName: String, date: String): Long

    @Query("SELECT last_time_used, session_time_foreground FROM appusagedatatrack WHERE package_name = :packageName AND date = :date")
    fun getLastTimeAndSessionList(
        packageName: String,
        date: String
    ): List<AppUsageLastTimeAndSessionTimeEntity>

    @Query("SELECT last_time_used FROM appusagedatatrack WHERE `package_name` = :packageName AND `date` = :date ORDER BY `last_time_used` DESC LIMIT 1")
    fun getLastUsedTimeStamp(packageName: String, date: String): Long

}
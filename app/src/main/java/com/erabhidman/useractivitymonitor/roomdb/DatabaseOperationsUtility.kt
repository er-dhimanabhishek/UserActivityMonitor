package com.erabhidman.useractivitymonitor.roomdb

import android.content.Context
import com.erabhidman.useractivitymonitor.model.AppUsageEntity
import com.erabhidman.useractivitymonitor.roomdb.dao.AppUsageDao
import com.erabhidman.useractivitymonitor.roomdb.database.RoomDataDB

object DatabaseOperationsUtility {

    private var mAppUsageDao: AppUsageDao? = null

    fun addAppUsageDataToDB(
        context: Context,
        appUsageList: List<AppUsageEntity>
    ) {
        val database: RoomDataDB = RoomDataDB.getInstance(context)
        synchronized(RoomDataDB.getInstance(context)) {
            mAppUsageDao =
                database.appUsageDao()
            mAppUsageDao?.insertAppUsageData(appUsageList)
        }
    }

    fun getDailyUsageEventUniquePackageNameList(context: Context, date: String): List<String>? {
        synchronized(RoomDataDB.getInstance(context)) {
            val database = RoomDataDB.getInstance(context)
            mAppUsageDao =
                database.appUsageDao()
            return mAppUsageDao?.getDailyUsageEventUniquePackageNameList(
                date
            )
        }
    }

    fun getSessionTimeForegroundSumForAppEvents(
        context: Context,
        appPackageName: String,
        date: String
    ): Long? {
        synchronized(RoomDataDB.getInstance(context)) {
            val database = RoomDataDB.getInstance(context)
            mAppUsageDao =
                database.appUsageDao()
            return mAppUsageDao?.getSessionTimeForegroundSumForAppEvents(
                appPackageName,
                date
            )
        }
    }

}
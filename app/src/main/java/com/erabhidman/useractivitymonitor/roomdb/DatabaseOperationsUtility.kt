package com.erabhidman.useractivitymonitor.roomdb

import com.erabhidman.useractivitymonitor.model.AppUsageEntity
import com.erabhidman.useractivitymonitor.roomdb.dao.AppUsageDao
import com.erabhidman.useractivitymonitor.roomdb.database.RoomDataDB

object DatabaseOperationsUtility {

    private var mAppUsageDao: AppUsageDao? = null

    fun addAppUsageDataToDB(
        appUsageList: List<AppUsageEntity>,
        productDB: RoomDataDB
    ) {
        synchronized(productDB) {
            mAppUsageDao =
                productDB.appUsageDao()
            mAppUsageDao?.insertAppUsageData(appUsageList)
        }
    }

    fun getDailyUsageEventUniquePackageNameList(
        date: String,
        productDB: RoomDataDB
    ): List<String>? {
        synchronized(productDB) {
            mAppUsageDao =
                productDB.appUsageDao()
            return mAppUsageDao?.getDailyUsageEventUniquePackageNameList(
                date
            )
        }
    }

    fun getSessionTimeForegroundSumForAppEvents(
        appPackageName: String,
        date: String,
        productDB: RoomDataDB
    ): Long? {
        synchronized(productDB) {
            mAppUsageDao =
                productDB.appUsageDao()
            return mAppUsageDao?.getSessionTimeForegroundSumForAppEvents(
                appPackageName,
                date
            )
        }
    }

}
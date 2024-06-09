package com.erabhidman.useractivitymonitor.repository

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.erabhidman.useractivitymonitor.appusagedata.AppUsageDataHandler
import com.erabhidman.useractivitymonitor.model.AppUsageEntity
import com.erabhidman.useractivitymonitor.roomdb.DatabaseOperationsUtility
import com.erabhidman.useractivitymonitor.roomdb.database.RoomDataDB
import com.erabhidman.useractivitymonitor.utils.DateTimeUtils
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class UserDailyUsageRepository @Inject constructor(private val productDB: RoomDataDB,
                                private val appUsageDataHandler: AppUsageDataHandler){

    private val appUsageLiveData = MutableLiveData<List<AppUsageEntity>>()
    val appUsageDataObject : LiveData<List<AppUsageEntity>>
        get() = appUsageLiveData

    private val packageNameListLiveData = MutableLiveData<List<String>>()
    val packageNameListDataObject : LiveData<List<String>>
        get() = packageNameListLiveData

    fun getUserDailyAppUsageData() {
        val appUsageList = appUsageDataHandler.getTimeSpentOnApps(
            DateTimeUtils.getMidNightTimeInMillsForCurrentDay(), DateTimeUtils.getMidNightTimeInMillsForNextDay())

        runBlocking {
            if (appUsageList.isNotEmpty()){
                DatabaseOperationsUtility.addAppUsageDataToDB(appUsageList, productDB)
            }
            Handler(Looper.getMainLooper()).post {
                appUsageLiveData.value = appUsageList
            }
        }
    }

    fun getUniqueAppPackageNameList(date: String){
       val packageNameList = DatabaseOperationsUtility.getDailyUsageEventUniquePackageNameList(date, productDB)
        Handler(Looper.getMainLooper()).post {
            packageNameListLiveData.value = packageNameList
        }
    }

    fun getAppUsageEventTotalSessionTime(appPackage: String, date: String): Long? {
       return DatabaseOperationsUtility.getSessionTimeForegroundSumForAppEvents(appPackage, date, productDB)
    }

}
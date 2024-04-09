package com.erabhidman.useractivitymonitor.repository

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.erabhidman.useractivitymonitor.appusagedata.AppUsageDataHandler
import com.erabhidman.useractivitymonitor.model.AppUsageEntity
import com.erabhidman.useractivitymonitor.roomdb.DatabaseOperationsUtility
import com.erabhidman.useractivitymonitor.utils.DateTimeUtils
import kotlinx.coroutines.runBlocking

class UserDailyUsageRepository {

    private val appUsageLiveData = MutableLiveData<List<AppUsageEntity>>()
    val appUsageDataObject : LiveData<List<AppUsageEntity>>
        get() = appUsageLiveData

    private val packageNameListLiveData = MutableLiveData<List<String>>()
    val packageNameListDataObject : LiveData<List<String>>
        get() = packageNameListLiveData

    fun getUserDailyAppUsageData(context: Context) {
        val appUsageDataHandler =   AppUsageDataHandler()
        val appUsageList = appUsageDataHandler.getTimeSpentOnApps(context,
            DateTimeUtils.getMidNightTimeInMillsForCurrentDay(), DateTimeUtils.getMidNightTimeInMillsForNextDay())

        runBlocking {
            if (appUsageList.isNotEmpty()){
                DatabaseOperationsUtility.addAppUsageDataToDB(context, appUsageList)
            }
        }
        Handler(Looper.getMainLooper()).post {
            appUsageLiveData.value = appUsageList
        }
    }

    fun getUniqueAppPackageNameList(context: Context, date: String){
       val packageNameList = DatabaseOperationsUtility.getDailyUsageEventUniquePackageNameList(context, date)
        Handler(Looper.getMainLooper()).post {
            packageNameListLiveData.value = packageNameList
        }
    }

    fun getAppUsageEventTotalSessionTime(context: Context, appPackage: String, date: String): Long? {
       return DatabaseOperationsUtility.getSessionTimeForegroundSumForAppEvents(context, appPackage, date)
    }

}
package com.erabhidman.useractivitymonitor.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erabhidman.useractivitymonitor.model.AppUsageEntity
import com.erabhidman.useractivitymonitor.repository.UserDailyUsageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserDailyUsageViewModel(private val appUsageRepository: UserDailyUsageRepository): ViewModel() {

    var activityPaused = false

    val appUsageDataObj : LiveData<List<AppUsageEntity>>
    get() = appUsageRepository.appUsageDataObject

    val uniquePackageNameListDataObj : LiveData<List<String>>
        get() = appUsageRepository.packageNameListDataObject

    fun getAppUsageData(context: Context){
        Log.e("TAG", "Initiate system.")
        viewModelScope.launch(Dispatchers.IO) {
            appUsageRepository.getUserDailyAppUsageData(context)
        }
    }

    fun getUniquePackageNameList(context: Context, date: String){
        viewModelScope.launch(Dispatchers.IO) {
            appUsageRepository.getUniqueAppPackageNameList(context, date)
        }
    }

    suspend fun getAppUsageEventTotalSessionTime(
        context: Context, appPackage: String, date: String): Long? = withContext(Dispatchers.IO){
        //Log.e("TAG", "Total session sum date: $date")
        return@withContext appUsageRepository.getAppUsageEventTotalSessionTime(context, appPackage, date)
    }

}
package com.erabhidman.useractivitymonitor.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erabhidman.useractivitymonitor.model.AppUsageEntity
import com.erabhidman.useractivitymonitor.repository.UserDailyUsageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserDailyUsageViewModel(private val appUsageRepository: UserDailyUsageRepository): ViewModel() {

    val appUsageDataObj : LiveData<List<AppUsageEntity>>
    get() = appUsageRepository.appUsageDataObject

    val uniquePackageNameListDataObj : LiveData<List<String>>
        get() = appUsageRepository.packageNameListDataObject

    fun getAppUsageData(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            appUsageRepository.getUserDailyAppUsageData(context)
        }
    }

    fun getUniquePackageNameList(context: Context, date: String){
        viewModelScope.launch(Dispatchers.IO) {
            appUsageRepository.getUniqueAppPackageNameList(context, date)
        }
    }

    fun getAppUsageEventTotalSessionTime(context: Context, appPackage: String, date: String): Long {
        var totalSessionTime = 0L
        runBlocking {
            viewModelScope.launch(Dispatchers.Default) {
                val deferred =  viewModelScope.async(Dispatchers.IO) {
                   return@async appUsageRepository.getAppUsageEventTotalSessionTime(context, appPackage, date)
                }
                totalSessionTime = deferred.await() as Long
            }
        }
        return totalSessionTime
    }

}
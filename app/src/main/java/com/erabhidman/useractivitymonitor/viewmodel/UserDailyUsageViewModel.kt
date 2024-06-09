package com.erabhidman.useractivitymonitor.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erabhidman.useractivitymonitor.model.AppUsageEntity
import com.erabhidman.useractivitymonitor.repository.UserDailyUsageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserDailyUsageViewModel @Inject constructor(private val appUsageRepository: UserDailyUsageRepository): ViewModel() {

    var activityPaused = false

    val appUsageDataObj : LiveData<List<AppUsageEntity>>
    get() = appUsageRepository.appUsageDataObject

    val uniquePackageNameListDataObj : LiveData<List<String>>
        get() = appUsageRepository.packageNameListDataObject

    fun getAppUsageData(){
        Log.e("TAG", "Initiate system.")
        viewModelScope.launch(Dispatchers.IO) {
            appUsageRepository.getUserDailyAppUsageData()
        }
    }

    fun getUniquePackageNameList(date: String){
        viewModelScope.launch(Dispatchers.IO) {
            appUsageRepository.getUniqueAppPackageNameList(date)
        }
    }

    suspend fun getAppUsageEventTotalSessionTime(appPackage: String,
                                                 date: String): Long? = withContext(Dispatchers.IO){
        //Log.e("TAG", "Total session sum date: $date")
        return@withContext appUsageRepository.getAppUsageEventTotalSessionTime(appPackage, date)
    }

}
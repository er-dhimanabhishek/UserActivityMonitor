package com.erabhidman.useractivitymonitor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.erabhidman.useractivitymonitor.repository.UserDailyUsageRepository

class UserDailyUsageViewModelFactory(private val userDailyUsageRepository: UserDailyUsageRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserDailyUsageViewModel(userDailyUsageRepository) as T
    }

}
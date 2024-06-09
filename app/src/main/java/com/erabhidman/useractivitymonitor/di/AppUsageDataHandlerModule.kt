package com.erabhidman.useractivitymonitor.di

import android.content.Context
import com.erabhidman.useractivitymonitor.appusagedata.AppUsageDataHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppUsageDataHandlerModule {

    @Singleton
    @Provides
    fun provideAppUsageDataHandler(@ApplicationContext context: Context): AppUsageDataHandler{
        return AppUsageDataHandler(context)
    }

}
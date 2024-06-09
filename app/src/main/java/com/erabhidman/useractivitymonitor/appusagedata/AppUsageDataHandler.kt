package com.erabhidman.useractivitymonitor.appusagedata

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import com.erabhidman.useractivitymonitor.model.AppUsageEntity
import com.erabhidman.useractivitymonitor.utils.DateTimeUtils
import java.util.Calendar
import javax.inject.Inject

class AppUsageDataHandler @Inject constructor(private val ctx: Context) {

    fun getTimeSpentOnApps(beginTimeMills: Long, endTimeMills: Long): List<AppUsageEntity> {
        val allEvents = ArrayList<UsageEvents.Event>()
        val appUsageMap = HashMap<String, Long>()

        val updatedAppUsageList = ArrayList<AppUsageEntity>()

        val usageStatsManager =
            ctx.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val usageEvents = usageStatsManager.queryEvents(beginTimeMills, endTimeMills)

        while (usageEvents.hasNextEvent()) {
            val currentEvent = UsageEvents.Event()
            usageEvents.getNextEvent(currentEvent)

            //check if the events returned are from the same day as we requested
            val result: Int = DateTimeUtils.isSameDate(
                currentEvent.timeStamp,
                getParticularDayDateTimeMills()
            )
            if (currentEvent.eventType == UsageEvents.Event.ACTIVITY_RESUMED
                || currentEvent.eventType == UsageEvents.Event.ACTIVITY_PAUSED
            ) {
                allEvents.add(currentEvent)
                val key = currentEvent.packageName
                if (appUsageMap[key] == null) appUsageMap[key] = 0L
            }
        }

        if(allEvents.isNotEmpty()) {
            allEvents.forEachIndexed { index, event ->
                //Log.e("TAG", "Package: ${event.packageName} Event: ${event.timeStamp}")
                if (index < allEvents.size -1){
                    val e0 = event
                    val e1 = allEvents[index + 1]

                    if (index == 0 && e0.eventType == UsageEvents.Event.ACTIVITY_PAUSED) {
                        // first event ACTIVITY_PAUSED, calculate difference between E0 event time stamp and day starting time,
                        // and add to appUsageMap
                        val diff = e0.timeStamp - DateTimeUtils.getMidNightTimeInMillsForCurrentDay()
                        val prev: Long = if (appUsageMap[e0.packageName] != null) {
                            appUsageMap[e0.packageName]!!
                        } else {
                            0
                        }
                        appUsageMap[e0.packageName] = prev + diff
                        val appUsageEntity = AppUsageEntity(
                            e0.packageName,
                            e0.timeStamp,
                            diff,
                            DateTimeUtils.getFormattedDateFromMills(e0.timeStamp)?:"01-01-2000"
                        )
                        updatedAppUsageList.add(appUsageEntity)
                    }
                    else if (index == allEvents.size - 2 && e1.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                        //last event ACTIVITY_RESUMED, calculate difference between day end time or job time/current time and
                        // E1 event time stamp, and add to appUsageMap
                        //long diff = DateTimeUtils.getMidNightTimeInMillsForNextDay() - E1.getTimeStamp();
                        val diff = System.currentTimeMillis() - e1.timeStamp
                        val prev: Long = if (appUsageMap[e1.packageName] != null) {
                            appUsageMap[e1.packageName]!!
                        } else {
                            0
                        }
                        appUsageMap[e1.packageName] = prev + diff
                        val appUsageEntity = AppUsageEntity(
                            e1.packageName,
                            System.currentTimeMillis(),
                            diff,
                            DateTimeUtils.getFormattedDateFromMills(e1.timeStamp)?:"01-01-2000"
                        )
                        updatedAppUsageList.add(appUsageEntity)
                    } else if (e0.eventType == UsageEvents.Event.ACTIVITY_RESUMED
                        && e1.eventType == UsageEvents.Event.ACTIVITY_PAUSED
                        && e0.packageName == e1.packageName) {
                        val diff = e1.timeStamp - e0.timeStamp
                        val prev: Long = if (appUsageMap[e0.packageName] != null) {
                            appUsageMap[e0.packageName]!!
                        } else {
                            0
                        }
                        appUsageMap[e0.packageName] = prev + diff
                        val appUsageEntity = AppUsageEntity(
                            e0.packageName,
                            e1.timeStamp,
                            diff,
                            DateTimeUtils.getFormattedDateFromMills(e0.timeStamp)?:"01-01-2000"
                        )
                        updatedAppUsageList.add(appUsageEntity)
                    }
                    else if (e0.eventType == UsageEvents.Event.ACTIVITY_RESUMED
                        && e1.eventType != UsageEvents.Event.ACTIVITY_PAUSED
                    ) {
                        val diff: Long = e1.timeStamp - e0.timeStamp
                        val prev: Long = if (appUsageMap[e0.packageName] != null) {
                            appUsageMap[e0.packageName]!!
                        } else {
                            0
                        }
                        appUsageMap[e0.packageName] = prev + diff
                        val appUsageEntity = AppUsageEntity(
                            e0.packageName,
                            e1.timeStamp,
                            diff,
                            DateTimeUtils.getFormattedDateFromMills(e0.timeStamp)?:"01-01-2000"
                        )
                        updatedAppUsageList.add(appUsageEntity)
                    }
                }
            }

            val lastEvent = allEvents[allEvents.size - 1]
            if (lastEvent.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                val currentRunningPackageName = lastEvent.packageName
                val diff = System.currentTimeMillis() - lastEvent.timeStamp
                val prev: Long = if (appUsageMap[currentRunningPackageName] != null) {
                    appUsageMap[currentRunningPackageName]!!
                } else {
                    0
                }
                appUsageMap[currentRunningPackageName] = prev + diff
                //appUsageMap["current$currentRunningPackageName"] = -1L //for notification purpose

                val appUsageEntity = AppUsageEntity(
                    lastEvent.packageName,
                    lastEvent.timeStamp,
                    diff,
                    DateTimeUtils.getFormattedDateFromMills(lastEvent.timeStamp)?:"01-01-2000"
                )
                updatedAppUsageList.add(appUsageEntity)

            }
        }
        return updatedAppUsageList
    }

    private fun getParticularDayDateTimeMills(): Long {
        // Set the start time to midnight of the current day
        val calendar = Calendar.getInstance()
        //below line for previous day
        //calendar.add(Calendar.DAY_OF_MONTH, -1)

        // Set time components to 12 AM
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.timeInMillis
    }

    private fun getEventType(eventCode: Int): String {
        return if (eventCode == UsageEvents.Event.ACTIVITY_RESUMED) {
            "ACTIVITY_RESUMED"
        } else if (eventCode == UsageEvents.Event.ACTIVITY_PAUSED) {
            "ACTIVITY_PAUSED"
        } else "OTHER"
    }
}

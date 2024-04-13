package com.erabhidman.useractivitymonitor.utils

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateTimeUtils {

    const val DATE_FORMAT = "dd-MM-yyyy"

    fun isSameDate(appUsageDateMills: Long, currentDateMills: Long): Int {
        val previousDate = Date(appUsageDateMills)
        val currentDate = Date(currentDateMills)
        // Format Date objects to strings with date part only
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        val previousDateString = dateFormat.format(previousDate)
        val currentDateString = dateFormat.format(currentDate)

        // Parse the strings back to Date objects
        var parsedPreviousDate: Date? = null
        var parsedCurrentDate: Date? = null
        try {
            parsedPreviousDate = dateFormat.parse(previousDateString)
            parsedCurrentDate = dateFormat.parse(currentDateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        var result = -1
        if (parsedPreviousDate != null && parsedCurrentDate != null) {
            result = parsedPreviousDate.compareTo(parsedCurrentDate)
        }
        return result
    }

    fun getMidNightTimeInMillsForCurrentDay(): Long {
        // Set the start time to midnight of the current day
        val calendar = Calendar.getInstance()

        // Set time components to 12 AM
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.timeInMillis
    }

    fun getMidNightTimeInMillsForNextDay(): Long {
        // Set the start time to midnight of the current day
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 23
        calendar[Calendar.MINUTE] = 59
        calendar[Calendar.SECOND] = 59
        calendar[Calendar.MILLISECOND] = 999
        return calendar.timeInMillis
    }

    fun getFormattedDateFromMills(timeMills: Long): String? {
        val dateObj = Date(timeMills)
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(dateObj)
    }

    fun getDateForPreviousDay(): String {
        // Set the date to the previous day
        val calendar = Calendar.getInstance()
        //calendar.add(Calendar.DAY_OF_MONTH, -1)

        // Format the date as "dd-MM-yyyy"
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun getCustomFormattedDatFromMills(timeMills: Long): String {
        // Create a SimpleDateFormat object with the desired date format
        val sdf = SimpleDateFormat("EEE dd MMM", Locale.getDefault())

        // Convert the current time in milliseconds to a Date object
        val currentDate = Date(timeMills)

        // Format the Date object using SimpleDateFormat
        return sdf.format(currentDate)
    }

}
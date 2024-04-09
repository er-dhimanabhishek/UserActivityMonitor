package com.erabhidman.useractivitymonitor.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "appusagedatatrack", primaryKeys = ["package_name", "date", "last_time_used"])
data class AppUsageEntity(
    @ColumnInfo(name = "package_name")
    var packageName: String,
    @ColumnInfo(name = "last_time_used")
    var lastTimeUsed: Long,
    @ColumnInfo(name = "session_time_foreground")
    var sessionTimeForeground: Long,
    @ColumnInfo(name = "date")
    var date: String)

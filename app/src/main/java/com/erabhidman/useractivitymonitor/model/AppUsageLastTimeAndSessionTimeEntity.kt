package com.erabhidman.useractivitymonitor.model

import androidx.room.ColumnInfo

data class AppUsageLastTimeAndSessionTimeEntity(
    @ColumnInfo(name = "last_time_used")
    var lastTimeUsed: Long? = null,
    @ColumnInfo(name = "session_time_foreground")
    var sessionTimeForeground: Long? = null)

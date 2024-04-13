package com.erabhidman.useractivitymonitor.utils

import android.app.AppOpsManager
import android.content.Context
import android.content.Context.APP_OPS_SERVICE
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Process


object AppInfoUtils {

    fun getAppLabelFromPackageName(context: Context, packageName: String): String? {
        val packageManager = context.packageManager
        return try {
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getApplicationLabel(applicationInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    fun getAppIconFromPackageName(context: Context, packageName: String): Drawable? {
        val packageManager = context.packageManager
        return try {
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getApplicationIcon(applicationInfo)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    fun checkUsageStatsPermission(context: Context, packageName: String): Boolean {
        val appOpsManager =
            context.getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode: Int = appOpsManager.checkOpNoThrow(
            "android:get_usage_stats",
            Process.myUid(), packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

}
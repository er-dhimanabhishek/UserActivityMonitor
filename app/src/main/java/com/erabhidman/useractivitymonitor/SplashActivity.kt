package com.erabhidman.useractivitymonitor

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.erabhidman.useractivitymonitor.databinding.ActivitySplashBinding
import com.erabhidman.useractivitymonitor.utils.AppInfoUtils
import kotlinx.coroutines.delay

class SplashActivity : AppCompatActivity() {
    lateinit var _binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        lifecycleScope.launchWhenResumed {
            delay(4000)
            if (!AppInfoUtils.checkUsageStatsPermission(this@SplashActivity, packageName)) {
                // Navigate the user to the permission settings
                startActivity(Intent(this@SplashActivity, UsagePermissionActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, UserDailyActivity::class.java))
            }
            finish()
        }

    }

}
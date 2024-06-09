package com.erabhidman.useractivitymonitor

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.erabhidman.useractivitymonitor.databinding.ActivitySplashBinding
import com.erabhidman.useractivitymonitor.utils.AppInfoUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
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
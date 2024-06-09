package com.erabhidman.useractivitymonitor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.erabhidman.useractivitymonitor.databinding.ActivityUsagePermissionBinding
import com.erabhidman.useractivitymonitor.utils.AppInfoUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsagePermissionActivity : AppCompatActivity() {
    lateinit var _binding: ActivityUsagePermissionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this,R.layout.activity_usage_permission)

        _binding.btnProvideAccess.setOnClickListener {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenResumed {
            if (AppInfoUtils.checkUsageStatsPermission(this@UsagePermissionActivity, packageName)){
                startActivity(Intent(this@UsagePermissionActivity, UserDailyActivity::class.java))
                finish()
            }
        }
    }

}
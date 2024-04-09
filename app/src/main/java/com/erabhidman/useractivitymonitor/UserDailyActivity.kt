package com.erabhidman.useractivitymonitor

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.erabhidman.useractivitymonitor.adapter.AppUsageListAdapter
import com.erabhidman.useractivitymonitor.adapter.ItemClickListener
import com.erabhidman.useractivitymonitor.databinding.ActivityDailyUsageBinding
import com.erabhidman.useractivitymonitor.model.AppUsageTotalTimeEntity
import com.erabhidman.useractivitymonitor.repository.UserDailyUsageRepository
import com.erabhidman.useractivitymonitor.utils.DateTimeUtils
import com.erabhidman.useractivitymonitor.viewmodel.UserDailyUsageViewModel
import com.erabhidman.useractivitymonitor.viewmodel.UserDailyUsageViewModelFactory
import kotlinx.coroutines.runBlocking


class UserDailyActivity : AppCompatActivity() {
    lateinit var _binding: ActivityDailyUsageBinding
    lateinit var userDailyUsageViewModel: UserDailyUsageViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_daily_usage)

        if (!checkUsageStatsPermission(this)) {
            // Navigate the user to the permission settings
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(intent)
        } else {
            userDailyUsageViewModel = ViewModelProvider(
                this,
                UserDailyUsageViewModelFactory(UserDailyUsageRepository())
            )[UserDailyUsageViewModel::class.java]
            showProgressBar()
            userDailyUsageViewModel.getAppUsageData(this)
            userDailyUsageViewModel.appUsageDataObj.observe(this) { _ ->
                //fetch data from room db
                userDailyUsageViewModel.getUniquePackageNameList(
                    context = applicationContext,
                    DateTimeUtils.getDateForPreviousDay()
                )
            }

            userDailyUsageViewModel.uniquePackageNameListDataObj.observe(this) { appPackageNameList ->
                val totalAppUsageDurationList = ArrayList<AppUsageTotalTimeEntity>()
                if (appPackageNameList.isNotEmpty()) {
                    appPackageNameList.forEachIndexed { index, s ->
                        Log.e("TAG", "DB data fetch: App package $s")
                        var totalSessionTime = 0L
                        runBlocking {
                            totalSessionTime = userDailyUsageViewModel.getAppUsageEventTotalSessionTime(
                                applicationContext,
                                appPackageNameList[index],
                                DateTimeUtils.getDateForPreviousDay()
                            )
                        }
                        totalAppUsageDurationList.add(
                            AppUsageTotalTimeEntity(
                                appPackageNameList[index],
                                totalSessionTime
                            )
                        )
                    }
                    populateData(totalAppUsageDurationList)
                }else{
                    // no data
                    Toast.makeText(this, "No data available.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun populateData(totalAppUsageDurationList: List<AppUsageTotalTimeEntity>) {

        val adapter = AppUsageListAdapter(totalAppUsageDurationList, object: ItemClickListener{
            override fun onItemClicked(appPackageName: String) {
                viewDetailedTimeline()
            }

        })
        _binding.rvAppDataUsageForDay.layoutManager = LinearLayoutManager(this)
        _binding.rvAppDataUsageForDay.adapter = adapter
        hideProgressBar()
    }

    private fun viewDetailedTimeline() {

    }

    private fun showProgressBar() {
        _binding.progressBar.visibility = View.VISIBLE
        _binding.rvAppDataUsageForDay.visibility = View.GONE
    }

    private fun hideProgressBar() {
        _binding.progressBar.visibility = View.GONE
        _binding.rvAppDataUsageForDay.visibility = View.VISIBLE
    }

    private fun checkUsageStatsPermission(context: Context): Boolean {
        val appOpsManager =
            context.getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode: Int = appOpsManager.checkOpNoThrow(
            "android:get_usage_stats",
            Process.myUid(), packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

}
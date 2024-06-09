package com.erabhidman.useractivitymonitor

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.erabhidman.useractivitymonitor.adapter.AppUsageListAdapter
import com.erabhidman.useractivitymonitor.adapter.ItemClickListener
import com.erabhidman.useractivitymonitor.databinding.ActivityDailyUsageBinding
import com.erabhidman.useractivitymonitor.model.AppUsageTotalTimeEntity
import com.erabhidman.useractivitymonitor.repository.UserDailyUsageRepository
import com.erabhidman.useractivitymonitor.utils.DateTimeUtils
import com.erabhidman.useractivitymonitor.viewmodel.UserDailyUsageViewModel
import com.erabhidman.useractivitymonitor.viewmodel.UserDailyUsageViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserDailyActivity : AppCompatActivity() {
    lateinit var _binding: ActivityDailyUsageBinding
    lateinit var userDailyUsageViewModel: UserDailyUsageViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_daily_usage)

        userDailyUsageViewModel = ViewModelProvider(this)[UserDailyUsageViewModel::class.java]

        showProgressBar()

        userDailyUsageViewModel.getAppUsageData()
        userDailyUsageViewModel.appUsageDataObj.observe(this) { _ ->
            //fetch data from room db
            userDailyUsageViewModel.getUniquePackageNameList(
                DateTimeUtils.getDateForPreviousDay()
            )
        }

        userDailyUsageViewModel.uniquePackageNameListDataObj.observe(this) { appPackageNameList ->
            val totalAppUsageDurationList = ArrayList<AppUsageTotalTimeEntity>()
            if (appPackageNameList.isNotEmpty()) {
                lifecycleScope.launch(Dispatchers.Default) {
                    appPackageNameList.forEachIndexed { index, s ->
                        Log.e("TAG", "DB data fetch: App package $s")
                        totalAppUsageDurationList.add(
                            AppUsageTotalTimeEntity(
                                appPackageNameList[index],
                                userDailyUsageViewModel.getAppUsageEventTotalSessionTime(
                                    appPackageNameList[index],
                                    DateTimeUtils.getDateForPreviousDay()
                                ) ?: 0L
                            )
                        )
                    }
                    Log.e("TAG", "Final list size: ${totalAppUsageDurationList.size}")
                    Handler(Looper.getMainLooper()).post {
                        populateData(totalAppUsageDurationList)
                    }
                }
            } else {
                // no data
                Toast.makeText(this, "No data available in db.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun populateData(totalAppUsageDurationList: List<AppUsageTotalTimeEntity>) {

        val adapter = AppUsageListAdapter(object: ItemClickListener{
            override fun onItemClicked(appPackageName: String) {
                viewDetailedTimeline()
            }

        })
        adapter.submitList(totalAppUsageDurationList)
        _binding.rvAppDataUsageForDay.layoutManager = LinearLayoutManager(this)
        _binding.rvAppDataUsageForDay.adapter = adapter
        _binding.tvDate.text = DateTimeUtils.getCustomFormattedDatFromMills(System.currentTimeMillis())
        hideProgressBar()
    }

    private fun viewDetailedTimeline() {
        startActivity(
            Intent(
            this@UserDailyActivity,
                TimelineActivity::class.java))
    }

    private fun showProgressBar() {
        _binding.progressBar.visibility = View.VISIBLE
        _binding.rvAppDataUsageForDay.visibility = View.GONE
        _binding.tvDate.visibility = View.GONE
    }

    private fun hideProgressBar() {
        _binding.progressBar.visibility = View.GONE
        _binding.rvAppDataUsageForDay.visibility = View.VISIBLE
        _binding.tvDate.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        if (userDailyUsageViewModel.activityPaused){
            userDailyUsageViewModel.getAppUsageData()
            userDailyUsageViewModel.activityPaused = false
        }
    }

    override fun onPause() {
        super.onPause()
        userDailyUsageViewModel.activityPaused = true
    }

}
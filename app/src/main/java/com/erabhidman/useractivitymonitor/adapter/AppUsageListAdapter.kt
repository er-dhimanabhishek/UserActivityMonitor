package com.erabhidman.useractivitymonitor.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.erabhidman.useractivitymonitor.R
import com.erabhidman.useractivitymonitor.model.AppUsageTotalTimeEntity
import com.erabhidman.useractivitymonitor.utils.AppInfoUtils

class AppUsageListAdapter(private val appUsageList: List<AppUsageTotalTimeEntity>, val clickListener: ItemClickListener) :
    RecyclerView.Adapter<AppUsageListAdapter.AppUsageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppUsageViewHolder {
        return AppUsageViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(
                R.layout.app_usage_list_item, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return appUsageList.size
    }

    override fun onBindViewHolder(holder: AppUsageViewHolder, position: Int) {
        holder.bindAppUsageData(appUsageList[position])
    }

    class AppUsageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val appName = view.findViewById<TextView>(R.id.tvAppName)
        private val totalAppUsageTime = view.findViewById<TextView>(R.id.tvTotalAppUsageTime)

        fun bindAppUsageData(item: AppUsageTotalTimeEntity){
            appName.text = if (AppInfoUtils.getAppLabelFromPackageName(
                    itemView.context, item.appPackageName
                ) == null
            ) item.appPackageName
            else AppInfoUtils.getAppLabelFromPackageName(
                itemView.context, item.appPackageName
            )
            totalAppUsageTime.text = DateUtils.formatElapsedTime(item.totalTimeUsed / 1000)
        }

    }

}

interface ItemClickListener {
    fun onItemClicked(appPackageName: String)
}
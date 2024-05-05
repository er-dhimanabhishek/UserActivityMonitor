package com.erabhidman.useractivitymonitor.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erabhidman.useractivitymonitor.R
import com.erabhidman.useractivitymonitor.model.AppUsageTotalTimeEntity
import com.erabhidman.useractivitymonitor.utils.AppInfoUtils

class AppUsageListAdapter(val clickListener: ItemClickListener) :
    ListAdapter<AppUsageTotalTimeEntity, AppUsageListAdapter.AppUsageViewHolder>(MyDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppUsageViewHolder {
        return AppUsageViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(
                R.layout.app_usage_list_item, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AppUsageViewHolder, position: Int) {
        holder.bindAppUsageData(getItem(position))
        holder.itemView.setOnClickListener {
            clickListener.onItemClicked(getItem(position).appPackageName)
        }
    }

    class AppUsageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val appIcon = view.findViewById<ImageView>(R.id.ivAppIcon)
        private val appName = view.findViewById<TextView>(R.id.tvAppName)
        private val totalAppUsageTime = view.findViewById<TextView>(R.id.tvTotalAppUsageTime)

        fun bindAppUsageData(item: AppUsageTotalTimeEntity){
            appIcon.setImageDrawable(AppInfoUtils.getAppIconFromPackageName(itemView.context, item.appPackageName))
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

    class MyDiffUtil: DiffUtil.ItemCallback<AppUsageTotalTimeEntity>(){
        override fun areItemsTheSame(oldItem: AppUsageTotalTimeEntity, newItem: AppUsageTotalTimeEntity): Boolean {
            return oldItem.totalTimeUsed == newItem.totalTimeUsed
        }

        override fun areContentsTheSame(oldItem: AppUsageTotalTimeEntity, newItem: AppUsageTotalTimeEntity): Boolean {
            return oldItem == newItem
        }

    }

}

interface ItemClickListener {
    fun onItemClicked(appPackageName: String)
}
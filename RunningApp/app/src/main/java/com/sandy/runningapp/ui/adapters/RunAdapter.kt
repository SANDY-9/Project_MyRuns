package com.sandy.runningapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sandy.runningapp.R
import com.sandy.runningapp.db.Run
import com.sandy.runningapp.other.TrackingUtility
import kotlinx.android.synthetic.main.item_run.view.*
import kotlinx.android.synthetic.main.item_run.view.tvAvgSpeed
import kotlinx.android.synthetic.main.item_run.view.tvDate
import kotlinx.android.synthetic.main.item_run.view.tvDistance
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author SANDY
 * @email nnal0256@naver.com
 * @created 2021-10-08
 * @desc
 */
class RunAdapter : RecyclerView.Adapter<RunAdapter.RunViewHolder>() {

    inner class RunViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffCallback = object : DiffUtil.ItemCallback<Run>() {
        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
           return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Run>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        return RunViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_run,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val run = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(run.img).into(ivRunImage)
            ivRunImage.setOnClickListener {
                ivRunImageOrignal.visibility = if(ivRunImageOrignal.visibility == GONE) VISIBLE else GONE
            }

            Glide.with(this).load(run.img).into(ivRunImageOrignal)
            ivRunImageOrignal.setOnClickListener {
                ivRunImageOrignal.visibility = if(ivRunImageOrignal.visibility == GONE) VISIBLE else GONE
            }

            val calendar = Calendar.getInstance().apply {
                timeInMillis = run.timesTamp
            }
            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            tvDate.text = dateFormat.format(calendar.time)

            val avgSpeed = "${run.avgSpeedInKMH}km/h"
            tvAvgSpeed.text = avgSpeed

            val distanceInKm = run.distanceInMeters / 1000f
            tvDistance.text = String.format("%.2fkm", distanceInKm)

            tvTime.text = TrackingUtility.getFormattedStopWatchTime(run.timeInMillis)

            val caloriesBurned = "${run.caloriesBurned}kcal"
            tvCalories.text = caloriesBurned
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}
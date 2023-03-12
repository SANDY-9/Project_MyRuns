package com.sandy.runningapp.ui.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.sandy.runningapp.R
import com.sandy.runningapp.other.CustomMarkerView
import com.sandy.runningapp.other.TrackingUtility
import com.sandy.runningapp.ui.viewModels.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_statistics.*
import kotlin.math.round

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private val viewModel : StatisticsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        setupBarChart()
    }

    private fun setupBarChart() {
        with(barChart){
            xAxis.apply {
                setDrawLabels(false)
                setDrawGridLines(false)
                setDrawAxisLine(false)
                position = XAxis.XAxisPosition.BOTTOM
            }
            axisLeft.apply {
                setDrawLabels(false)
                setDrawAxisLine(false)
                setDrawGridLines(false)
            }
            axisRight.apply {
                setDrawLabels(false)
                setDrawAxisLine(false)
                setDrawGridLines(false)
            }
            description.isEnabled = false
            legend.isEnabled = true
        }
    }

    private fun subscribeToObservers() {
        val darkGreen = ContextCompat.getColor(requireContext(), R.color.dark_green)
        with(viewModel) {
            totalTimeRun.observe(viewLifecycleOwner, {
                it?.let {
                    val totalTimeRun = TrackingUtility.getFormattedStopWatchTime(it)
                    tvTotalTime.text = totalTimeRun
                }
            })
            totalDistance.observe(viewLifecycleOwner, {
                it?.let {
                    val km = it / 1000f
                    val totalDistance = round(km * 10f) / 10f
                    tvTotalDistance.text = "${totalDistance}km"
                }
            })
            totalAvgSpeed.observe(viewLifecycleOwner, {
                it?.let {
                    val avgSpeed = round(it * 10f) / 10f
                    tvAverageSpeed.text = "${avgSpeed}km/h"
                }
            })
            totalCaloriesBurned.observe(viewLifecycleOwner, {
                it?.let {
                    tvTotalCalories.text = "${it}kcal"
                }
            })
            runsSortedByDate.observe(viewLifecycleOwner, {
                it?.let {
                    val caloriesBurned = it.indices.map { i -> BarEntry(i.toFloat(), it[i].caloriesBurned.toFloat()) }
                    val barDataSet = BarDataSet(caloriesBurned, "CaloriesBurned (kcal)").apply {
                        valueTextColor = darkGreen
                        color = darkGreen
                        valueTextSize = 10f
                    }
                    barChart.apply {
                        data = BarData(barDataSet)
                        marker = CustomMarkerView(it.reversed(), requireContext(), R.layout.marker_view)
                        invalidate()
                    }
                }
            })
        }
    }

}

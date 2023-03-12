package com.sandy.runningapp.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.sandy.runningapp.db.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author SANDY
 * @email nnal0256@naver.com
 * @created 2021-09-06
 * @desc
 */

@HiltViewModel
class StatisticsViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    val totalTimeRun = mainRepository.getTotalTimeInMillis()
    val totalDistance = mainRepository.getTotalDistance()
    val totalCaloriesBurned = mainRepository.getTotalCaloriesBurned()
    val totalAvgSpeed = mainRepository.getTotalAvgSpeed()

    val runsSortedByDate = mainRepository.getAllRunsSortedByDate()

    init {
        Log.e("뭐가 문제인겨", "시부랄")
    }

}
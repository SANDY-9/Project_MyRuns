package com.sandy.runningapp.ui.viewModels

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sandy.runningapp.db.Run
import com.sandy.runningapp.db.repositories.MainRepository
import com.sandy.runningapp.other.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author SANDY
 * @email nnal0256@naver.com
 * @created 2021-09-06
 * @desc
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    private val runsSortedByDate = mainRepository.getAllRunsSortedByDate()
    private val runsSortedByDistance = mainRepository.getAllRunsSortedByDistance()
    private val runsSortedByCaloriesBurned = mainRepository.getAllRunsSortedByCaloriesBurned()
    private val runsSortedByTimeInMillis = mainRepository.getAllRunsSortedByTimeInMillis()
    private val runsSortedByAvgSpeed = mainRepository.getAllRunsSortedByAvgSpeed()

    val runs = MediatorLiveData<List<Run>>()

    var sortType = SortType.DATE

    init {
        Log.e("[확인]", "얘도 설마")
        runs.addSource(runsSortedByDate) {
            if(sortType == SortType.DATE) {
                it?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByAvgSpeed) {
            if(sortType == SortType.AVG_SPEED) {
                it?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByCaloriesBurned) {
            if(sortType == SortType.CALORIES_BURNED) {
                it?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByDistance) {
            if(sortType == SortType.DISTANCE) {
                it?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByTimeInMillis) {
            if(sortType == SortType.RUNNING_TIME) {
                it?.let { runs.value = it }
            }
        }
    }

    fun sortRuns(sortType: SortType) = when(sortType) {
        SortType.DATE -> runsSortedByDate.value?.let { runs.value = it }
        SortType.RUNNING_TIME -> runsSortedByTimeInMillis.value?.let { runs.value = it }
        SortType.AVG_SPEED -> runsSortedByAvgSpeed.value?.let { runs.value = it }
        SortType.DISTANCE -> runsSortedByDistance.value?.let { runs.value = it }
        SortType.CALORIES_BURNED -> runsSortedByCaloriesBurned.value?.let { runs.value = it }
    }.also {
        this.sortType = sortType
    }

    fun insertRun(run: Run) = viewModelScope.launch {
        mainRepository.insertRun(run)
    }
}
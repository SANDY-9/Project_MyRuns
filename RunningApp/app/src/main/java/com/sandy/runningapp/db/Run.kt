package com.sandy.runningapp.db

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author SANDY
 * @email nnal0256@naver.com
 * @created 2021-08-23
 * @desc
 */

@Entity(tableName = "running_table")
data class Run (
    var img : Bitmap? = null,
    var timesTamp : Long = 0L,
    var avgSpeedInKMH : Float = 0f,
    var distanceInMeters : Int = 0,
    var timeInMillis : Long = 0L,
    var caloriesBurned : Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
}
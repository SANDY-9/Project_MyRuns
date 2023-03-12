package com.sandy.runningapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * @author SANDY
 * @email nnal0256@naver.com
 * @created 2021-08-23
 * @desc
 */

@Database(
    entities = [Run::class],
    version = 3
)
@TypeConverters(Converters::class)
abstract class RunningDatabase : RoomDatabase() {

    abstract fun getRunDao() : RunDAO

}
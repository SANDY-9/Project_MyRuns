package com.sandy.runningapp.other.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.sandy.runningapp.db.RunningDatabase
import com.sandy.runningapp.other.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author SANDY
 * @email nnal0256@naver.com
 * @created 2021-08-23
 * @desc
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRunningDatabase(@ApplicationContext app: Context) = Room.databaseBuilder(
        app,
        RunningDatabase::class.java,
        RUNNING_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideRunDao(db: RunningDatabase) = db.getRunDao()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context) =
        app.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideName(sharedPrefs: SharedPreferences) =
        sharedPrefs.getString(KEY_NAME, "") ?: ""

    @Singleton
    @Provides
    fun provideWeight(sharedPrefs: SharedPreferences) =
        sharedPrefs.getFloat(KEY_WEIGHT, 60f)

    @Singleton
    @Provides
    fun provideFirstTimeToggle(sharedPrefs: SharedPreferences) =
        sharedPrefs.getBoolean(KEY_FIRST_TIME_TOGGLE, true)

}
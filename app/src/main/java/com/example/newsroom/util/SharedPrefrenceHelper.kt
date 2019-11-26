package com.example.newsroom.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
//Shared prefrence class to store the value in app memory
class SharedPrefrenceHelper  {
    companion object{
        private const val PREF_TIME="pref_time"
        private var prefs: SharedPreferences?=null

        @Volatile private var instance: SharedPrefrenceHelper?=null
        private val LOCK=Any()

        operator fun invoke(context: Context): SharedPrefrenceHelper {
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPrefrenceHelper()
        }

    }

    fun saveUpdateTime(time:Long){
        prefs?.edit(commit=true){putLong(PREF_TIME,time)}
    }

    fun getUpdateTime()= prefs?.getLong(PREF_TIME,0)

    fun getCacheDuration()= prefs?.getString("pref_cache_duration","")

    fun getSelectedCountry()= prefs?.getString("country","in")

    fun getSelectedCategory()= prefs?.getString("category","general")


}
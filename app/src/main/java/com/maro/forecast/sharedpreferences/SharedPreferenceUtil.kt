package com.maro.forecast.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceUtil(
    context: Context
) {

    private val prefs: SharedPreferences = context.getSharedPreferences("lab_prefs", Context.MODE_PRIVATE)

    fun getString(key: String): String? {
        return prefs.getString(key, null)
    }


    fun setString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }
}
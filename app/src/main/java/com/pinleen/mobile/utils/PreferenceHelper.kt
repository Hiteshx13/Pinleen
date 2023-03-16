package com.pinleen.mobile.utils

import android.content.Context
import com.pinleen.mobile.R

object PreferenceHelper {

    fun setString(context: Context, key: String, value: String) {
        val sharedPreference =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString(key, value)
        editor.commit()
    }

    fun setBoolean(context: Context, key: String, value: Boolean) {
        val sharedPreference =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putBoolean(key, value)
        editor.commit()
    }

    fun getString(context: Context, key: String): String {
        val sharedPreference =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        return sharedPreference.getString(key, "") ?: ""
    }

    fun getBoolean(context: Context, key: String): Boolean {
        val sharedPreference =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        return sharedPreference.getBoolean(key, false)
    }
}
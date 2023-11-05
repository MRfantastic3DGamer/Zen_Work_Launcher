package com.zenworklauncher.preffsDatabase

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager private constructor(context: Context) {

    private val sharedPreferencesCache: MutableMap<String, String> = mutableMapOf()

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var instance: PreferencesManager? = null
        fun getInstance(context: Context): PreferencesManager {
            return instance ?: synchronized(this) {
                instance ?: PreferencesManager(context).also { instance = it }
            }
        }
    }

    fun saveData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getData(context: Context, key: String, defaultValue: String): String {
        // Check the cache first
        if (sharedPreferencesCache.containsKey(key)) {
            return sharedPreferencesCache[key] ?: defaultValue
        }

        val storedValue = sharedPreferences.getString(key, defaultValue) ?: defaultValue

        sharedPreferencesCache[key] = storedValue

        return storedValue
    }

    fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
}
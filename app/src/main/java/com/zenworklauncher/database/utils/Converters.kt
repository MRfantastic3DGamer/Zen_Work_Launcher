package com.zenworklauncher.database.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object Converters {
    private val gson = Gson()

    fun stringsListToString(list: List<String>): String {
        return gson.toJson(list)
    }

    fun stringToStringsList(string: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(string, type)
    }

    fun usageMapToString(list: Map<String,Int>): String{
        return gson.toJson(list)
    }

    fun stringToUsageMap(string: String): Map<String,Int> {
        val type = object : TypeToken<Map<String,Int>>() {}.type
        return gson.fromJson(string, type)
    }
}
package com.zenworklauncher.database.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object Converters {
    private val gson = Gson()

    fun listToString(list: List<String>): String {
        return gson.toJson(list)
    }

    fun stringToList(string: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(string, type)
    }
}
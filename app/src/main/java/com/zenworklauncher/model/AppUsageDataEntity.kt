package com.zenworklauncher.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @param usageArray -> json array of how many times it was used in that time span
 */
@Entity(tableName = "app_usage")
data class AppUsageDataEntity(
    @PrimaryKey
    var `package`: String,
    val group: String = "",
    val usageArray: String = "",
    val hidden: Boolean = false
)

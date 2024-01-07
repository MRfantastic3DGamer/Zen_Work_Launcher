package com.zenworklauncher.model

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @param usageArray -> json array of how many times it was used in that time span
 */
@Immutable
@Entity(tableName = "app_usage")
data class AppUsageDataEntity(
    @PrimaryKey
    val `package`: String,
    val name: String,
    val group: String = "",
    val usageArray: String = "",
    val hidden: Boolean = false
)

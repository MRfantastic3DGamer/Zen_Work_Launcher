package com.zenworklauncher.model

import javax.annotation.concurrent.Immutable

@Immutable
data class GroupsDataResult(
    val resultState             : DatabaseState,
    val allGroups               : List<GroupDataEntity>,
)

@Immutable
data class AppsDataResult(
    val resultState             : DatabaseState,
    val allAppsData              : List<AppData>,
    val packageNameToAppDataMap : Map<String, Int>,
    val firstLetterToAppDataMap : Map<String, List<Int>>,
    val groupNameToAppDataMap   : Map<String, List<Int>>,
)

@Immutable
data class AppsUsageDataResult(
    val resultState             : DatabaseState,
    val allAppsUsageData        : List<AppUsageDataEntity>,
    val packageToAppUsageDataMap: Map<String, Int>
)
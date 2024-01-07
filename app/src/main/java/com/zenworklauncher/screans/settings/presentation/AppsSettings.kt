package com.zenworklauncher.screans.settings.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.zenworklauncher.database.rooms_database.DatabaseProvider
import com.zenworklauncher.screans.settings.AppsSettingsState
import com.zenworklauncher.screans.settings.presentation.components.AppUsageDataButton

@Composable
fun AppsSettings (
    modifier: Modifier = Modifier,
    state: AppsSettingsState
){
    val context = LocalContext.current
    LazyColumn (
        modifier,
    ){
        items(
            state.allAppsUsageData.count(),
            key = {it}
        ){ i ->
            val data = state.allAppsUsageData[i]
            AppUsageDataButton(
                Modifier.fillMaxWidth(),
                data = data,
                icon = state.icons[i],
                update = { DatabaseProvider.AddAppUsageData(context, it) },
                groups = state.allGroups
            )
        }
    }
}
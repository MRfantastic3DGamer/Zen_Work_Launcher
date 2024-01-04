package com.zenworklauncher.screans.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.zenworklauncher.model.AppData

@Composable
fun IconsGrid (
    modifier: Modifier = Modifier,
    numberOfColumns: Int,
    apps: List<AppData>,
    iconSize: Float,
){
    val context = LocalContext.current
    println(apps)
    LazyVerticalGrid(
        modifier = modifier
            .background(Color.Black)
            .padding(bottom = 50.dp),
        columns = GridCells.Fixed(numberOfColumns),
        contentPadding = PaddingValues(10.dp),
        reverseLayout = true,
    ){
        items(apps){ app ->
            IconButton(
                modifier = Modifier.padding(horizontal = 0.dp, vertical = 10.dp),
                onClick = { context.startActivity(app.launchIntent) }
            ) {
                AppIcon(action = app, iconSize = iconSize)
            }
        }
    }
}
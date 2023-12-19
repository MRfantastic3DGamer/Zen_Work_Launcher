package com.zenworklauncher.screans.home.widgetssection

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zenworklauncher.screans.home.model.WidgetsTabData

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WidgetsSection (
    modifier: Modifier = Modifier,
    appWidgetHost: AppWidgetHost,
    appWidgetManager: AppWidgetManager,
    tabsData: List<WidgetsTabData>,
    width: Float
){
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(initialPage = 0) { tabsData.size }
    LaunchedEffect(selectedTabIndex){
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage){
        if (!pagerState.isScrollInProgress)
            selectedTabIndex = pagerState.currentPage
    }
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(width.dp)
            .background(Color.Transparent)
        // handle positioning and hiding widgets section
    ){
        Column (
            Modifier
                .fillMaxSize()
        ){
            HorizontalPager(state = pagerState) { i->
                LazyColumn(content = {
                    items(tabsData[i].ids){
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color.Red))
                        Box(modifier = Modifier.height(10.dp))
                    }
                    item {
                        Box(
                            Modifier
                                .height(500.dp)
                                .fillMaxWidth()
                        )
                    }
                })
            }
            Row (
                Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            ){
                IconButton(onClick = { selectedTabIndex = 0 }) {
                    Icon(imageVector = Icons.Filled.Home, contentDescription = "tab 1 button")
                }
                IconButton(onClick = { selectedTabIndex = 1 }) {
                    Icon(imageVector = Icons.Filled.Home, contentDescription = "tab 2 button")
                }
            }
        }
    }
}
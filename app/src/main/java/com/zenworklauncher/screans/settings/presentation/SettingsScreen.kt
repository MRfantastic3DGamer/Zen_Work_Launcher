package com.zenworklauncher.screans.settings.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.zenworklauncher.preffsDatabase.SettingsValues
import com.zenworklauncher.screans.settings.SettingsViewModel
import com.zenworklauncher.screans.settings.presentation.components.AppsViewSettings
import com.zenworklauncher.screans.settings.presentation.components.FoldersSettings
import com.zenworklauncher.screans.settings.presentation.components.MainSettings


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel,
    backFunction: ()->Unit,
) {

    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(SettingsValues.SettingsTabType.Main) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0, 0, 0, 139))
    ) {

        @Composable
        fun tabButton(
            text: String,
            onTap: ()->Unit
        ){
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100))
                    .clickable { onTap() }
            ) {
                Text(
                    text = text,
                    modifier = Modifier
                        .vertical(true)
                        .rotate(-90f)
                        .padding(20.dp),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = TextUnit(20f, TextUnitType.Sp),
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }
        }

        @Composable
        fun Preview(){

            viewModel.UpdateValues()

            @Composable
            fun LabelTextPreview(){
                Box(modifier = Modifier.fillMaxSize())
                {
                    Box(modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxHeight()
                        .padding(0.dp, 100.dp, 0.dp, 0.dp))
                    {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "A",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = TextUnit(viewModel.labelSize, TextUnitType.Sp)
                            )
                        )
                    }
                }
            }

            @Composable
            fun LabelBGPreview(){
                Box(modifier = Modifier.fillMaxSize())
                {
                    Box(modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(
                            SettingsValues.getDp(SettingsValues.AppsView.AppsViewKeys.labelWidth),
                            SettingsValues.getDp(SettingsValues.AppsView.AppsViewKeys.height)
                        )
                    )
                }
            }

            @Composable
            fun IconPreview(selected: Boolean){
                Box(modifier = Modifier.fillMaxSize())
                {
                    Box(modifier = Modifier
                        .align(Alignment.TopCenter)
                        .clip(CircleShape)
                        .size(viewModel.iconSize)
                        .offset { IntOffset(0, 700 + viewModel.iconSize.value.toInt()) }
                        .background(Color.Gray)
                        .border(
                            width =
                            if (selected) viewModel.iconBorderSize
                            else viewModel.selectedIconBorderSize,
                            Color.White,
                            shape = CircleShape,
                        )
                    )
                }
            }

            @Composable
            fun IconsSectionPreview(){

                Box(modifier = Modifier.fillMaxSize()){
                    Row(
                        Modifier
                            .height(viewModel.height)
                            .fillMaxWidth(),
                        Arrangement.End
                    ) {
                        Box(
                            Modifier
                                .fillMaxHeight()
                                .width(viewModel.width)
                                .background(Color.DarkGray))
                        Box(
                            Modifier
                                .fillMaxHeight()
                                .width(viewModel.labelWidth)
                                .background(Color.Gray))
                    }
                }
            }

            @Composable
            fun AppNamePreview(){

            }

//            if(viewModel.currentlyBeingEdited.value != null){
//                Box(modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.Black))
//            }

            when (viewModel.currentlyBeingEdited.value) {
                SettingsValues.AppsView.AppsViewKeys.labelSize -> LabelTextPreview()
                SettingsValues.AppsView.AppsViewKeys.labelWidth -> IconsSectionPreview()
                SettingsValues.AppsView.AppsViewKeys.labelBGBlurValue -> LabelBGPreview()
                SettingsValues.AppsView.AppsViewKeys.labelBGTint -> LabelBGPreview()
                SettingsValues.AppsView.AppsViewKeys.height -> IconsSectionPreview()
                SettingsValues.AppsView.AppsViewKeys.width -> IconsSectionPreview()
                SettingsValues.AppsView.AppsViewKeys.iconSize -> IconPreview(selected = false)
                SettingsValues.AppsView.AppsViewKeys.iconSeparation -> IconsSectionPreview()
                SettingsValues.AppsView.AppsViewKeys.iconRowSeparation -> IconsSectionPreview()
                SettingsValues.AppsView.AppsViewKeys.iconBGBlurValue -> IconsSectionPreview()
                SettingsValues.AppsView.AppsViewKeys.iconBGTint -> IconsSectionPreview()
                SettingsValues.AppsView.AppsViewKeys.iconBorderSize -> IconPreview(selected = false)
                SettingsValues.AppsView.AppsViewKeys.selectedIconBorderSize -> IconPreview(selected = true)
                SettingsValues.AppsView.AppsViewKeys.appNameBottumPadding -> AppNamePreview()
                null -> {}
            }
        }

//        Preview()

        Row(
            Modifier.fillMaxSize()
        ) {
            NavigationRail(
                modifier = Modifier.width(50.dp),
                containerColor = Color.Transparent,
                contentColor = Color.Black,
                windowInsets = WindowInsets(0,0,0,0),
                header = {
                    Icon(imageVector = Icons.Rounded.KeyboardArrowLeft, contentDescription = "back")
                },
                content = {
                    tabButton(text = "Main") {
                        selectedTab = SettingsValues.SettingsTabType.Main
                    }
                    tabButton(text = "Apps View"){
                        selectedTab = SettingsValues.SettingsTabType.AppsView
                    }
                    tabButton(text = "Folders") {
                        selectedTab = SettingsValues.SettingsTabType.Folders
                    }
                }
            )
            AnimatedContent(
                targetState = selectedTab,
                label = "settings",
                transitionSpec = {
                    slideIntoContainer(
                        animationSpec = tween(100, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Up
                    ).with(
                        slideOutOfContainer(
                            animationSpec = tween(100, easing = EaseOut),
                            towards = AnimatedContentTransitionScope.SlideDirection.Up
                        )
                    )
                }
            ) {
                targetState ->
                when (targetState) {
                    SettingsValues.SettingsTabType.Main -> Box (Modifier.fillMaxSize()){
                        MainSettings(viewModel)
                    }
                    SettingsValues.SettingsTabType.AppsView -> Box (Modifier.fillMaxSize()){
                        AppsViewSettings(viewModel)
                    }
                    SettingsValues.SettingsTabType.Folders -> Box (Modifier.fillMaxSize()){
                        FoldersSettings(viewModel.foldersPageState)
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = {
                backFunction()
                viewModel.save(context)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset { IntOffset(-20, -20) },
        ) {
            Text(text = "Save")
        }
    }
}


fun Modifier.vertical(enabled: Boolean = true) =
    if (enabled)
        layout { measurable, constraints ->
            val placeable = measurable.measure(constraints.copy(maxWidth = Int.MAX_VALUE))
            layout(placeable.height, placeable.width) {
                placeable.place(
                    x = -(placeable.width / 2 - placeable.height / 2),
                    y = -(placeable.height / 2 - placeable.width / 2)
                )
            }
        } else this
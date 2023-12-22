package com.zenworklauncher.screans.home.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.toOffset
import com.dhruv.quick_apps.QuickAppsVisual
import com.dhruv.quick_apps.SelectionMode
import com.dhruv.radial_quick_actions.model.QuickAction
import com.zenworklauncher.R
import com.zenworklauncher.preffsDatabase.SettingsValues
import com.zenworklauncher.screans.home.HomeViewModel
import com.zenworklauncher.screans.home.presentation.components.AppIcon
import com.zenworklauncher.screans.home.presentation.components.Background
import com.zenworklauncher.screans.home.presentation.components.BlurredBG
import com.zenworklauncher.screans.home.presentation.components.IconsGrid
import com.zenworklauncher.screans.home.presentation.components.PathsCalculation
import com.zenworklauncher.screans.home.presentation.components.QuickAppsTriggerPositioning
import com.zenworklauncher.screans.home.presentation.components.SelectedAppLabel
import com.zenworklauncher.screans.home.presentation.components.TapHighLite
import com.zenworklauncher.utils.Outline
import com.zenworklauncher.utils.PathToShape

class HomeQuickAction(name: String, onSelect: ()->Unit ) : QuickAction(name, onSelect)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
) {
    val haptic                          = LocalHapticFeedback.current
    val searchFieldFocusRequester       = remember { FocusRequester() }
    val selectionMode                   = viewModel.quickAppsViewModel.selectionMode
    val currentAction                   = viewModel.quickAppsViewModel.currentAction
    val keyboardController              = LocalSoftwareKeyboardController.current

    val selectedAlphabetYOffset by animateFloatAsState(
        targetValue = viewModel.quickAppsViewModel.getSelectedTriggerYOffset,
        label = "selected-string-Y-offset"
    )
    val cursorOffset by animateOffsetAsState(
        targetValue = viewModel.quickAppsViewModel.getGlobalResponsiveBubblePosition(SettingsValues.getFloat(SettingsValues.AppsView.AppsViewKeys.iconSize)*0.75f),
        label = "selected-action-offset"
    )
    var currentFocusValueTarget by remember { mutableFloatStateOf(0f) }
    val cursorFocus by animateFloatAsState(targetValue = currentFocusValueTarget, label = "cursor-focus")
    LaunchedEffect(selectionMode, currentAction){
        currentFocusValueTarget = when (selectionMode) {
            SelectionMode.NonActive -> 0f
            SelectionMode.TriggerGestureSelect -> 1f
            SelectionMode.AppGestureSelect -> if (currentAction == null) 0f else 1f
            else -> 0f
        }
    }
    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics
    val densityFactor =  displayMetrics.density

    val paths by remember(selectedAlphabetYOffset) {
        mutableStateOf(
            PathsCalculation(
                triggerOffset = viewModel.quickAppsViewModel.getTriggerOffset.toOffset(),
                sidePadding = 10f,
                selectionOffsetY = selectedAlphabetYOffset,
                triggerSize = viewModel.quickAppsViewModel.getTriggerSize,
                radius_1 = viewModel.quickAppsViewModel.startingRowHeight.toFloat() - 80,
                radius_3 = viewModel.quickAppsViewModel.getAlphabetAppsMaxRadius,
                isTriggerActive = selectionMode != SelectionMode.NonActive
            )
        )
    }


    viewModel.quickAppsViewModel.setHapticFeedback(haptic)

    @Composable
    fun triggerSide(size: IntSize, offset: IntOffset){
        val path = paths[0]
        val shape = PathToShape(path)

        Box(modifier = Modifier
            .fillMaxSize()
            .clip(shape)
        )
        {
            BlurredBG(SettingsValues.getDp(SettingsValues.AppsView.AppsViewKeys.labelBGBlurValue), R.drawable.wallpaper, SettingsValues.AppsView.AppsViewKeys.labelBGTint)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(1f, 1f, 1f, 0.03f),
                                Color(1f, 1f, 1f, 0.1f)
                            ),
                            start = offset.toOffset() + Offset(
                                0f,
                                size.height.toFloat()
                            ),
                            end = offset.toOffset() + Offset(size.width.toFloat(), 0f)
                        )
                    )
            )

            TapHighLite(cursorOffset, cursorFocus)

            Outline(path, 5f)
        }
    }

    @Composable
    fun iconsSide(size: IntSize, offset: IntOffset, outlineWidth: Float){

        val iconsBorderPath = paths[1]
        val iconsShape = PathToShape(iconsBorderPath)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(iconsShape),
        ){
            BlurredBG(SettingsValues.getDp(SettingsValues.AppsView.AppsViewKeys.iconBGBlurValue), R.drawable.wallpaper, SettingsValues.AppsView.AppsViewKeys.iconBGTint)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(1f, 1f, 1f, 0.03f),
                                Color(1f, 1f, 1f, 0.1f)
                            ),
                            start = Offset(0f, offset.y + size.height.toFloat()),
                            end = offset.toOffset()
                        )
                    )
            )
            TapHighLite(cursorOffset, cursorFocus)
        }
        Outline(iconsBorderPath, outlineWidth)
    }

    @Composable
    fun searchButton (modifier: Modifier = Modifier, focusRequester: FocusRequester, visibility: Boolean) {
        val keyboardActions = KeyboardActions (onAny = {
            keyboardController?.hide()
        })
        TextField(
            modifier = modifier
                .onFocusChanged { state ->
                    if (state.isFocused) {
                        viewModel.searching = true
                    }
                }
                .focusRequester(focusRequester = focusRequester)
                .visibleHeight(visibility),
            value = viewModel.appSearchQuery,
            shape = RoundedCornerShape(500f),
            singleLine = true,
            keyboardActions = keyboardActions,
            onValueChange = {s -> viewModel.appSearch(s) }
        )
    }

    Box(
        modifier = modifier
    ) {
        val iconSize = SettingsValues.getFloat(SettingsValues.AppsView.AppsViewKeys.iconSize)
        val iconSizeOffset = Offset(-iconSize/2,-iconSize/2).round()

        Background()
//        AnimatedVisibility(
//            modifier = Modifier,
//            visible = selectionMode == SelectionMode.NonActive,
//            label = "widgets section"
//        ) {
//            WidgetsSection(
//                Modifier,
//                appWidgetManager = viewModel.appWidgetManager,
//                appWidgetHost = viewModel.appWidgetHost,
//                tabsData = listOf(
//                    WidgetsTabData(name = "T1",unselectedIcon= Icons.Outlined.Home, selectedIcon = Icons.Filled.Home, ids = listOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)),
//                    WidgetsTabData(name = "T2",unselectedIcon= Icons.Outlined.Home, selectedIcon = Icons.Filled.Home, ids = listOf())
//                ),
//                width = (viewModel.quickAppsViewModel.getTriggerOffset.x / densityFactor) - 20
//            )
//        }
        AnimatedVisibility(
            visible = !viewModel.searching,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            QuickAppsVisual(
                modifier = Modifier.fillMaxSize(),
                viewModel = viewModel.quickAppsViewModel,
                alphabetSideFloat = 100F,
                labelSize = SettingsValues.AppsView.savedData.cacheValues[SettingsValues.AppsView.AppsViewKeys.labelSize]?.toFloat() ?: 10f,
                appComposable = { action, offset, selected -> AppIcon(action, offset, selected, iconSizeOffset, iconSize) },
                triggerBGComposable = { offset, size, _ ->
                    triggerSide(size, offset)
                    iconsSide(size, offset, 5f)
                },
                iconsBGComposable = { offset, size, _ ->
                    triggerSide(size, offset)
                    iconsSide(size, offset, 5f)
                },
                allActions = viewModel.allAppsData
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            Arrangement.Bottom,
        ) {
            SelectedAppLabel(action = if (currentAction == null) null else viewModel.allAppsData[currentAction])
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                Arrangement.End,
                Alignment.Bottom
            ) {
                if(viewModel.searching) {
                    IconsGrid(
                        Modifier
                            .height(SettingsValues.getDp(SettingsValues.AppsView.AppsViewKeys.height))
                            .weight(1f),
                        numberOfColumns = 5,
                        apps = viewModel.searchedAppsData,
                        iconSize = 45f
                    )
                }
                else{
                    Box(
                        modifier =
                        Modifier
                            .height(SettingsValues.getDp(SettingsValues.AppsView.AppsViewKeys.height))
                            .weight(1f)
                            .clickable {
                                viewModel.startAppSearching()
                                searchFieldFocusRequester.captureFocus()
                                keyboardController?.show()
                            }
                    )
                }
                QuickAppsTriggerPositioning(
                    onTriggerGloballyPositioned = { viewModel.onTriggerPositioned(it) },
                    onDragStart = {
                        searchFieldFocusRequester.freeFocus()
                        viewModel.onDragStart(it)
                        keyboardController?.hide()
                    },
                    onDrag = {
                        viewModel.onDrag(it)
                    },
                    onDragStop = {
                        viewModel.onDragStop()
                    }
                )
            }
            searchButton(
                Modifier.fillMaxWidth(),
                focusRequester = searchFieldFocusRequester,
                visibility = viewModel.searching,
            )
        }
    }
}

fun Modifier.visibleHeight(visibility: Boolean): Modifier {
    return if (visibility) this else this then Modifier.height(0.dp).alpha(0f)
}
package com.zenworklauncher.widgets.screans.schedule.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import com.zenworklauncher.widgets.screans.schedule.model.Task

@Composable
fun TasksLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    tasks: List<Task>,
) {
    Layout(
        modifier = modifier,
        content = content,
        measurePolicy = {measurables, constraints ->
            val placeables = measurables.map {
                it.measure(constraints)
            }

            layout(
                width = constraints.maxWidth,
                height = constraints.maxHeight,
            ){
                var i = 0
                placeables.forEach { placeable ->
                    val t = tasks[i].alarmTime
                    val T = t.split(":")
                    val h = T[0].toInt()
                    val m = T[1].toInt()
                    println("$h:$m")
                    val posValue = h + (m/60)
                    placeable.place(
                        x = 0,
                        y = (constraints.maxHeight/24) * posValue
                    )
                    i++
                }
            }
        }
    )
}
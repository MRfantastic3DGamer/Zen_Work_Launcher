package com.zenworklauncher.widgets.schedule.presentation.components

import android.app.TimePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.zenworklauncher.widgets.schedule.model.Task
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEdit(
    modifier: Modifier = Modifier,
    visible: Boolean,
    res : (Task) -> Unit,
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var notificationTime by remember { mutableStateOf("") }
    var alarmTime by remember { mutableStateOf("") }

    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]
    val notificationTimePickerDialog = TimePickerDialog(
        context,
        {_, mHour : Int, mMinute: Int ->
            notificationTime = "$mHour:$mMinute"
        }, mHour, mMinute, false
    )
    val alarmTimePickerDialog = TimePickerDialog(
        context,
        {_, mHour : Int, mMinute: Int ->
            alarmTime = "$mHour:$mMinute"
        }, mHour, mMinute, false
    )

    AnimatedVisibility(
        visibleState = MutableTransitionState(visible),
        label = "task editor visibility") {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Box(
                modifier = Modifier
                    .size(400.dp, 300.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
            ){
                Column {
                    TextField(value = name, onValueChange = {name = it})
                    TextField(value = description, onValueChange = {description = it})
                    Button(onClick = { notificationTimePickerDialog.show() }) {
                        Text(text = "notification time")
                    }
                    Button(onClick = { alarmTimePickerDialog.show() }) {
                        Text(text = "alarm time")
                    }
                    Button(onClick = {
                        res(Task(
                            name = name,
                            description = description,
                            notificationTime = notificationTime,
                            alarmTime = alarmTime,
                            completed = false
                        ))
                        name = ""
                        description = ""
                        notificationTime = ""
                        alarmTime = ""
                    }) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}
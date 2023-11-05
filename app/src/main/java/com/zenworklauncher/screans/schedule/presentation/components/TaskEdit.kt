package com.zenworklauncher.screans.schedule.presentation.components

import android.app.TimePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.zenworklauncher.screans.schedule.ScheduleViewModel
import com.zenworklauncher.screans.schedule.model.Task
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditor(
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel,
) {
    val context = LocalContext.current

    AnimatedVisibility(
        visible = viewModel.tasksUiState.currentlyEditing,
        label = "task editor visibility"
    ) {

        val name = viewModel.tasksUiState.currentEditedTask.name
        val description = viewModel.tasksUiState.currentEditedTask.description
        val notificationTime = viewModel.tasksUiState.currentEditedTask.notificationTime
        val alarmTime = viewModel.tasksUiState.currentEditedTask.alarmTime

        val mCalendar = Calendar.getInstance()
        val mHour = mCalendar[Calendar.HOUR_OF_DAY]
        val mMinute = mCalendar[Calendar.MINUTE]
        val notificationTimePickerDialog = TimePickerDialog(
            context,
            {_, mHour : Int, mMinute: Int ->
                viewModel.tasksUiState = viewModel.tasksUiState.copy(
                    currentEditedTask = viewModel.tasksUiState.currentEditedTask.copy(
                        notificationTime = "$mHour:$mMinute"
                    )
                )
            }, mHour, mMinute, false
        )

        val alarmTimePickerDialog = TimePickerDialog(
            context,
            {_, mHour : Int, mMinute: Int ->
                viewModel.tasksUiState = viewModel.tasksUiState.copy(
                    currentEditedTask = viewModel.tasksUiState.currentEditedTask.copy(
                        alarmTime = "$mHour:$mMinute"
                    )
                )
            }, mHour, mMinute, false
        )

        Column(
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Black.copy(alpha = 0.1f)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = name,
                    onValueChange = {
                        viewModel.tasksUiState = viewModel.tasksUiState.copy(
                            currentEditedTask = viewModel.tasksUiState.currentEditedTask.copy(
                                name = it
                            )
                        ) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp)),
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "title") },
                    singleLine = true,
                    label = {
                        Text(
                            "Title"
                        )
                    }
                )
                TextField(
                    value = description,
                    onValueChange = {
                        viewModel.tasksUiState =
                            viewModel.tasksUiState.copy(
                                currentEditedTask = viewModel.tasksUiState.currentEditedTask.copy(
                                    description = it
                                )
                            ) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp)),
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "title") },
                    singleLine = true,
                    label = {
                        Text(
                            "Description"
                        )
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,) {
                        TextButton(onClick = { notificationTimePickerDialog.show() }) {
                            Text(text = "notification at $notificationTime")
                        }
                        Text(text = "->")
                        TextButton(onClick = { alarmTimePickerDialog.show() }) {
                            Text(text = "alarm at $alarmTime")
                        }
                    }
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (name.isBlank()) return@Button
                            if (alarmTime.isBlank()) return@Button
                            if (notificationTime.isBlank()) return@Button
                            viewModel.upsertTask(
                                Task(
                                    name = name,
                                    description = description,
                                    notificationTime = notificationTime,
                                    alarmTime = alarmTime,
                                    completed = false
                                )
                            )
                            viewModel.endEditingTask()

                        }) {
                        Text(text = "Save")
                    }
                }
            }
    }
}
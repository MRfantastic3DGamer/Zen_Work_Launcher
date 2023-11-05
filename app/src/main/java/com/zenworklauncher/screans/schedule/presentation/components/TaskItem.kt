package com.zenworklauncher.screans.schedule.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.zenworklauncher.screans.schedule.model.Task

@Composable
fun TaskItem(
    task: Task,
    expand: Boolean,
    onTap: ((Task)->Unit)?,
    onHold: ((Task)->Unit)?,
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStart = 1000.dp,
                    bottomStart = 1000.dp,
                    topEnd = 5.dp,
                    bottomEnd = 5.dp
                )
            )
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onTap?.invoke(task) },
                    onLongPress = { onHold?.invoke(task) }
                )
            }
    ){
        AnimatedVisibility(visible = !task.completed)
        {
            Column(
                Modifier.fillMaxWidth(),
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 5.dp),
                        text = task.notificationTime + " -> " + task.alarmTime,
                        color = Color.Gray,
                        fontWeight = FontWeight(400),
                        fontSize = TextUnit(15f, TextUnitType.Sp)
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 5.dp),
                        text = task.name,
                        color = Color.Black,
                        fontWeight = FontWeight(1000),
                        fontSize = TextUnit(17f, TextUnitType.Sp)
                    )
                }
                AnimatedVisibility(visible = expand) {
                    Column {
                        Spacer(modifier = Modifier.height(5.dp))
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 5.dp),
                                text = task.description,
                                color = Color.Black,
                                fontWeight = FontWeight(500),
                                fontSize = TextUnit(15f, TextUnitType.Sp)
                            )
                        }
                    }
                }
            }
        }
        AnimatedVisibility(visible = task.completed){
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(20.dp))
        }
    }
}


@Preview
@Composable
fun prev(){
    TaskItem(
        task = Task(1,"Take pills", "take em after food","5:30","6:30",false),
        expand = false,
        onTap = {

        },
        onHold = {},
    )
}
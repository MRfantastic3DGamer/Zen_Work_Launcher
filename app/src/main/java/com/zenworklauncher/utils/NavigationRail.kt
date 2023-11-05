package com.zenworklauncher.utils

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
inline fun NavigationRail(
    topIconButtonId: Int,
    noinline onTopIconButtonClick: () -> Unit,
    tabIndex: Int,
    crossinline onTabIndexChanged: (Int) -> Unit,
    content: List<String>,
    modifier: Modifier = Modifier
) {

    val width = 100.dp
    val height = 300.dp
    val offset = 100.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding()
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .size(
                    width = width,
                    height = height
                )
        ) {
            Image(
                painter = painterResource(topIconButtonId),
                contentDescription = null,
                modifier = Modifier
                    .offset(
                        x = 0.dp,
                        y = 48.dp
                    )
                    .clip(CircleShape)
                    .clickable(onClick = onTopIconButtonClick)
                    .padding(all = 12.dp)
                    .size(22.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(width)
        ) {
            val transition = updateTransition(targetState = tabIndex, label = null)

            content.forEachIndexed { index, text ->
                val dothAlpha by transition.animateFloat(label = "") {
                    if (it == index) 1f else 0f
                }

                val textColor by transition.animateColor(label = "") {
                    if (it == index) Color.Black else Color.Red
                }

//                val iconContent: @Composable () -> Unit = {
//                    Image(
//                        painter = painterResource(icon),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .vertical(enabled = true)
//                            .graphicsLayer {
//                                alpha = dothAlpha
//                                translationX = (1f - dothAlpha) * -48.dp.toPx()
//                                rotationZ = 0f
//                            }
//                            .size(offset * 2)
//                    )
//                }

                val textContent: @Composable () -> Unit = {
                    Text(
                        text = text,
                        modifier = Modifier
//                            .vertical(true)
                            .rotate(-90f)
//                            .padding(horizontal = 16.dp)
                    )
                }

                val contentModifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .clickable(onClick = { onTabIndexChanged(index) })

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = contentModifier
                        .padding(horizontal = 8.dp)
                ) {
//                    iconContent()
                    textContent()
                }
            }
        }
    }
}
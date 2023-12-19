package com.zenworklauncher.screans.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dhruv.radial_quick_actions.RadialQuickActionViewModel
import com.dhruv.radial_quick_actions.presentation.RadialQuickActionTrigger
import com.dhruv.radial_quick_actions.presentation.RadialQuickActionVisual

@Composable
fun RadialQuickActionPositioning(viewModel: RadialQuickActionViewModel){
    Column {
        Box {
            RadialQuickActionTrigger(
                modifier = Modifier
                    .size(50.dp, 300.dp),
                viewModel = viewModel
            )
            RadialQuickActionVisual(
                modifier = Modifier
                    .size(50.dp, 300.dp),
                viewModel = viewModel,
                actionItem = {m,action,selected ->
                    Box(
                        m
                            .clip(CircleShape)
                            .background(Color.White)
                    ){
                        Text(text = action.name)
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(200.dp))
    }
}
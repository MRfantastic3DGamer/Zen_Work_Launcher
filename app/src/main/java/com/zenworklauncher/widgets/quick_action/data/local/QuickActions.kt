package com.zenworklauncher.widgets.quick_action.data.local

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import com.zenworklauncher.widgets.quick_action.model.QuickAction

class QuickActions {

    fun getQuickActions(
        actionsType: QuickActionsType.Navigation,
        navAction: (QuickActionsType.Actions) -> Unit,
    ): List<QuickAction> {
        when (actionsType) {
            QuickActionsType.Navigation.Home -> {
                return emptyList()
            }
            QuickActionsType.Navigation.Schedule -> {
                return emptyList()
            }
        }
    }

    fun getQuickActions(
        actionsType: QuickActionsType.Actions,
        action: (QuickActionsType.Actions) -> Unit,
    ): List<QuickAction> {
        when (actionsType) {
            QuickActionsType.Actions.Home -> {
                return listOf(
//                    QuickAction(
//                        Icons.Rounded.Add,
//                        "Settings",
//                        onSelect = { action(QuickActionsType.Actions.Home.Settings) }
//                    ),
                )
            }
            QuickActionsType.Actions.Schedule -> {
                return listOf(
                    QuickAction(
                        Icons.Rounded.Add,
                        "add new task",
                        onSelect = { action(QuickActionsType.Actions.Schedule.AddNew) }
                    ),
                    QuickAction(
                        Icons.Rounded.Edit,
                        "Edit task",
                        onSelect = { action(QuickActionsType.Actions.Schedule.Edit) }
                    ),
                    QuickAction(
                        Icons.Rounded.Delete,
                        "delete tasks",
                        onSelect = { action(QuickActionsType.Actions.Schedule.Delete) }
                    ),
                )
            }
        }
    }
}


//QuickActionsType.Actions.Home -> {
//    return emptyList()
//}
//QuickActionsType.Actions.Schedule -> {
//    return listOf(
//        QuickAction(
//            Icons.Rounded.Add,
//            "add new task",
//            onSelect = { action(QuickActionsType.Actions.Schedule.Add) }
//        ),
//        QuickAction(
//            Icons.Rounded.Delete,
//            "delete tasks",
//            onSelect = { action(QuickActionsType.Actions.Schedule.Delete) }
//        ),
//    )
//}
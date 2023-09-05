package com.zenworklauncher.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.ViewModel
import com.models.App

class AppsEntryViewModel : ViewModel(){

    var appUiState by mutableStateOf(AppUiState())
        private set

    /**
     * Updates the [appUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(itemDetails: AppDetails) {
        appUiState =
            AppUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    private fun validateInput(uiState: AppDetails = appUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && packageName.isNotBlank() && (icon != null)
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class AppUiState(
    val itemDetails: AppDetails = AppDetails(),
    val isEntryValid: Boolean = false
)

data class AppDetails(
    val id: Int = 0,
    val name: String = "",
    val icon: Painter? = null,
    val packageName: String = "",
    val noOfTimesOpened: Int = 0,
)

/**
 * Extension function to convert [AppDetails] to [Item]. If the value of [AppDetails.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [AppDetails.quantity] is not a valid [Int], then the quantity will be set to 0
 */
fun AppDetails.toItem(): App = App(
    id = id,
    name = name,
    icon = icon as Painter,
    packageName = packageName,
    noOfTimesOpened = noOfTimesOpened,
)

/**
 * Extension function to convert [Item] to [AppUiState]
 */
fun App.toAppUiState(isEntryValid: Boolean = false): AppUiState = AppUiState(
    itemDetails = this.toAppDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [AppDetails]
 */
fun App.toAppDetails(): AppDetails = AppDetails(
    id = id,
    name = name,
    noOfTimesOpened = noOfTimesOpened,
    packageName = packageName,
    icon = icon
)

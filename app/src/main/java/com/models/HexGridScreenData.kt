package com.models

import com.example.launcher.models.AppButtonData
import com.example.launcher.models.FolderButtonData

data class HexGridScreenData(
    var apps         : MutableList<AppButtonData>,
    var folders      : MutableList<FolderButtonData>,
    var indexToAction: MutableMap<Int, HexAction>,
    var inputState   : InputState,
    var openedFolder : Int
)

data class HexAction(val appPackage: String = "", val folderIndex: FolderButtonData? = null)

enum class InputState{
    Default,
    HexSelected,
    HexHeld,
}
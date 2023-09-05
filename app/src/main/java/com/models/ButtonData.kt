package com.example.launcher.models

data class AppButtonData(var index: Int, var posX: Float, var posY: Float, var app: App)

data class FolderButtonData(var index: Int, var posX: Float, var posY: Float, var opened:Boolean, var apps : MutableList<App>, var folderName: String)

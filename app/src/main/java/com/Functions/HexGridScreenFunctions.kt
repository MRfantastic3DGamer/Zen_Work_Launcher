package com.example.launcher.Functions

import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import com.example.launcher.Drawing.DrawablePainter
import com.example.launcher.models.App
import com.example.launcher.models.AppButtonData
import com.example.launcher.models.FolderButtonData
import com.example.launcher.models.HexAction
import kotlin.math.floor
import kotlin.math.roundToInt


fun getAllAppsFromPackageManager(pm : PackageManager) : MutableList<App>{
    val main = Intent(Intent.ACTION_MAIN, null)
    main.addCategory(Intent.CATEGORY_LAUNCHER)
    val appsL = pm.queryIntentActivities(main, 0)

    val installedApps: MutableList<App> = ArrayList()

    for (app in appsL)
    {
        val saveApp = App(
            app.loadLabel(pm) as String,
            DrawablePainter(app.loadIcon(pm)),
            app.activityInfo.packageName
        )
        installedApps.add(saveApp)
    }
    installedApps.sortBy { it.name }

    return installedApps
}

fun getFoldersByPackageSimilarities(allAps : MutableList<App>): MutableMap<String, MutableList<App>> {
    val packageToApp : MutableMap<String, MutableList<App>> = mutableMapOf<String,MutableList<App>>()
    var packageName : String
    for (i in 0 until  allAps.size){
        packageName = allAps[i].packageName
        val parts = packageName.split('.')
        parts.forEach{part ->
            if(packageToApp.containsKey(part))
                packageToApp[part]?.add(allAps[i])
            else
                packageToApp[part] = MutableList(1){_-> allAps[i]}
        }
    }

    return packageToApp
}

fun getAllAppsFromMemory(){
    //TODO:
}

fun getFoldersFromMemory(){
    // TODO:
}

fun getAllFolderButtons(startingIndex: Int, folders: MutableMap<String, MutableList<App>>, separation: Int,buttonSize: Dp,rowSize: Int): MutableList<FolderButtonData> {
    val buttons : MutableList<FolderButtonData> = mutableListOf()
    var position: Array<Float>
    var i = startingIndex
    folders.forEach { folder ->
        if(folder.value.size in 4..18){
            position = indexToPosition(i, separation, buttonSize, rowSize)
            buttons.add(FolderButtonData(i,position[0],position[1],false,folder.value, folder.key))
            i++
        }
    }
    return buttons
}

fun getAllAppButtons(startingIndex:Int, apps: MutableList<App>, separation: Int,buttonSize: Dp,rowSize: Int): MutableList<AppButtonData> {
    val buttons : MutableList<AppButtonData> = mutableListOf()
    var position: Array<Float>
    var i = startingIndex
    apps.forEach { app ->
        position = indexToPosition(i,separation,buttonSize, rowSize)
        buttons.add(AppButtonData(i,position[0],position[1],app))
        i++
    }
    return buttons
}

fun getPositionActions(appButtons: MutableList<AppButtonData>, folders: MutableList<FolderButtonData>, rowSize: Int): MutableMap<Int, HexAction> {
    val offsets6Odd  = arrayOf(-rowSize*2,
                        -rowSize, -rowSize+1,
                        +rowSize, +rowSize+1,
                            +rowSize*2,
        )
    val offsets6Even  = arrayOf(-rowSize*2,
                        -rowSize-1, -rowSize,
                        +rowSize-1, +rowSize,
                            +rowSize*2,
    )
    val offsets18Odd = arrayOf(-rowSize*4,
                    -rowSize*3, -rowSize*3+1,
            -rowSize*2-1, -rowSize*2, -rowSize*2+1,
                      -rowSize, -rowSize+1,
        -1,                                                +1,
                      +rowSize, +rowSize+1,
            +rowSize*2-1, +rowSize*2, +rowSize*2+1,
                    +rowSize*3, +rowSize*3+1,
                           +rowSize*4
        )
    val offsets18Even = arrayOf(-rowSize*4,
                        -rowSize*3-1, -rowSize*3,
                -rowSize*2-1, -rowSize*2, -rowSize*2+1,
                            -rowSize-1, -rowSize,
        -1,                                                +1,
                            +rowSize-1, +rowSize,
                +rowSize*2-1, +rowSize*2, +rowSize*2+1,
                        +rowSize*3-1, +rowSize*3,
                                +rowSize*4
    )

    val actionMap : MutableMap<Int, HexAction> = mutableMapOf()

    appButtons.forEach { appButton->
        actionMap[(appButton.index%rowSize) + (appButton.index/rowSize) * rowSize] = HexAction(appPackage = appButton.app.packageName)
    }
    folders.forEach { folder ->
        actionMap[(folder.index%rowSize) + (folder.index/rowSize) * rowSize] = HexAction(folderIndex = folder)
    }
    folders.forEach { folder->
        val folderIndex = (folder.index%rowSize) + (folder.index/rowSize) * rowSize
        if((folder.index/rowSize) % 2 == 1){
            if(folder.opened){
                if(folder.apps.size < 7){
                    for (i in 0 until folder.apps.size){
                        actionMap[folderIndex + offsets6Odd[i]] = HexAction(appPackage = folder.apps[i].packageName)
                    }
                }else if(folder.apps.size < 19){
                    for (i in 0 until folder.apps.size){
                        actionMap[folderIndex + offsets18Odd[i]] = HexAction(appPackage = folder.apps[i].packageName)
                    }
                }
            }
        }
        else{
            if(folder.opened){
                if(folder.apps.size < 7){
                    for (i in 0 until folder.apps.size){
                        actionMap[folderIndex + offsets6Even[i]] = HexAction(appPackage = folder.apps[i].packageName)
                    }
                }else if(folder.apps.size < 19){
                    for (i in 0 until folder.apps.size){
                        actionMap[folderIndex + offsets18Even[i]] = HexAction(appPackage = folder.apps[i].packageName)
                    }
                }
            }
        }
    }
    return actionMap
}

fun indexToPosition(i : Int, separation: Int, buttonSize: Dp, rowSize: Int): Array<Float> {
    val x = (i % rowSize) * separation * 1.6F + (if((i / rowSize) % 2 == 0) buttonSize.value * 1.6F else 0F)
    val y = (i / rowSize) * separation.toFloat() * 0.47F
    return arrayOf(x,y)
}

fun positionToIndex(offset : Offset, offsetX : Float, offsetY : Float, separation : Int, buttonSize : Dp): Int {
    val j =
        ((((offset.y - separation) / 0.47) - offsetY) / separation) / 2
    val i =
        ((offset.x - separation - offsetX - (if ((floor(j).roundToInt() + 1) % 2 == 0) buttonSize.value * 1.6F else 0F)) / (separation * 1.6F))
    val J = (floor((j + 0.5) * 2) - 1).toInt()
    val I =
        floor(i) + (if (J % 4 == 0) 1 else 0) + if (J % 2 == 1) 1 else (if (J % 4 == 0) -1 else 1)
    println("$I, $J :-> ${(J * 10) + I}")
    return ((J * 10) + I).toInt()
}

fun positionToOffset(posX: Float, posY: Float, offsetX: Float, offsetY: Float): IntOffset {
    return IntOffset((posX + offsetX).toInt(), ((posY + offsetY) * 0.47F).toInt())
}
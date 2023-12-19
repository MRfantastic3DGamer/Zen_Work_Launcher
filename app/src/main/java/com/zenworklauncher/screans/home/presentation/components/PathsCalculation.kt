package com.zenworklauncher.screans.home.presentation.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.IntSize
import java.lang.Float.max
import java.lang.Float.min
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun PathsCalculation(
    triggerOffset: Offset,
    triggerSize: IntSize,
    sidePadding: Float,
    selectionOffsetY: Float,
    radius_1: Float,
    radius_3: Float,
    isTriggerActive: Boolean,
): List<Path> {
    val maxAngle        = 180f
    val quality_1_2     = 20
    val quality_3_4     = 80
    val quality_trigg   = 15
    val padding         = 10f
    val paths           = mutableListOf<Path>()
    val arcsOffset = triggerOffset + Offset(-100f, 0f)

    val selectedAlphabetOffset = arcsOffset + Offset(-sidePadding/2, selectionOffsetY)
// region arc 1, 2
    val arcPoints1      = mutableListOf<Offset>()
    val arcPoints2      = mutableListOf<Offset>()
    val radius_2        = radius_1 + padding
    val deltaAngle_1_2  = (maxAngle / quality_1_2) * (PI / 180).toFloat()
    val arcOffset_2 = arcsOffset + Offset(-padding, 0f)
    arcPoints1.add(triggerOffset + Offset(0f, selectionOffsetY + radius_1))
    arcPoints2.add(arcOffset_2 + Offset(0f, selectionOffsetY + radius_2))
    repeat(quality_1_2){
        val angle = deltaAngle_1_2 * it
        arcPoints1.add(selectedAlphabetOffset + Offset(-(sin(angle) * radius_1), (cos(angle) * radius_1)))
        arcPoints2.add(selectedAlphabetOffset + Offset(-(sin(angle) * radius_2), (cos(angle) * radius_2)))
    }
    arcPoints1.add(triggerOffset + Offset(0f, selectionOffsetY - radius_1 ))
    arcPoints2.add(arcOffset_2 + Offset(0f, selectionOffsetY - radius_2 ))
// endregion

// region arc 3, 4
    val arcPoints3      = mutableListOf<Offset>()
    val arcPoints4      = mutableListOf<Offset>()
    val radius_4        = radius_3 + padding
    val deltaAngle_3_4  = (maxAngle / quality_3_4) * (PI / 180).toFloat()
    val arcOffset_3_4 = arcsOffset + Offset(-padding, 0f)
    arcPoints3.add(arcOffset_3_4 + Offset(0f, selectionOffsetY + radius_3))
    arcPoints4.add(arcOffset_3_4 + Offset(0f, selectionOffsetY + radius_4))
    repeat(quality_3_4){
        val angle = deltaAngle_3_4 * it
        arcPoints3.add(selectedAlphabetOffset + Offset(-(sin(angle) * radius_3), (cos(angle) * radius_3)))
        arcPoints4.add(selectedAlphabetOffset + Offset(-(sin(angle) * radius_4), (cos(angle) * radius_4)))
    }
    arcPoints3.add(arcOffset_3_4 + Offset(0f, selectionOffsetY - radius_3 ))
    arcPoints4.add(arcOffset_3_4 + Offset(0f, selectionOffsetY - radius_4 ))
// endregion

// region arc trigger top, bottom
    val triggerArcTop   = mutableListOf<Offset>()
    val triggerArcBot   = mutableListOf<Offset>()
    val triggerArcRadius= (triggerSize.width/2).toFloat()
    val deltaAngle_trigg= (maxAngle / quality_trigg) * (PI / 180).toFloat()
    val triggerTopArcMid       = triggerOffset + Offset(triggerArcRadius, min(0f, selectionOffsetY - radius_1 + triggerArcRadius))
    val triggerBotArcMid       = triggerOffset + Offset(triggerArcRadius, triggerSize.height.toFloat())
    repeat(quality_trigg){
        val angle = deltaAngle_trigg * it
        triggerArcTop.add(triggerTopArcMid + Offset(-(cos(angle) * triggerArcRadius), -(sin(angle) * triggerArcRadius)))
        triggerArcBot.add(triggerBotArcMid + Offset( (cos(angle) * triggerArcRadius),  (sin(angle) * triggerArcRadius)))
    }
// endregion

// region trigger path
    val triggerOpenPath = Path()
    triggerOpenPath.moveTo(arcPoints1.first().x, arcPoints1.first().y)
    if (isTriggerActive){
        for (i in 0 until  arcPoints1.size)
            triggerOpenPath.lineTo(arcPoints1[i].x, arcPoints1[i].y)
    }
    val minY        = arcPoints1.last().y
    val maxY        = arcPoints1.first().y
    for (i in 0 until triggerArcTop.size/2)
        triggerOpenPath.lineTo(triggerArcTop[i].x, min(minY, triggerArcTop[i].y))
    for (i in triggerArcTop.size/2+1 until triggerArcTop.size)
        triggerOpenPath.lineTo(triggerArcTop[i].x, triggerArcTop[i].y)
    for (i in 0 until triggerArcBot.size)
        triggerOpenPath.lineTo(triggerArcBot[i].x, max(maxY, triggerArcBot[i].y))
    triggerOpenPath.close()
    paths.add(triggerOpenPath)
// endregion

// region apps section path
    val appsSectionPath = Path()
    val appsSectionPathStart = selectedAlphabetOffset + Offset(100 - padding, radius_2)
    val appsSectionPathArc2End = selectedAlphabetOffset + Offset(100 - padding, -radius_2)
    val appsSectionPathArc3Start = selectedAlphabetOffset + Offset(100 - padding, radius_3)
    val appsSectionPathArc3End = selectedAlphabetOffset + Offset(100 - padding, -radius_3)

    val minYValue = triggerOffset.y - radius_2 - 100

    appsSectionPath.moveTo(appsSectionPathArc3Start.x, appsSectionPathArc3Start.y)
    appsSectionPath.lineTo(appsSectionPathStart.x, max(appsSectionPathStart.y, minYValue))
    for (i in 0 until  arcPoints2.size)
        appsSectionPath.lineTo(arcPoints2[i].x, max(arcPoints2[i].y, minYValue))
    appsSectionPath.lineTo(appsSectionPathArc2End.x, max(appsSectionPathArc2End.y, minYValue))
    appsSectionPath.lineTo(appsSectionPathArc3End.x, max(appsSectionPathArc3End.y, minYValue))
    arcPoints3.reverse()
    for (i in 0 until  arcPoints3.size)
        appsSectionPath.lineTo(arcPoints3[i].x, max(arcPoints3[i].y, minYValue))
    appsSectionPath.close()
    paths.add(appsSectionPath)
// endregion

    return paths
}
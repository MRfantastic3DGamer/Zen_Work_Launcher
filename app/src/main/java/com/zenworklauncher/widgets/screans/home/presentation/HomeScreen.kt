package com.zenworklauncher.widgets.screans.home.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toOffset
import androidx.core.math.MathUtils.clamp
import com.dhruv.quick_apps.QuickAppsTrigger
import com.dhruv.quick_apps.QuickAppsVisual
import com.dhruv.radial_quick_actions.model.QuickAction
import com.dhruv.radial_quick_actions.presentation.RadialQuickActionTrigger
import com.dhruv.radial_quick_actions.presentation.RadialQuickActionVisual
import com.zenworklauncher.R
import com.zenworklauncher.widgets.screans.home.HomeViewModel
import com.zenworklauncher.widgets.screans.home.model.AppData
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class HomeQuickAction(name: String, onSelect: ()->Unit ) : QuickAction(name, onSelect)

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: HomeViewModel
) {

    val selectedYOffset by animateFloatAsState(targetValue = viewModel.quickAppsViewModel.getSelectedStringYOffset, label = "selected-string-Y-offset")

    @Composable
    fun RadialQuickAction(){
        Column {
            Box {
                RadialQuickActionTrigger(
                    modifier = Modifier
                        .size(50.dp, 300.dp),
                    viewModel = viewModel.radialViewModel
                )
                RadialQuickActionVisual(
                    modifier = Modifier
                        .size(50.dp, 300.dp),
                    viewModel = viewModel.radialViewModel,
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

    @Composable
    fun QuickApps(){
        Column {
            Box {
                QuickAppsTrigger(
                    modifier = Modifier
                        .size(50.dp, 490.dp), viewModel = viewModel.quickAppsViewModel
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    @Composable
    fun blurredBG(blurAmount: Dp, image:Int){
        Image(
            modifier = Modifier
                .fillMaxSize()
                .blur(
                    blurAmount,
                    edgeTreatment = BlurredEdgeTreatment(
                        RoundedCornerShape(0.dp)
                    )
                ),
            bitmap = ImageBitmap.imageResource(id = image),
            contentDescription = "quick-apps-background",
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopStart,
        )
    }

    @Composable
    fun tapHighLite(){
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Brush.radialGradient(
                colors = listOf(Color(1f,1f,1f,0.5f), Color(1f,1f,1f,0f), Color(1f,1f,1f,0f), Color(1f,1f,1f,0f)),
                center = viewModel.quickAppsViewModel.getGlobalTouchPosition
            ))
        )
    }

    @Composable
    fun outline(path: Path, width: Float){
        Canvas(
            modifier = Modifier
        ){
            this.drawPath(
                path,
                Color.White,
                style = Stroke(
                    width = width
                ),
                blendMode = BlendMode.Overlay
            )
        }
    }

    fun selectedAlphabetSemiCirclePoints(quality: Int = 20, offset: Offset, selectionOffset: Float, selectionRadius: Float, maxAngle: Float = 180f ): MutableList<Offset> {
        val points = mutableListOf<Offset>()
        val deltaAngle = (maxAngle / quality) * (PI / 180).toFloat()
        val selectedAlphabetOffset = offset + Offset(-viewModel.quickAppsViewModel.sidePadding/2, selectionOffset)
        var angle = 0f
        repeat(quality){ i ->
            val curr = selectedAlphabetOffset + Offset(-(sin(angle) * selectionRadius), (cos(angle) * selectionRadius))
            points.add(curr)
            angle += deltaAngle
        }
        return points
    }

    Box(
        modifier = modifier
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize(),
            bitmap = ImageBitmap.imageResource(id = R.drawable.wallpaper),
            contentDescription = "wallpaper",
            contentScale = ContentScale.Crop
        )
        QuickAppsVisual(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel.quickAppsViewModel,
            alphabetSideFloat = 100F,
            appComposable = { action, modifier, selected ->
                if (selected){
                    Box(
                        modifier = modifier
                            .clip(CircleShape)
                            .size(75.dp)
                            .background(Color.Black)
                            .paint(
                                (action as AppData).painter,
                                contentScale = ContentScale.Crop,
                                alignment = Alignment.Center,
                            )
                    )
                }
                else
                {
                    Box(
                        modifier = modifier
                            .clip(CircleShape)
                            .size(60.dp)
                            .paint(
                                (action as AppData).painter,
                                contentScale = ContentScale.Crop,
                                alignment = Alignment.Center,
                            )
                    )
                }
            },
            wordsBGComposable = { offset, size, _ ->

                fun alphabetsBackGroundPath(Size: IntSize, offset: IntOffset, radius: Float, selectionOffset: Float, selectionRadius: Float, boxMargin: Float = 10f): Path{

                    val topLeft = offset.toOffset() + Offset(0f, -boxMargin)
                    val topRight = offset.toOffset() + Offset(Size.width.toFloat(), 0f) + Offset(0f, -boxMargin - selectionRadius)
                    val bottomLeft = offset.toOffset() + Offset(0f, Size.height.toFloat()) + Offset(0f, boxMargin)
                    val bottomRight = offset.toOffset() + Offset(Size.width.toFloat(),Size.height.toFloat()) + Offset(0f, boxMargin + selectionRadius)
                    val selectedAlphabetOffset = offset.toOffset() + Offset(-viewModel.quickAppsViewModel.sidePadding/2, selectionOffset)

                    val points = mutableListOf<Offset>()
                    val quality = 20
                    points.add( offset.toOffset() + Offset(0f, selectionOffset + selectionRadius))
                    points.addAll(selectedAlphabetSemiCirclePoints(quality, offset.toOffset(), selectionOffset, selectionRadius))
                    points.add( offset.toOffset() + Offset(0f, selectionOffset - selectionRadius ))
                    if(points.last().y > topLeft.y) {
                        points.add(topLeft)
                    }
                    points.add(topRight)
                    points.add(bottomRight)
                    if(points.first().y < bottomLeft.y)
                        points.add(bottomLeft)

                    val path = Path()
                    path.moveTo (points.first().x, points.first().y)
                    points.forEach {
                        path.lineTo(it.x,it.y)
                    }
                    path.close()

                    return path
                }

                fun alphabetsBackGroundShape(Size: IntSize, offset: IntOffset, radius: Float, selectionOffset: Float, selectionRadius: Float): Shape{
                    return object : Shape {
                        override fun createOutline(
                            size: Size,
                            layoutDirection: LayoutDirection,
                            density: Density
                        ): Outline {
                            return Outline.Generic(
                                alphabetsBackGroundPath(
                                    Size,
                                    offset,
                                    radius,
                                    selectionOffset,
                                    selectionRadius,
                                    boxMargin = 20f
                                )
                            )
                        }

                    }
                }

                Box(modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        alphabetsBackGroundShape(
                            size,
                            offset,
                            10f,
                            selectedYOffset,
                            100f
                        )
                    )
                )
                {

                    blurredBG(5.dp, R.drawable.wallpaper)

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(1f, 1f, 1f, 0.03f),
                                        Color(1f, 1f, 1f, 0.1f)
                                    ),
                                    start = offset.toOffset() + Offset(
                                        0f,
                                        size.height.toFloat()
                                    ),
                                    end = offset.toOffset() + Offset(size.width.toFloat(), 0f)
                                )
                            )
                    )

                    tapHighLite()

                    outline(
                        alphabetsBackGroundPath(
                            size,
                            offset,
                            10f,
                            selectedYOffset,
                            100f
                        ),
                        5f
                    )
                }
            },
            iconsBGComposable = { offset, size, _ ->

                fun appsBackGroundPath(offset: IntOffset): Path {
                    val points = mutableListOf<Offset>()
                    val outerPoints = selectedAlphabetSemiCirclePoints(
                        quality =  100,
                        offset.toOffset(),
                        selectedYOffset,
                        viewModel.quickAppsViewModel.getAlphabetAppsMaxRadius + 100
                    ).reversed()
                    val innerPoints = selectedAlphabetSemiCirclePoints(
                        quality =  100,
                        offset.toOffset(),
                        selectedYOffset,
                        100f
                    )
                    points.add(Offset(offset.x.toFloat(), outerPoints.first().y))
                    points.addAll(outerPoints)
                    points.add(Offset(offset.x.toFloat(), outerPoints.last().y))
                    points.add(Offset(offset.x.toFloat(), innerPoints.first().y))
                    points.addAll(innerPoints)
                    points.add(Offset(offset.x.toFloat(), innerPoints.last().y))
                    val path = Path()
                    val start = Offset(
                        clamp(points.first().x, 0f, offset.x.toFloat()),
                        clamp(points.first().y, offset.y.toFloat() - 100, offset.y.toFloat() + size.height)
                    )
                    path.moveTo(start.x, start.y)
                    points.forEach { p ->
                        val curr = Offset( clamp(p.x, 0f, offset.x.toFloat()), clamp(p.y, offset.y.toFloat() - 100, offset.y.toFloat() + size.height) )
                        path.lineTo(curr.x, curr.y)
                    }
                    path.close()
                    return path
                }

                fun appsBackGroundShape(Size: IntSize, offset: IntOffset): Shape{
                    return object : Shape {
                        override fun createOutline(
                            size: Size,
                            layoutDirection: LayoutDirection,
                            density: Density
                        ): Outline {
                            return Outline.Generic(
                                appsBackGroundPath(
                                    offset
                                )
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(
                            appsBackGroundShape(
                                size,
                                offset
                            )
                        ),
                ){
                    blurredBG(5.dp, R.drawable.wallpaper)

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(1f, 1f, 1f, 0.03f),
                                        Color(1f, 1f, 1f, 0.1f)
                                    ),
                                    start = Offset(0f, offset.y + size.height.toFloat()),
                                    end = offset.toOffset()
                                )
                            )
                    )

                    tapHighLite()
                }
                outline(appsBackGroundPath(offset), 2.5f)
            },
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            Arrangement.Bottom,
        )
        {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = viewModel.quickAppsViewModel.currentAction?.name ?: "",
                style = TextStyle(
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = TextUnit(35f, TextUnitType.Sp)
                )
            )

            Box(modifier = Modifier.height(100.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                Arrangement.End,
                Alignment.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp, 500.dp)
                )
                RadialQuickAction()
                QuickApps()
            }
        }
    }
}
package com.devansh.noteapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun ColorPicker(
    activeColor:Color,
    screenWidthPx: Float,
    onColorSelected: (Color) -> Unit
) {
    val minPx = 0f
    val maxPx = screenWidthPx
    val dragOffset = remember { mutableStateOf(0f) }

    Box(modifier = Modifier.padding(8.dp)) {
        // Slider
        Spacer(
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(brush = colorMapGradient(screenWidthPx))
                .align(Alignment.Center)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        dragOffset.value = offset.x
                        onColorSelected(getActiveColor(dragOffset.value, screenWidthPx))
                    }
                }
        )
        // Draggable icon
        Icon(
            imageVector = Icons.Filled.FiberManualRecord,
            tint = activeColor,
            contentDescription = null,
            modifier = Modifier
                .offset { IntOffset(dragOffset.value.roundToInt(), 0) }
                .border(
                    border = BorderStroke(4.dp, MaterialTheme.colorScheme.onSurface),
                    shape = CircleShape
                )
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        val newValue = dragOffset.value + delta
                        dragOffset.value = newValue.coerceIn(minPx, maxPx)
                        onColorSelected(getActiveColor(dragOffset.value, screenWidthPx))
                    }
                )
        )
    }
}

fun createColorMap(): List<Color> {
    val colorList = mutableListOf<Color>()
    for (i in 0..360 step 2) {
        val hue = i.toFloat() / 360f
        val saturation = 0.9f + Random.nextFloat() * 0.1f
        val lightness = 0.5f + Random.nextFloat() * 0.1f
        colorList.add(hslToColor(hue, saturation, lightness))
    }
    return colorList
}

fun colorMapGradient(screenWidthPx: Float) = Brush.horizontalGradient(
    colors = createColorMap(),
    startX = 0f,
    endX = screenWidthPx
)

fun getActiveColor(dragPosition: Float, screenWidthPx: Float): Color {
    val hue = (dragPosition / screenWidthPx)
    val saturation = 0.9f + Random.nextFloat() * 0.1f
    val lightness = 0.5f + Random.nextFloat() * 0.1f
    return hslToColor(hue, saturation, lightness)
}

fun hslToColor(hue: Float, saturation: Float, lightness: Float): Color {
    val c = (1 - kotlin.math.abs(2 * lightness - 1)) * saturation
    val x = c * (1 - kotlin.math.abs((hue * 6) % 2 - 1))
    val m = lightness - c / 2
    val (r, g, b) = when {
        hue < 1 / 6f -> Triple(c, x, 0f)
        hue < 2 / 6f -> Triple(x, c, 0f)
        hue < 3 / 6f -> Triple(0f, c, x)
        hue < 4 / 6f -> Triple(0f, x, c)
        hue < 5 / 6f -> Triple(x, 0f, c)
        else -> Triple(c, 0f, x)
    }
    return Color((r + m), (g + m), (b + m))
}

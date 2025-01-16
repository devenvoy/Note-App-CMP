package com.devansh.noteapp.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp

@Composable
fun ColorPickerView(
    activeColor: Color,
    onColorSelected: (Color) -> Unit
) {
    val screenSize = remember { mutableStateOf(Pair(-1, -1)) }
    var sColor by remember { mutableStateOf(Color.Red) }
    Layout(
        modifier = Modifier.height(50.dp),
        content = {
            ColorPicker(
                screenWidthPx = screenSize.value.first.toFloat(),
                onColorSelected = onColorSelected,
                activeColor = activeColor
            )
        },
        measurePolicy = { measurables, constraints ->
            val width = constraints.maxWidth
            val height = constraints.maxHeight

            screenSize.value = Pair(width, height)
            println("Width: $width, height: $height")

            val placeables = measurables.map { measurable ->
                measurable.measure(constraints)
            }

            layout(width, height) {
                var yPosition = 0
                placeables.forEach { placeable ->
                    placeable.placeRelative(x = 0, y = yPosition)
                    yPosition += placeable.height
                }
            }
        }
    )
}
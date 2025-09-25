package com.devansh.noteapp.core.designsystem.components.colorPickerBottomSheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.ColorPickerController

@Composable
fun ColorPickerContent(
    currentColor: Long?,
    onColorChanged: (Long?) -> Unit,
    controller: ColorPickerController
) {
    var useCustomColor by remember(currentColor) { mutableStateOf(currentColor != null) }
    var basicExpanded by remember { mutableStateOf(true) }
    var advancedExpanded by remember { mutableStateOf(false) }

    val selectedColor = currentColor?.let { Color(it) } ?: MaterialTheme.colorScheme.surface

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header Section
        ColorPickerHeader()

        Spacer(modifier = Modifier.height(24.dp))

        // Custom Color Toggle
        CustomColorToggle(
            useCustomColor = useCustomColor,
            onToggleChanged = { enabled ->
                useCustomColor = enabled
                if (!enabled) {
                    onColorChanged(null)
                } else {
                    basicExpanded = true
                }
            }
        )

        // Color Selection Content
        AnimatedVisibility(
            visible = useCustomColor,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))

                // Basic Color Selection
                BasicColorSection(
                    expanded = basicExpanded,
                    onExpandedChanged = { basicExpanded = it },
                    selectedColor = selectedColor,
                    controller = controller,
                    onColorChanged = { color ->
                        onColorChanged(color.toArgb().toLong())
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Advanced Color Selection
                AdvancedColorSection(
                    expanded = advancedExpanded,
                    onExpandedChanged = { advancedExpanded = it },
                    controller = controller
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

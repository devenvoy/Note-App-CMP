package com.devansh.noteapp.core.designsystem.components.colorPickerBottomSheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker

@Composable
fun BasicColorSection(
    expanded: Boolean,
    onExpandedChanged: (Boolean) -> Unit,
    selectedColor: Color,
    controller: ColorPickerController,
    onColorChanged: (Color) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column {
            // Section Header
            SectionHeader(
                title = "Basic Color Selection",
                icon = Icons.Default.Palette,
                iconTint = MaterialTheme.colorScheme.primary,
                expanded = expanded,
                onExpandedChanged = onExpandedChanged
            )
            
            // Section Content
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        // HSV Color Picker
                        HsvColorPicker(
                            modifier = Modifier
                                .weight(1f)
                                .height(200.dp),
                            controller = controller,
                            initialColor = selectedColor,
                            onColorChanged = { colorEnvelope: ColorEnvelope ->
                                onColorChanged(colorEnvelope.color)
                            }
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        // Color Preview
                        ColorPreview(selectedColor = selectedColor)
                    }
                }
            }
        }
    }
}

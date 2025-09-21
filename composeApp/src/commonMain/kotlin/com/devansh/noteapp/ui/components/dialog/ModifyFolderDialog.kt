package com.devansh.noteapp.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Colorize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.devansh.noteapp.domain.model.Category
import com.devansh.noteapp.domain.utils.UnitCBF
import com.devansh.noteapp.ui.screens.core.textColors
import noteapp.composeapp.generated.resources.Res
import noteapp.composeapp.generated.resources.cancel
import noteapp.composeapp.generated.resources.modify
import noteapp.composeapp.generated.resources.name
import noteapp.composeapp.generated.resources.ok
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyFolderDialog(
    category: Category,
    onDismissRequest: UnitCBF,
    onModify: (Category) -> Unit
) {

    var text by remember { mutableStateOf(category.name) }
    var color by remember { mutableStateOf(category.color) }

    // Check if current color is custom (not in predefined colors)
    val isCustomColor = color != null && !Category.folderColors.contains(Color(color!!))

    // Initialize selected index based on current color
    val initValue = when {
        category.color == null -> 0 // No color selected
        isCustomColor -> Category.folderColors.size + 1 // Custom color
        else -> Category.folderColors.indexOf(Color(category.color)) + 1 // Predefined color
    }

    var selectedIndex by remember { mutableIntStateOf(initValue) }
    var showColorDialog by remember { mutableStateOf(false) }

    AlertDialog(
        title = {
            Text(text = stringResource(Res.string.modify))
        },
        text = {
            Column {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = text,
                    onValueChange = { text = it },
                    singleLine = true,
                    placeholder = { Text(text = stringResource(Res.string.name)) },
                )
                LazyRow(
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(Category.folderColors.size + 2) { index ->
                        when (index) {
                            0 -> {
                                // No color option
                                ColoredCircle2(
                                    selected = selectedIndex == 0
                                ) {
                                    selectedIndex = 0
                                    color = null
                                }
                            }

                            Category.folderColors.size + 1 -> {
                                // Custom color picker option
                                val displayColor = if (isCustomColor && selectedIndex == index) {
                                    Color(color!!)
                                } else {
                                    Color.Black
                                }

                                ColoredCircle3(
                                    background = displayColor,
                                    selected = selectedIndex == index
                                ) {
                                    selectedIndex = index
                                    showColorDialog = true
                                }
                            }

                            else -> {
                                // Predefined colors
                                ColoredCircle(
                                    color = Category.folderColors[index - 1],
                                    selected = selectedIndex == index,
                                    onClick = {
                                        selectedIndex = index
                                        color = Category.folderColors[index - 1].toArgb().toLong()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            val haptic = LocalHapticFeedback.current
            Button(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.Confirm)

                    // Determine final color based on selection
                    val finalColor = when (selectedIndex) {
                        0 -> null // No color
                        Category.folderColors.size + 1 -> color // Custom color (already set)
                        else -> Category.folderColors[selectedIndex - 1].toArgb().toLong() // Predefined color
                    }

                    onModify(
                        Category(
                            id = category.id,
                            name = text.trim(),
                            color = finalColor
                        )
                    )

                    onDismissRequest()
                }
            ) {
                Text(stringResource(Res.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(Res.string.cancel))
            }
        }
    )
}

@Composable
fun ColoredCircle(color: Color, selected: Boolean, onClick: UnitCBF) {
    val background = MaterialTheme.colorScheme.onSurface
    val selectionRing = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent

    Box(
        modifier = Modifier
            .size(50.dp)
            .drawBehind {
                if (selected) {
                    drawCircle(
                        color = selectionRing,
                        radius = size.minDimension / 2f
                    )
                }
            }
            .clip(shape = CircleShape)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (selected) 4.dp else 2.dp)
                .clip(shape = CircleShape)
                .background(color = color)
        )
    }
}

@Composable
fun ColoredCircle2(selected: Boolean, onClick: UnitCBF) {
    val background = MaterialTheme.colorScheme.onSurface
    val selectionRing = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent

    Box(
        modifier = Modifier
            .size(50.dp)
            .drawBehind {
                if (selected) {
                    drawCircle(
                        color = selectionRing,
                        radius = size.minDimension / 2f
                    )
                }
            }
            .clip(shape = CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "A",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ColoredCircle3(background: Color, selected: Boolean, onClick: UnitCBF) {
    val selectionRing = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent

    Box(
        modifier = Modifier
            .size(50.dp)
            .drawBehind {
                if (selected) {
                    drawCircle(
                        color = selectionRing,
                        radius = size.minDimension / 2f
                    )
                }
            }
            .clip(shape = CircleShape)
            .background(background)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Colorize,
            contentDescription = "Custom Color",
            tint = if (background.luminance() > 0.5f) Color.Black else Color.White
        )
    }
}
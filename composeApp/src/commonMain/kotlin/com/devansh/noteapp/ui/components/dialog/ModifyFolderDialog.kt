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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.devansh.noteapp.domain.model.Category
import com.devansh.noteapp.ui.screens.core.textColors
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.color.ColorDialog
import com.maxkeppeler.sheets.color.models.ColorConfig
import com.maxkeppeler.sheets.color.models.ColorSelection
import com.maxkeppeler.sheets.color.models.ColorSelectionMode
import com.maxkeppeler.sheets.color.models.MultipleColors
import com.maxkeppeler.sheets.color.models.SingleColor
import note_app_cmp.composeapp.generated.resources.Res
import note_app_cmp.composeapp.generated.resources.cancel
import note_app_cmp.composeapp.generated.resources.modify
import note_app_cmp.composeapp.generated.resources.name
import note_app_cmp.composeapp.generated.resources.ok
import org.jetbrains.compose.resources.stringResource

@Composable
fun ModifyFolderDialogPreview() {
    ModifyFolderDialog(
        category = Category(),
        onDismissRequest = {},
        onModify = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyFolderDialog(
    category: Category,
    onDismissRequest: () -> Unit,
    onModify: (Category) -> Unit
) {

    var text by remember { mutableStateOf(category.name) }
    var color by remember { mutableStateOf(category.color) }
    val custom =
        color != null && !Category.folderColors.contains(Color(color!!))
    val initValue =
        if (category.color == null) 0
        else if (custom) Category.folderColors.size + 1
        else Category.folderColors.indexOf(Color(category.color)) + 1
    var selectedIndex by remember { mutableIntStateOf(initValue) }

    var showDialog by remember {
        mutableStateOf(false)
    }

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

                    items(Category.folderColors.size + 2) {
                        when (it) {
                            0 -> {
                                ColoredCircle2(selected = 0 == selectedIndex) { selectedIndex = 0 }
                            }

                            Category.folderColors.size + 1 -> {
                                ColoredCircle3(
                                    background = if (custom) Color(color!!) else Color.Black,
                                    selected = Category.folderColors.size + 1 == selectedIndex
                                ) {
                                    selectedIndex = Category.folderColors.size + 1
                                    showDialog = true
                                }
                            }

                            else -> {
                                ColoredCircle(
                                    color = Category.folderColors[it - 1],
                                    selected = it == selectedIndex,
                                    onClick = { selectedIndex = it }
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

                    color = when (selectedIndex) {
                        0 -> null
                        Category.folderColors.size + 1 -> color
                        else -> Category.folderColors[selectedIndex - 1].toArgb().toLong()
                    }

                    onModify(
                        Category(
                            id = category.id,
                            name = text,
                            color = color
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

    if (showDialog) {

        val colorState = rememberUseCaseState()

        val templateColors = MultipleColors.ColorsInt(*textColors.map { it.toArgb() }.toIntArray())

        ColorDialog(
            state = colorState,
            selection = ColorSelection(
                selectedColor = SingleColor(if (custom) Color(color!!).toArgb() else Color.White.toArgb()),
                onSelectColor = { color = it.toLong() },
            ),
            config = ColorConfig(
                displayMode = ColorSelectionMode.TEMPLATE,
                templateColors = templateColors,
            )
        )
    }
}

@Composable
fun ColoredCircle(color: Color, selected: Boolean, onClick: () -> Unit) {

    val background = MaterialTheme.colorScheme.onSurface

    Box(
        modifier = Modifier
            .size(50.dp)
            .drawBehind {
                if (selected)
                    drawCircle(
                        color = background
                    )
            }
            .clip(shape = CircleShape)
            .clickable(onClick = onClick)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .clip(shape = CircleShape)
                .background(color = color)
        )
    }
}

@Composable
fun ColoredCircle2(selected: Boolean, onClick: () -> Unit) {

    val background = MaterialTheme.colorScheme.onSurface

    Box(
        modifier = Modifier
            .size(50.dp)
            .drawBehind {
                if (selected)
                    drawCircle(
                        color = background
                    )
            }
            .clip(shape = CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "A")
    }
}

@Composable
fun ColoredCircle3(background: Color, selected: Boolean, onClick: () -> Unit) {

    Box(
        modifier = Modifier
            .size(50.dp)
            .drawBehind {
                if (selected)
                    drawCircle(
                        color = background
                    )
            }
            .clip(shape = CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = Icons.Outlined.Colorize, contentDescription = "")
    }
}

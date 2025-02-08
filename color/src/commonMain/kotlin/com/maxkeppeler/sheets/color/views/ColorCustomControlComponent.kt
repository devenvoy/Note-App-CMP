/*
 *  Copyright (C) 2022-2024. Maximilian Keppeler (https://www.maxkeppeler.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.maxkeppeler.sheets.color.views


import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.LibOrientation
import com.maxkeppeker.sheets.core.utils.TestTags
import com.maxkeppeker.sheets.core.utils.testSequenceTagOf
import com.maxkeppeker.sheets.core.views.Grid
import com.maxkeppeler.sheets.color.models.ColorConfig
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import note_app_cmp.color.generated.resources.*
import note_app_cmp.color.generated.resources.Res
import note_app_cmp.color.generated.resources.scd_color_dialog_alpha
import note_app_cmp.color.generated.resources.scd_color_dialog_blue
import note_app_cmp.color.generated.resources.scd_color_dialog_green

/**
 * The control component to build up a custom color.
 * @param config The general configuration for the dialog view.
 * @param color The color that is currently selected.
 * @param onColorChange The listener that returns a selected color.
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun ColorCustomControlComponent(
    config: ColorConfig,
    orientation: LibOrientation,
    color: Int,
    onColorChange: (Int) -> Unit
) {

    val composeColor = Color(color)
    val alphaValue = remember(color) {
        val value = if (config.allowCustomColorAlphaValues) {
            composeColor.alpha
        } else {
            1F
        }
        mutableStateOf(value)
    }
    val redValue = remember(color) { mutableStateOf(composeColor.red) }
    val greenValue = remember(color) { mutableStateOf(composeColor.green) }
    val blueValue = remember(color) { mutableStateOf(composeColor.blue) }

    val newColor by remember(alphaValue.value, redValue.value, greenValue.value, blueValue.value) {
        mutableStateOf(Color(redValue.value, greenValue.value, blueValue.value, alphaValue.value))
    }

    LaunchedEffect(newColor) { onColorChange.invoke(newColor.toArgb()) }

    val colorItemLabelWidth = remember { mutableStateOf<Int?>(null) }
    val colorValueLabelWidth = remember { mutableStateOf<Int?>(null) }

    val colorItems = mutableListOf(
        if (config.allowCustomColorAlphaValues) stringResource(Res.string.scd_color_dialog_alpha) to alphaValue else null,
        stringResource(Res.string.scd_color_dialog_red) to redValue,
        stringResource(Res.string.scd_color_dialog_green) to greenValue,
        stringResource(Res.string.scd_color_dialog_blue) to blueValue
    ).filterNotNull()

    Grid(
        modifier = Modifier.padding(
            top = 16.dp
        ),
        items = colorItems,
        columns = when (orientation) {
            LibOrientation.PORTRAIT -> 1
            LibOrientation.LANDSCAPE -> 2
        },
        rowSpacing = 0.dp,
        columnSpacing = 24.dp
    ) { entry ->
        val index = colorItems.indexOf(entry)
        val onValueChange: (Int) -> Unit = {
            entry.second.value = it.toFloat()
        }
        val sliderTestTag = testSequenceTagOf(
            TestTags.COLOR_CUSTOM_VALUE_SLIDER,
            index.toString()
        )
        when (orientation) {
            LibOrientation.PORTRAIT ->
                ColorCustomControlListItemComponent(
                    label = entry.first,
                    value = entry.second.value.toInt(),
                    onValueChange = onValueChange,
                    colorItemLabelWidth = colorItemLabelWidth,
                    colorValueLabelWidth = colorValueLabelWidth,
                    sliderTestTag = sliderTestTag,
                )
            LibOrientation.LANDSCAPE ->
                ColorCustomControlGridItemComponent(
                    label = entry.first,
                    value = entry.second.value.toInt(),
                    onValueChange = onValueChange,
                    sliderTestTag = sliderTestTag,
                )
        }


    }
}

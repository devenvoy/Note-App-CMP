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
@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxkeppeler.sheets.color

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.maxkeppeker.sheets.core.models.base.*
import com.maxkeppeker.sheets.core.views.ButtonsComponent
import com.maxkeppeker.sheets.core.views.base.FrameBase
import com.maxkeppeler.sheets.color.models.ColorConfig
import com.maxkeppeler.sheets.color.models.ColorSelection
import com.maxkeppeler.sheets.color.models.ColorSelectionMode
import com.maxkeppeler.sheets.color.views.ColorCustomComponent
import com.maxkeppeler.sheets.color.views.ColorSelectionModeComponent
import com.maxkeppeler.sheets.color.views.ColorTemplateComponent

/**
 * Color view for the use-case to to select a color.
 * @param useCaseState The state of the sheet.
 * @param selection The selection configuration for the dialog view.
 * @param config The general configuration for the dialog view.
 * @param header The header to be displayed at the top of the dialog view.
 */
@ExperimentalMaterial3Api
@Composable
fun ColorView(
    useCaseState: UseCaseState,
    selection: ColorSelection,
    config: ColorConfig = ColorConfig(),
    header: Header? = null,
) {
    val colorState = rememberColorState(selection, config)
    StateHandler(useCaseState, colorState)

    val coroutine = rememberCoroutineScope()
    val onSelection: (Int) -> Unit = {
        colorState.processSelection(it)
        BaseBehaviors.autoFinish(
            selection = selection,
            coroutine = coroutine,
            onSelection = colorState::onFinish,
            onFinished = useCaseState::finish,
            onDisableInput = colorState::disableInput
        )
    }

    FrameBase(
        header = header,
        config = config,
        layout = { autoOrientation ->
            ColorSelectionModeComponent(
                config = config,
                selection = selection,
                mode = colorState.displayMode,
                onModeChange = { colorState.displayMode = it },
                onNoColorClick = {
                    selection.onSelectNone?.invoke()
                    useCaseState.finish()
                }
            )
            when (colorState.displayMode) {
                ColorSelectionMode.TEMPLATE ->
                    ColorTemplateComponent(
                        config = config,
                        colors = colorState.colors,
                        selectedColor = colorState.color,
                        inputDisabled = colorState.inputDisabled,
                        onColorClick = onSelection
                    )
                ColorSelectionMode.CUSTOM ->
                    ColorCustomComponent(
                        config = config,
                        orientation = autoOrientation,
                        color = colorState.color ?: Color.Gray.toArgb(),
                        onColorChange = colorState::processSelection,
                    )
            }
        },
        buttonsVisible = selection.withButtonView
                || config.displayMode != ColorSelectionMode.TEMPLATE
    ) { orientation ->
        ButtonsComponent(
            orientation = orientation,
            selection = selection,
            onPositiveValid = colorState.valid,
            onNegative = { selection.onNegativeClick?.invoke() },
            onPositive = colorState::onFinish,
            state = useCaseState,
        )
    }
}


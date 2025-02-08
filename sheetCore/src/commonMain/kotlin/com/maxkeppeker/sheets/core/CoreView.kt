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

package com.maxkeppeker.sheets.core

import androidx.compose.runtime.Composable
import com.maxkeppeker.sheets.core.models.CoreSelection
import com.maxkeppeker.sheets.core.models.base.Header
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.views.ButtonsComponent
import com.maxkeppeker.sheets.core.views.base.FrameBase

/**
 * Core view that functions as the base of a custom use-case.
 * @param useCaseState The use-case state.
 * @param selection The selection configuration for the dialog view.
 * @param header The header to be displayed at the top of the dialog view.
 * @param body The body content to be displayed inside the dialog view.
 * @param onPositiveValid If the positive button is valid and therefore enabled.
 */
@Composable
fun CoreView(
    useCaseState: UseCaseState,
    selection: CoreSelection,
    header: Header? = null,
    body: @Composable () -> Unit,
    onPositiveValid: Boolean = true
) {

    FrameBase(
        header = header,
        layout = { body() },
        buttonsVisible = selection.withButtonView
    ) {orientation ->

        ButtonsComponent(
            state = useCaseState,
            orientation = orientation,
            onPositiveValid = onPositiveValid,
            selection = selection,
            onNegative = { selection.onNegativeClick?.invoke() },
            onPositive = { selection.onPositiveClick?.invoke() },
        )
    }
}


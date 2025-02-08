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
package com.maxkeppeler.sheets.color.models

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb


/**
 * Helper class to simplify passing colors.
 * @param colorInt Value of color as Int.
 * @param colorRes Res of color value.
 * @param colorHex Color value as Hex-String.
 */
data class SingleColor(
    val colorInt: Int? = null,
    val colorHex: String? = null,
) {
    @OptIn(ExperimentalStdlibApi::class)
    fun colorInInt(): Int? = colorInt
        ?: colorHex?.let {
            Color(it.substringAfter('#').hexToLong()).toArgb()
        }
}

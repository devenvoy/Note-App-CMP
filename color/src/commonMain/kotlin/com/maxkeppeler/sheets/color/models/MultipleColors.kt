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
@file:Suppress("unused")

package com.maxkeppeler.sheets.color.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

/**
 * Holds various colors of a specific type.
 * @param colorsInt colors as packed color int, AARRGGBB.
 * @param colorsRes colors as resource references, R.color.the_color.
 * @param colorsHex color as String value, #RRGGBB or #AARRGGBB.
 */
sealed class MultipleColors(
    private var colorsInt: Array<Int>? = null,
    private var colorsHex: Array<String>? = null,
) {

    /**
     * Define a variety of color resource references.
     */
    class ColorsInt : MultipleColors {

        /**
         * Define dynamic amount of colors.
         * @param colors The color resource references
         */
        constructor(vararg colors: Int) : super(colorsInt = colors.toTypedArray())

        /**
         * Define an array of colors.
         * @param colors The color resource references
         */
        constructor(colors: List<Int>) : super(colorsInt = colors.toTypedArray())
    }

    /**
     * Define a variety of color String values.
     */
    class ColorsHex : MultipleColors {

        /**
         * Define dynamic amount of colors.
         * @param colors The color string values.
         */
        constructor(vararg colors: String) : super(colorsHex = colors.toList().toTypedArray())

        /**
         * Define an array of colors.
         * @param colors The color string values.
         */
        constructor(colors: List<String>) : super(colorsHex = colors.toTypedArray())
    }

    /**
     * Resolve the defined colors as color integers.
     * @param context the Context to resolve the colors.
     */
    @OptIn(ExperimentalStdlibApi::class)
    fun getColorsAsInt(): List<Int> {
        return colorsInt?.toList()
            ?: colorsHex?.map {
                Color(it.substringAfter('#').hexToLong()).toArgb()
            }
            ?: throw IllegalStateException("No colors available for color templates view. Either disabled template view or add colors.")
    }
}

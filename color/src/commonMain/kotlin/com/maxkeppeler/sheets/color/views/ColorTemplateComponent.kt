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


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.utils.BaseConstants
import com.maxkeppeker.sheets.core.utils.TestTags
import com.maxkeppeker.sheets.core.utils.testTags
import com.maxkeppeler.sheets.color.models.ColorConfig
import com.maxkeppeler.sheets.color.utils.Constants

/**
 * The template mode that displays a list of colors to choose from.
 * @param config The general configuration for the dialog view.
 * @param colors A list of colors that are displayed.
 * @param selectedColor The color that is currently selected.
 * @param inputDisabled If input is disabled.
 * @param onColorClick The listener that returns the selected color.
 */
@Composable
internal fun ColorTemplateComponent(
    config: ColorConfig,
    colors: List<Int>,
    selectedColor: Int?,
    inputDisabled: Boolean,
    onColorClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(Constants.COLOR_TEMPLATE_ITEM_SIZE),
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .sizeIn(
                maxHeight = BaseConstants.DYNAMIC_SIZE_MAX,
            )
            .fillMaxWidth()
            .graphicsLayer { alpha = 0.99F }
            .drawWithContent {
                val colorStops = arrayOf(
                    0.0f to Color.Transparent,
                    0.05f to Color.Black,
                    0.95f to Color.Black,
                    1.0f to Color.Transparent
                )
                drawContent()
                drawRect(
                    brush = Brush.verticalGradient(*colorStops),
                    blendMode = BlendMode.DstIn
                )
            },
    ) {
        items(colors) { color ->
            val selected = color == selectedColor
            ColorTemplateItemComponent(
                config = config,
                modifier = Modifier
                    .testTags(TestTags.COLOR_TEMPLATE_SELECTION, color)
                    .padding(4.dp),
                color = color,
                selected = selected,
                inputDisabled = inputDisabled,
                onColorClick = onColorClick
            )
        }
    }
}
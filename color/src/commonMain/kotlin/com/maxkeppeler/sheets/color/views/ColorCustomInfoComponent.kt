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
@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.maxkeppeler.sheets.color.views


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.utils.TestTags
import com.maxkeppeker.sheets.core.utils.testTags
import com.maxkeppeler.sheets.color.models.ColorConfig
import com.maxkeppeler.sheets.color.utils.Constants
import com.maxkeppeler.sheets.color.utils.getFormattedColor
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import note_app_cmp.color.generated.resources.Res
import note_app_cmp.color.generated.resources.scd_color_dialog_argb

/**
 * A information view for the custom color picker.
 * @param config The general configuration for the dialog view.
 * @param color The color that is currently selected.
 * @param onColorChange The listener that returns a selected color.
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun ColorCustomInfoComponent(
    config: ColorConfig,
    color: Int,
    onColorChange: (Int) -> Unit,
) {
    val colorPasteError = rememberSaveable { mutableStateOf<String?>(null) }
    LaunchedEffect(colorPasteError.value) {
        delay(3000)
        colorPasteError.value = null
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        ColorTemplateItemComponent(
            config = config,
            modifier = Modifier
                .testTags(TestTags.COLOR_CUSTOM_SELECTION, color)
                .size(Constants.COLOR_CUSTOM_ITEM_SIZE),
            color = color,
            selected = false,
            onColorClick = onColorChange
        )

        ElevatedCard(
            modifier = Modifier
                .height(Constants.COLOR_CUSTOM_ITEM_SIZE)
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp)
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    if (colorPasteError.value != null) {
                        Text(
                            modifier = Modifier.wrapContentWidth(),
                            text = colorPasteError.value!!,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    } else {
                        Text(
                            text = stringResource(Res.string.scd_color_dialog_argb),
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1
                        )
                        Text(
                            modifier = Modifier.padding(top = 2.dp),
                            text = getFormattedColor(color),
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1
                        )
                    }
                }
                if (colorPasteError.value == null) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

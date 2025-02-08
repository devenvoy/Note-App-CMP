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
import com.maxkeppeker.sheets.core.icons.LibIcons
import com.maxkeppeker.sheets.core.models.base.BaseConfigs
import com.maxkeppeker.sheets.core.utils.BaseConstants.DEFAULT_ICON_STYLE

/**
 * The general configuration for the clock dialog.
 * @param displayMode Available color selection modes. If null, both are used.
 * @param defaultDisplayMode Default view when opening ColorDialog.
 * @param templateColors Colors for the [ColorSelectionMode.TEMPLATE]-view.
 * @param allowCustomColorAlphaValues Allow alpha values in the custom color picker.
 * @param icons The style of icons that are used for dialog/ view-specific icons.
 */
data class ColorConfig(
    val displayMode: ColorSelectionMode? = null,
    val defaultDisplayMode: ColorSelectionMode? = null,
    val templateColors: MultipleColors = MultipleColors.ColorsInt(),
    val allowCustomColorAlphaValues: Boolean = true,
    override val icons: LibIcons = DEFAULT_ICON_STYLE,
) : BaseConfigs()
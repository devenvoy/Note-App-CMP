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

package com.maxkeppeker.sheets.core.views.base

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.BaseConfigs
import com.maxkeppeker.sheets.core.models.base.Header
import com.maxkeppeker.sheets.core.models.base.LibOrientation
import com.maxkeppeker.sheets.core.utils.AndroidWindowSizeFix
import com.maxkeppeker.sheets.core.utils.BaseValues
import com.maxkeppeker.sheets.core.utils.TestTags
import com.maxkeppeker.sheets.core.utils.isLandscape
import com.maxkeppeker.sheets.core.views.HeaderComponent

/**
 * Base component for the content structure of a dialog.
 * @param header The content to be displayed inside the dialog that functions as the header view of the dialog.
 * @param horizontalContentPadding The horizontal padding that is applied to the content.
 * @param layout The content to be displayed inside the dialog between the header and the buttons.
 * @param layoutHorizontalAlignment The horizontal alignment of the layout's children.
 * @param layoutLandscape The content to be displayed inside the dialog between the header and the buttons when the device is in landscape mode.
 * @param layoutLandscapeVerticalAlignment The vertical alignment of the layout's children in landscape mode.
 * @param buttonsVisible Display the buttons.
 * @param buttons The content to be displayed inside the dialog that functions as the buttons view of the dialog.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrameBase(
    header: Header? = null,
    config: BaseConfigs? = null,
    horizontalContentPadding: PaddingValues = BaseValues.CONTENT_DEFAULT_PADDING,
    layout: @Composable ColumnScope.(LibOrientation) -> Unit,
    layoutHorizontalAlignment: Alignment.Horizontal = Alignment.Start,
    layoutLandscape: @Composable (RowScope.() -> Unit)? = null,
    layoutLandscapeVerticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    buttonsVisible: Boolean = true,
    buttons: @Composable (ColumnScope.(LibOrientation) -> Unit)? = null,
) {
    AndroidWindowSizeFix {
        val layoutDirection = LocalLayoutDirection.current
        val isDeviceLandscape = isLandscape()
        val deviceOrientation = if (config?.orientation != LibOrientation.PORTRAIT && isDeviceLandscape) {
            LibOrientation.LANDSCAPE
        } else {
            LibOrientation.PORTRAIT
        }
        val layoutType = when (config?.orientation) {
            null -> {
                when {
                    // Only if auto orientation is currently landscape, content for landscape exists
                    // and the device screen is not larger than a typical phone.
                    isDeviceLandscape && layoutLandscape != null -> LibOrientation.LANDSCAPE
                    else -> LibOrientation.PORTRAIT
                }
            }
            LibOrientation.LANDSCAPE -> if (layoutLandscape != null) LibOrientation.LANDSCAPE else LibOrientation.PORTRAIT
            else -> config.orientation
        }

        Column(
            modifier = when (deviceOrientation) {
                LibOrientation.PORTRAIT -> Modifier.wrapContentSize()
                LibOrientation.LANDSCAPE -> Modifier
                    .wrapContentWidth()
            },
            horizontalAlignment = Alignment.Start
        ) {

            header?.takeUnless { deviceOrientation == LibOrientation.LANDSCAPE }?.let {
                // Display header
                Column(modifier = Modifier.testTag(TestTags.FRAME_BASE_HEADER)) {
                    HeaderComponent(
                        header = header,
                        contentHorizontalPadding = PaddingValues(
                            start = horizontalContentPadding.calculateStartPadding(layoutDirection),
                            end = horizontalContentPadding.calculateEndPadding(layoutDirection),
                        )
                    )
                }
            } ?: run {
                // If no header is defined, add extra spacing to the content top padding
                Spacer(
                    modifier = Modifier
                        .testTag(TestTags.FRAME_BASE_NO_HEADER)
                        .height(8.dp)
                )
            }

            val contentModifier = Modifier
                .testTag(TestTags.FRAME_BASE_CONTENT)
                .padding(
                    PaddingValues(
                        start = horizontalContentPadding.calculateStartPadding(
                            layoutDirection
                        ),
                        end = horizontalContentPadding.calculateEndPadding(layoutDirection),
                        // Enforce default top spacing
                        top = 16.dp,
                    )
                )
            when (layoutType) {
                LibOrientation.PORTRAIT -> {
                    Column(
                        modifier = contentModifier,
                        horizontalAlignment = layoutHorizontalAlignment,
                        content = { layout(deviceOrientation) }
                    )
                }
                LibOrientation.LANDSCAPE -> {
                    Row(
                        modifier = contentModifier,
                        verticalAlignment = layoutLandscapeVerticalAlignment,
                        content = layoutLandscape!!
                    )
                }
                else -> Unit
            }

            buttons?.let { buttons ->
                if (buttonsVisible) {
                    Column(modifier = Modifier.testTag(TestTags.FRAME_BASE_BUTTONS)) {
                        buttons.invoke(this, deviceOrientation)
                    }
                } else Spacer(
                    modifier = Modifier
                        .testTag(TestTags.FRAME_BASE_NO_BUTTONS)
                        .height(24.dp)
                )
            }
        }
    }
}
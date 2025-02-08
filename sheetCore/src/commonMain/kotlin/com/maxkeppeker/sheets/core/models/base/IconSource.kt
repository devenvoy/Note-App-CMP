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

package com.maxkeppeker.sheets.core.models.base

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi

/**
 * An icon from various sources alongside an optional contentDescription and tint.
 */
class IconSource {

    @OptIn(ExperimentalResourceApi::class)
    var drawableRes: DrawableResource? = null
    var imageVector: ImageVector? = null
    var bitmap: ImageBitmap? = null
    var painter: Painter? = null
    var contentDescription: String? = null
    var tint: Color? = null
    var selectedTint: Color? = null

    /**
     * Create an icon from a drawable res.
     * @param drawableRes Res of drawable to use as icon
     * @param contentDescription Text that is used as content description for the icon
     * @param tint Color that is used to tint the icon
     */
    @OptIn(ExperimentalResourceApi::class)
    constructor(
        drawableRes: DrawableResource,
        contentDescription: String? = null,
        tint: Color? = null
    ) {
        this.drawableRes = drawableRes
        this.contentDescription = contentDescription
        this.tint = tint
    }

    /**
     * Create an icon from a drawable res.
     * @param imageVector The vector graphic object to use as icon
     * @param contentDescription Text that is used as content description for the icon
     * @param tint Color that is used to tint the icon
     */
    constructor(
        imageVector: ImageVector,
        contentDescription: String? = null,
        tint: Color? = null
    ) {
        this.imageVector = imageVector
        this.contentDescription = contentDescription
        this.tint = tint
    }

    /**
     * Create an icon from a drawable res.
     * @param bitmap Drawable bitmap to use as icon
     * @param contentDescription Text that is used as content description for the icon
     * @param tint Color that is used to tint the icon
     */
    constructor(
        bitmap: ImageBitmap,
        contentDescription: String? = null,
        tint: Color? = null
    ) {
        this.bitmap = bitmap
        this.contentDescription = contentDescription
        this.tint = tint
    }

    /**
     * Create an icon from a drawable res.
     * @param painter Painter drawable to use as icon
     * @param contentDescription Text that is used as content description for the icon
     * @param tint Color that is used to tint the icon
     */
    constructor(
        painter: Painter,
        contentDescription: String? = null,
        tint: Color? = null
    ) {
        this.painter = painter
        this.contentDescription = contentDescription
        this.tint = tint
    }

    /**
     * Create an icon from a drawable res.
     * @param drawableRes Res of drawable to use as icon
     * @param contentDescription Text that is used as content description for the icon
     * @param tint Color that is used to tint the icon
     * @param selectedTint Color that is used to tint the icon when selected
     */
    @OptIn(ExperimentalResourceApi::class)
    constructor(
        drawableRes: DrawableResource,
        contentDescription: String? = null,
        tint: Color? = null,
        selectedTint: Color? = null
    ) {
        this.drawableRes = drawableRes
        this.contentDescription = contentDescription
        this.tint = tint
        this.selectedTint = selectedTint
    }

    /**
     * Create an icon from a drawable res.
     * @param imageVector The vector graphic object to use as icon
     * @param contentDescription Text that is used as content description for the icon
     * @param tint Color that is used to tint the icon
     * @param selectedTint Color that is used to tint the icon when selected
     */
    constructor(
        imageVector: ImageVector,
        contentDescription: String? = null,
        tint: Color? = null,
        selectedTint: Color? = null
    ) {
        this.imageVector = imageVector
        this.contentDescription = contentDescription
        this.tint = tint
        this.selectedTint = selectedTint
    }

    /**
     * Create an icon from a drawable res.
     * @param bitmap Drawable bitmap to use as icon
     * @param contentDescription Text that is used as content description for the icon
     * @param tint Color that is used to tint the icon
     */
    constructor(
        bitmap: ImageBitmap,
        contentDescription: String? = null,
        tint: Color? = null,
        selectedTint: Color? = null
    ) {
        this.bitmap = bitmap
        this.contentDescription = contentDescription
        this.tint = tint
        this.selectedTint = selectedTint
    }

    /**
     * Create an icon from a drawable res.
     * @param painter Painter drawable to use as icon
     * @param contentDescription Text that is used as content description for the icon
     * @param tint Color that is used to tint the icon
     */
    constructor(
        painter: Painter,
        contentDescription: String? = null,
        tint: Color? = null,
        selectedTint: Color? = null
    ) {
        this.painter = painter
        this.contentDescription = contentDescription
        this.tint = tint
        this.selectedTint = selectedTint
    }
}
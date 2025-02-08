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
package com.maxkeppeker.sheets.core.models.base

import androidx.compose.ui.text.AnnotatedString
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource

/**
 * Represents a button with text and an optional icon.
 */
open class SelectionButton {

    internal val text: String?
    @OptIn(ExperimentalResourceApi::class)
    internal val textRes: StringResource?
    val annotatedString: AnnotatedString?
    internal val icon: IconSource?
    internal val type: ButtonStyle

    /**
     * Creates a new instance of `SelectionButton` with the given text, icon and type.
     *
     * @param text The text to be displayed on the button.
     * @param icon The icon to be displayed on the button. Can be `null`.
     * @param type The style of the button. Default value is `ButtonStyle.TEXT`.
     */
    @OptIn(ExperimentalResourceApi::class)
    constructor(
        text: String,
        icon: IconSource? = null,
        type: ButtonStyle = ButtonStyle.TEXT
    ) {
        this.text = text
        this.textRes = null
        this.annotatedString = null
        this.icon = icon
        this.type = type
    }

    /**
     * Creates a new instance of `SelectionButton` with the given string resource identifier, icon and type.
     *
     * @param textRes The string resource identifier to be displayed on the button.
     * @param icon The icon to be displayed on the button. Can be `null`.
     * @param type The style of the button. Default value is `ButtonStyle.TEXT`.
     */
    @OptIn(ExperimentalResourceApi::class)
    constructor(
        textRes: StringResource,
        icon: IconSource? = null,
        type: ButtonStyle = ButtonStyle.TEXT
    ) {
        this.textRes = textRes
        this.text = null
        this.annotatedString = null
        this.icon = icon
        this.type = type
    }

    /**
     * Creates a new instance of `SelectionButton` with the given `AnnotatedString`, icon and type.
     *
     * @param annotatedString The annotated string to be displayed on the button.
     * @param icon The icon to be displayed on the button. Can be `null`.
     * @param type The style of the button. Default value is `ButtonStyle.TEXT`.
     */
    @OptIn(ExperimentalResourceApi::class)
    constructor(
        annotatedString: AnnotatedString,
        icon: IconSource? = null,
        type: ButtonStyle = ButtonStyle.TEXT
    ) {
        this.annotatedString = annotatedString
        this.textRes = null
        this.text = null
        this.icon = icon
        this.type = type
    }
}



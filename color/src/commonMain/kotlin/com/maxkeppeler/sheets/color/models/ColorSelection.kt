@file:Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")

package com.maxkeppeler.sheets.color.models

import androidx.compose.runtime.Stable
import com.maxkeppeker.sheets.core.models.base.BaseSelection
import com.maxkeppeker.sheets.core.models.base.SelectionButton
import com.maxkeppeker.sheets.core.utils.BaseConstants

/**
 * The selection configuration for the color dialog.
 * @param selectedColor A color that is selected by default.
 * @param withButtonView If true, the dialog will show a button view.
 * @param extraButton An extra button that can be used for a custom action.
 * @param onExtraButtonClick The listener that is invoked when the extra button is clicked.
 * @param negativeButton The button that will be used as a negative button.
 * @param onNegativeClick The listener that is invoked when the negative button is clicked.
 * @param positiveButton The button that will be used as a positive button.
 * @param onSelectNone The listener that is invoked when no color is selected.
 * @param onSelectColor The listener that returns the selected color.
 */
@Stable
data class ColorSelection(
    val selectedColor: SingleColor? = null,
    override val withButtonView: Boolean = true,
    override val extraButton: SelectionButton? = null,
    override val onExtraButtonClick: (() -> Unit)? = null,
    override val negativeButton: SelectionButton? = BaseConstants.DEFAULT_NEGATIVE_BUTTON,
    override val onNegativeClick: (() -> Unit)? = null,
    override val positiveButton: SelectionButton = BaseConstants.DEFAULT_POSITIVE_BUTTON,
    val onSelectNone: (() -> Unit)? = null,
    val onSelectColor: (color: Int) -> Unit,
) : BaseSelection()
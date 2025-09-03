package com.maxkeppeker.sheets.core.models.base

import com.maxkeppeker.sheets.core.icons.LibIcons
import com.maxkeppeker.sheets.core.utils.BaseConstants

/**
 * Base configs for dialog-specific configs.
 * @param icons The style of icons that are used for dialog/ view-specific icons.
 * @param orientation The orientation of the view or null for auto orientation.
 */
abstract class BaseConfigs(
    open val icons: LibIcons = BaseConstants.DEFAULT_ICON_STYLE,
    open val orientation: LibOrientation? = BaseConstants.DEFAULT_LIB_LAYOUT,
)
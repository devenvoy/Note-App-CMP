package com.maxkeppeker.sheets.core.icons.outlined

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Check: ImageVector
    get() {
        if (_check != null) {
            return _check!!
        }
        _check = materialIcon(name = "Outlined.Check") {
            materialPath {
                moveTo(9.0f, 16.17f)
                lineTo(4.83f, 12.0f)
                lineToRelative(-1.42f, 1.41f)
                lineTo(9.0f, 19.0f)
                lineTo(21.0f, 7.0f)
                lineToRelative(-1.41f, -1.41f)
                lineTo(9.0f, 16.17f)
                close()
            }
        }
        return _check!!
    }

private var _check: ImageVector? = null

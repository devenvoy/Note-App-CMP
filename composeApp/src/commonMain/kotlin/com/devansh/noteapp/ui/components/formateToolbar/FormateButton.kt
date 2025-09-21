package com.devansh.noteapp.ui.components.formateToolbar

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.devansh.noteapp.domain.utils.BooleanCBF

@Composable
fun FormateButton(
    onClick: BooleanCBF,
    icon: ImageVector,
    tint: Color? = null,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val backgroundColor = when {
        !enabled -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        else -> Color.Transparent
    }

    val iconTint = when {
        !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        tint != null -> tint
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onPrimaryContainer
    }

    IconToggleButton(
        checked = isSelected,
        onCheckedChange = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = IconButtonDefaults.iconToggleButtonColors(
            containerColor = Color.Transparent,
            checkedContainerColor = backgroundColor,
            contentColor = tint ?: MaterialTheme.colorScheme.onSurface,
            checkedContentColor = iconTint
        ),
        content = {
            Icon(imageVector = icon, contentDescription = icon.name)
        }
    )
}
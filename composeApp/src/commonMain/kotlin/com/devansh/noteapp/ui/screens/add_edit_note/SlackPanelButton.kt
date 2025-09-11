package com.devansh.noteapp.ui.screens.add_edit_note

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
fun SlackPanelButton(
    onClick: BooleanCBF,
    icon: ImageVector,
    tint: Color? = null,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier,
) {
    IconToggleButton(
        checked = isSelected,
        onCheckedChange = onClick,
        modifier = modifier,
        colors = IconButtonDefaults.iconToggleButtonColors(
            containerColor = Color.Transparent,
            checkedContainerColor = MaterialTheme.colorScheme.primary,
            contentColor = tint ?: MaterialTheme.colorScheme.onSurface,
            checkedContentColor = tint ?: MaterialTheme.colorScheme.onPrimaryContainer
        ),
        content = {
            Icon(imageVector = icon, contentDescription = icon.name)
        }
    )
}
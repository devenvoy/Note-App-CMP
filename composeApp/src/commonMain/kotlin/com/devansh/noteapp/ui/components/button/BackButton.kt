package com.devansh.noteapp.ui.components.button

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devansh.noteapp.domain.utils.UnitCBF

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BackButton(
    showBackText: Boolean = false,
    text: String = "Back",
    toolTipText: String = text,
    onClick: UnitCBF
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Below),
        tooltip = { PlainTooltip { Text(text = toolTipText) } },
        state = rememberTooltipState(),
    ) {
        if (!showBackText) {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                    contentDescription = "back arrow",
                )
            }
        } else {
            TextButton(onClick) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                    contentDescription = text,
                )
                Text(text = text)
            }
        }
    }
}
package com.devansh.noteapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import com.devansh.noteapp.domain.utils.UnitCBF

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BackButton(onClick: UnitCBF) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(),
        tooltip = { PlainTooltip { Text(text = "Back") } },
        state = rememberTooltipState(),
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = "back arrow",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
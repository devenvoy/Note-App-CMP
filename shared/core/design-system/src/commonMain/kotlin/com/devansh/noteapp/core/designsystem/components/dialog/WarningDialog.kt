package com.devansh.noteapp.core.designsystem.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.devansh.noteapp.core.utils.UnitCBF
import noteapp.shared.core.design_system.generated.resources.Res
import noteapp.shared.core.design_system.generated.resources.cancel
import noteapp.shared.core.design_system.generated.resources.ok
import noteapp.shared.core.design_system.generated.resources.warning
import org.jetbrains.compose.resources.stringResource

@Composable
fun WarningDialog(
    message: String,
    onDismissRequest: UnitCBF,
    onConfirm: UnitCBF
) = AlertDialog(
    title = { Text(text = stringResource(Res.string.warning)) },
    text = { Text(text = message) },
    onDismissRequest = onDismissRequest,
    confirmButton = {
        val haptic = LocalHapticFeedback.current
        Button(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                onConfirm()
                onDismissRequest()
            },
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text(text = stringResource(Res.string.ok))
        }
    },
    dismissButton = {
        TextButton(onClick = onDismissRequest) {
            Text(text = stringResource(Res.string.cancel))
        }
    }
)


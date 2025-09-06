package com.devansh.noteapp.ui.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.devansh.noteapp.ui.components.button.TextOptionButton
import note_app_cmp.composeapp.generated.resources.Res
import note_app_cmp.composeapp.generated.resources.export_as
import org.jetbrains.compose.resources.stringResource

enum class ExportType {
    TXT,
    MARKDOWN,
    HTML
}

@Composable
fun ExportDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (ExportType) -> Unit
) = AlertDialog(
    title = {
        Text(text = stringResource(Res.string.export_as))
    },
    text = {
        Column(modifier = Modifier.fillMaxWidth()) {
            TextOptionButton(buttonText = "TXT") {                onConfirm(ExportType.TXT)            }

            TextOptionButton(buttonText = "MARKDOWN") {                onConfirm(ExportType.MARKDOWN)            }

            TextOptionButton(buttonText = "HTML") {                onConfirm(ExportType.HTML)            }
        }
    },
    onDismissRequest = onDismissRequest,
    confirmButton = {}
)

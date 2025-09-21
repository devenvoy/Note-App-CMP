package com.devansh.noteapp.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.devansh.noteapp.domain.utils.UnitCBF
import com.devansh.noteapp.ui.components.button.TextOptionButton
import noteapp.composeapp.generated.resources.Res
import noteapp.composeapp.generated.resources.file
import noteapp.composeapp.generated.resources.image
import noteapp.composeapp.generated.resources.markdown_copy
import noteapp.composeapp.generated.resources.share_note_as
import noteapp.composeapp.generated.resources.text
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

enum class ShareType {
    FILE,
    TEXT,
    COPY,
    IMAGE
}

@Composable
fun ShareDialog(
    isStandard: Boolean,
    onDismissRequest: UnitCBF,
    onConfirm: (ShareType) -> Unit
) = AlertDialog(
    onDismissRequest = onDismissRequest,
    title = {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(Res.string.share_note_as))
            IconButton(
                onClick = {
                    onConfirm(ShareType.COPY)
                }
            ) {
                Icon(painter = painterResource(Res.drawable.markdown_copy), contentDescription = null)
            }
        }
    },
    text = {
        Column(modifier = Modifier.fillMaxWidth()) {
            TextOptionButton(buttonText = stringResource(Res.string.file)) {
                onConfirm(ShareType.FILE)
            }

            TextOptionButton(buttonText = stringResource(Res.string.text)) {
                onConfirm(ShareType.TEXT)
            }

            if (isStandard)
                TextOptionButton(buttonText = stringResource(Res.string.image)) {
                    onConfirm(ShareType.IMAGE)
                }
        }
    },
    confirmButton = {}
)

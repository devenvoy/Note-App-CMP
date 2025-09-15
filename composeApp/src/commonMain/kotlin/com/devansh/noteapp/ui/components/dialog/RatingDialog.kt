package com.devansh.noteapp.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.devansh.noteapp.domain.utils.UnitCBF
import note_app_cmp.composeapp.generated.resources.Res
import note_app_cmp.composeapp.generated.resources.cancel
import note_app_cmp.composeapp.generated.resources.ok
import note_app_cmp.composeapp.generated.resources.rate_this_app
import org.jetbrains.compose.resources.stringResource

@Composable
fun RatingDialog(
    onDismissRequest: UnitCBF,
    onRatingChanged: (Int) -> Unit
) {

    var rating by remember { mutableIntStateOf(0) }

    AlertDialog(
        title = { Text(text = stringResource(Res.string.rate_this_app) + " 💖") },
        text = {
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                for (i in 1..5) {
                    IconButton(onClick = {
                        rating = i
                    }) {
                        Icon(
                            imageVector = Icons.Filled.StarRate,
                            contentDescription = "Star",
                            tint = if (i <= rating) Color.Yellow else MaterialTheme.colorScheme.surfaceDim
                        )
                    }
                }
            }
        },
        onDismissRequest = onDismissRequest,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(Res.string.cancel))
            }
        },
        confirmButton = {
            val haptic = LocalHapticFeedback.current
            Button(onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                onDismissRequest()
                onRatingChanged(rating)
            }) {
                Text(text = stringResource( Res.string.ok))
            }
        },
    )
}

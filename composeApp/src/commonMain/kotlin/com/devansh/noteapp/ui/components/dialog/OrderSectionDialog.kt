package com.devansh.noteapp.ui.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.devansh.noteapp.domain.model.NoteOrder
import com.devansh.noteapp.domain.model.OrderType
import com.devansh.noteapp.domain.utils.UnitCBF
import noteapp.composeapp.generated.resources.Res
import noteapp.composeapp.generated.resources.ascending
import noteapp.composeapp.generated.resources.cancel
import noteapp.composeapp.generated.resources.date
import noteapp.composeapp.generated.resources.descending
import noteapp.composeapp.generated.resources.ok
import noteapp.composeapp.generated.resources.sort_by
import noteapp.composeapp.generated.resources.title
import org.jetbrains.compose.resources.stringResource

@Composable
fun OrderSectionDialog(
    noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    onOrderChange: (NoteOrder) -> Unit,
    onDismiss: UnitCBF
) {

    val hapticFeedback = LocalHapticFeedback.current
    var newOrder by remember { mutableStateOf(noteOrder) }

    val typeOptions = listOf(
        stringResource(Res.string.title),
        stringResource(Res.string.date)
    )

    val orderOptions = listOf(
        stringResource(Res.string.ascending),
        stringResource(Res.string.descending)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(Res.string.sort_by)) },
        text = {
            Column {
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = 0,
                            count = typeOptions.size
                        ),
                        onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentTick)
                            newOrder = NoteOrder.Title(noteOrder.orderType)
                        },
                        selected = newOrder is NoteOrder.Title
                    ) {
                        Text(typeOptions[0])
                    }
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = 1,
                            count = typeOptions.size
                        ),
                        onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentTick)
                            newOrder = NoteOrder.Date(noteOrder.orderType)
                        },
                        selected = newOrder is NoteOrder.Date
                    ) {
                        Text(typeOptions[1])
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = 0,
                            count = typeOptions.size
                        ),
                        onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentTick)
                            newOrder = newOrder.copy(OrderType.Ascending)
                        },
                        selected = newOrder.orderType is OrderType.Ascending
                    ) {
                        Text(orderOptions[0])
                    }
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = 1,
                            count = typeOptions.size
                        ),
                        onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentTick)
                            newOrder = newOrder.copy(OrderType.Descending)
                        },
                        selected = newOrder.orderType is OrderType.Descending
                    ) {
                        Text(orderOptions[1])
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.cancel))
            }
        },
        confirmButton = {
            Button(onClick = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
                onDismiss()
                onOrderChange(newOrder)
            }) {
                Text(stringResource(Res.string.ok))
            }
        }
    )
}

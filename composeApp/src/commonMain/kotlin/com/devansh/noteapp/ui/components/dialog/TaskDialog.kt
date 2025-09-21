package com.devansh.noteapp.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.devansh.noteapp.domain.utils.UnitCBF
import kotlinx.serialization.Serializable
import noteapp.composeapp.generated.resources.Res
import noteapp.composeapp.generated.resources.cancel
import noteapp.composeapp.generated.resources.ok
import noteapp.composeapp.generated.resources.task
import org.jetbrains.compose.resources.stringResource

@Stable
@Serializable
data class TaskItem(
    var task: String = "",
    var checked: Boolean = false
)

@Composable
fun TaskDialog(
    onDismissRequest: UnitCBF,
    onConfirm: (taskList: List<TaskItem>) -> Unit
) {

    val taskList = remember {
        mutableStateListOf(TaskItem("", false))
    }

    AlertDialog(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(Res.string.task))

                FilledTonalIconButton(onClick = {
                    taskList.add(TaskItem("", false))
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "Add Task"
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                taskList.forEachIndexed { index, taskState ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Checkbox(
                            checked = taskState.checked,
                            onCheckedChange = {
                                taskList[index] = taskList[index].copy(checked = it)
                            }
                        )

                        OutlinedTextField(
                            modifier = Modifier.weight(1f),
                            value = taskState.task,
                            onValueChange = {
                                taskList[index] = taskList[index].copy(task = it)
                            },
                            singleLine = true
                        )

                        IconButton(onClick = { taskList.removeAt(index) }) {
                            Icon(
                                Icons.Outlined.RemoveCircleOutline,
                                contentDescription = "Remove Task"
                            )
                        }
                    }
                }
            }
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            val haptic = LocalHapticFeedback.current
            Button(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                    onConfirm(taskList)
                    onDismissRequest()
                }
            ) {
                Text(stringResource(Res.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(Res.string.cancel))
            }
        }
    )
}
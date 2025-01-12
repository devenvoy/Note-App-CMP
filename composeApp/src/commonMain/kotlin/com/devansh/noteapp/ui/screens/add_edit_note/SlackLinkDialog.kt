package com.devansh.noteapp.ui.screens.add_edit_note

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.model.RichTextState

@Composable
fun SlackLinkDialog(
    state: RichTextState,
    openLinkDialog: MutableState<Boolean>,
) {
    var text by remember { mutableStateOf(state.selectedLinkText.orEmpty()) }
    var link by remember { mutableStateOf(state.selectedLinkUrl.orEmpty()) }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Add link",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {
                    openLinkDialog.value = false
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value =
            if (state.selection.collapsed)
                text
            else
                state.annotatedString.text.substring(
                    state.selection.min,
                    state.selection.max
                ),
            onValueChange = {
                text = it
            },
            label = {
                Text(
                    text = "Text",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface
            ),
            enabled = state.selection.collapsed && !state.isLink,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = link,
            onValueChange = {
                link = it
            },
            label = {
                Text(
                    text = "Link",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.End)
        ) {
            if (state.isLink) {
                OutlinedButton(
                    onClick = {
                        state.removeLink()

                        openLinkDialog.value = false
                        text = ""
                        link = ""
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color.Red
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                ) {
                    Text(
                        text = "Remove",
                        color = Color.Red
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))
            }

            OutlinedButton(
                onClick = {
                    openLinkDialog.value = false
                    text = ""
                    link = ""
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
            ) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = {
                    when {
                        state.isLink ->
                            state.updateLink(
                                url = link,
                            )

                        state.selection.collapsed ->
                            state.addLink(
                                text = text,
                                url = link
                            )

                        else ->
                            state.addLinkToSelection(
                                url = link
                            )
                    }

                    openLinkDialog.value = false
                    text = ""
                    link = ""
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF007a5a),
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                enabled = (text.isNotEmpty() || !state.selection.collapsed) && link.isNotEmpty(),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
            ) {
                Text(
                    text = "Save",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
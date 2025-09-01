package com.devansh.noteapp.ui.screens.home.notes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devansh.noteapp.domain.model.Note
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import network.chaintech.sdpcomposemultiplatform.sdp

@Composable
fun NoteItemUI(
    note: Note,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.onSurface
        ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        val content = rememberRichTextState().setHtml(note.content)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(note.colorRes).copy(.8f))
                .padding(8.dp)
        ) {
            Column {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.sdp))

                RichText(
                    state = content,
                    maxLines = 12,
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


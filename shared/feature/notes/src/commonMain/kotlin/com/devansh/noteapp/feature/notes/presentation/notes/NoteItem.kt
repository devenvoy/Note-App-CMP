package com.devansh.noteapp.feature.notes.presentation.notes

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devansh.noteapp.data.models.dto.NoteResponse
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText

@Composable
fun NoteItemUI(
    noteResponse: NoteResponse,
    modifier: Modifier = Modifier,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    textAlign: TextAlign = TextAlign.Unspecified,
    maxLines: Int = 12,
) {
    Card(
        modifier = modifier,
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onSurface.copy(.5f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        val content = rememberRichTextState().setHtml(noteResponse.content)
        val bgColor = noteResponse.colorRes?.let { Color(it) }
            ?: MaterialTheme.colorScheme.surface
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor.copy(.8f))
                .padding(8.dp)
        ) {
            Column {
                Text(
                    text = noteResponse.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = textAlign,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = overflow
                )

                Spacer(modifier = Modifier.height(4.dp))

                RichText(
                    state = content,
                    maxLines = maxLines,
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    overflow = overflow
                )
            }
        }
    }
}


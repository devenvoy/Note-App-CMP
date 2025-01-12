package com.devansh.noteapp.ui.screens.add_edit_note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.model.RichTextState
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.AlignCenter
import compose.icons.fontawesomeicons.solid.AlignLeft
import compose.icons.fontawesomeicons.solid.AlignRight
import compose.icons.fontawesomeicons.solid.Bold
import compose.icons.fontawesomeicons.solid.Circle
import compose.icons.fontawesomeicons.solid.CircleNotch
import compose.icons.fontawesomeicons.solid.Code
import compose.icons.fontawesomeicons.solid.Italic
import compose.icons.fontawesomeicons.solid.Link
import compose.icons.fontawesomeicons.solid.List
import compose.icons.fontawesomeicons.solid.ListOl
import compose.icons.fontawesomeicons.solid.Strikethrough
import compose.icons.fontawesomeicons.solid.TextHeight
import compose.icons.fontawesomeicons.solid.Underline

@Composable
fun SlackPanel(
    state: RichTextState,
    openLinkDialog: MutableState<Boolean>,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
    ) {
        item {
            SlackPanelButton(
                onClick = {
                    state.addParagraphStyle(
                        ParagraphStyle(
                            textAlign = TextAlign.Left,
                        )
                    )
                },
                isSelected = state.currentParagraphStyle.textAlign == TextAlign.Left,
                icon = FontAwesomeIcons.Solid.AlignLeft
            )
        }

        item {
            SlackPanelButton(
                onClick = {
                    state.addParagraphStyle(
                        ParagraphStyle(
                            textAlign = TextAlign.Center
                        )
                    )
                },
                isSelected = state.currentParagraphStyle.textAlign == TextAlign.Center,
                icon = FontAwesomeIcons.Solid.AlignCenter
            )
        }

        item {
            SlackPanelButton(
                onClick = {
                    state.addParagraphStyle(
                        ParagraphStyle(
                            textAlign = TextAlign.Right
                        )
                    )
                },
                isSelected = state.currentParagraphStyle.textAlign == TextAlign.Right,
                icon = FontAwesomeIcons.Solid.AlignRight
            )
        }

        item {
            Box(
                Modifier
                    .height(24.dp)
                    .width(1.dp)
                    .background(Color(0xFF393B3D))
            )
        }

        item {
            SlackPanelButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                isSelected = state.currentSpanStyle.fontWeight == FontWeight.Bold,
                icon = FontAwesomeIcons.Solid.Bold
            )
        }

        item {
            SlackPanelButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            fontStyle = FontStyle.Italic
                        )
                    )
                },
                isSelected = state.currentSpanStyle.fontStyle == FontStyle.Italic,
                icon = FontAwesomeIcons.Solid.Italic
            )
        }

        item {
            SlackPanelButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            textDecoration = TextDecoration.Underline
                        )
                    )
                },
                isSelected = state.currentSpanStyle.textDecoration?.contains(TextDecoration.Underline) == true,
                icon = FontAwesomeIcons.Solid.Underline
            )
        }

        item {
            SlackPanelButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            textDecoration = TextDecoration.LineThrough
                        )
                    )
                },
                isSelected = state.currentSpanStyle.textDecoration?.contains(TextDecoration.LineThrough) == true,
                icon = FontAwesomeIcons.Solid.Strikethrough
            )
        }

        item {
            Box(
                Modifier
                    .height(24.dp)
                    .width(1.dp)
                    .background(Color(0xFF393B3D))
            )
        }

        item {
            SlackPanelButton(
                onClick = {
                    openLinkDialog.value = true
                },
                isSelected = state.isLink,
                icon = FontAwesomeIcons.Solid.Link
            )
        }

        item {
            Box(
                Modifier
                    .height(24.dp)
                    .width(1.dp)
                    .background(Color(0xFF393B3D))
            )
        }

        item {
            SlackPanelButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            fontSize = 28.sp
                        )
                    )
                },
                isSelected = state.currentSpanStyle.fontSize == 28.sp,
                icon = FontAwesomeIcons.Solid.TextHeight
            )
        }

        item {
            SlackPanelButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            color = Color.Red
                        )
                    )
                },
                isSelected = state.currentSpanStyle.color == Color.Red,
                icon = FontAwesomeIcons.Solid.Circle,
                tint = Color.Red
            )
        }

        item {
            SlackPanelButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            background = Color.Yellow
                        )
                    )
                },
                isSelected = state.currentSpanStyle.background == Color.Yellow,
                icon = FontAwesomeIcons.Solid.CircleNotch,
                tint = Color.Yellow
            )
        }

        item {
            Box(
                Modifier
                    .height(24.dp)
                    .width(1.dp)
                    .background(Color(0xFF393B3D))
            )
        }

        item {
            SlackPanelButton(
                onClick = {
                    state.toggleUnorderedList()
                },
                isSelected = state.isUnorderedList,
                icon = FontAwesomeIcons.Solid.List,
            )
        }

        item {
            SlackPanelButton(
                onClick = {
                    state.toggleOrderedList()
                },
                isSelected = state.isOrderedList,
                icon = FontAwesomeIcons.Solid.ListOl,
            )
        }

        item {
            Box(
                Modifier
                    .height(24.dp)
                    .width(1.dp)
                    .background(Color(0xFF393B3D))
            )
        }

        item {
            SlackPanelButton(
                onClick = {
                    state.toggleCodeSpan()
                },
                isSelected = state.isCodeSpan,
                icon = FontAwesomeIcons.Solid.Code,
            )
        }
    }
}
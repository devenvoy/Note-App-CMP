package com.devansh.noteapp.ui.screens.add_edit_note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devansh.noteapp.ui.screens.core.textColors
import com.devansh.noteapp.ui.theme.CrimsonRed
import com.materialkolor.ktx.toHex
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.color.ColorDialog
import com.maxkeppeler.sheets.color.models.ColorConfig
import com.maxkeppeler.sheets.color.models.ColorSelection
import com.maxkeppeler.sheets.color.models.ColorSelectionMode
import com.maxkeppeler.sheets.color.models.MultipleColors
import com.maxkeppeler.sheets.color.models.SingleColor
import com.mohamedrejeb.richeditor.model.RichTextState
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.AlignCenter
import compose.icons.fontawesomeicons.solid.AlignLeft
import compose.icons.fontawesomeicons.solid.AlignRight
import compose.icons.fontawesomeicons.solid.Bold
import compose.icons.fontawesomeicons.solid.Brush
import compose.icons.fontawesomeicons.solid.Circle
import compose.icons.fontawesomeicons.solid.CircleNotch
import compose.icons.fontawesomeicons.solid.Code
import compose.icons.fontawesomeicons.solid.Highlighter
import compose.icons.fontawesomeicons.solid.Italic
import compose.icons.fontawesomeicons.solid.Link
import compose.icons.fontawesomeicons.solid.List
import compose.icons.fontawesomeicons.solid.ListOl
import compose.icons.fontawesomeicons.solid.Strikethrough
import compose.icons.fontawesomeicons.solid.TextHeight
import compose.icons.fontawesomeicons.solid.Underline

@OptIn(
    ExperimentalLayoutApi::class, ExperimentalMaterial3WindowSizeClassApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun SlackPanel(
    state: RichTextState,
    openLinkDialog: MutableState<Boolean>,
    modifier: Modifier = Modifier,
) {

    var selectedTextColor by remember { mutableStateOf(CrimsonRed.toArgb()) }

    val colorState = rememberUseCaseState(onFinishedRequest = {
        state.toggleSpanStyle(
            SpanStyle(color = Color(selectedTextColor))
        )
    })

    val templateColors = MultipleColors.ColorsInt(*textColors.map { it.toArgb() }.toIntArray())

    ColorDialog(
        state = colorState,
        selection = ColorSelection(
            selectedColor = SingleColor(selectedTextColor),
            onSelectColor = { selectedTextColor = it },
        ),
        config = ColorConfig(
            displayMode = ColorSelectionMode.TEMPLATE,
            templateColors = templateColors,
        )
    )

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(2.dp), modifier = modifier
    ) {

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

        Box(
            Modifier.height(24.dp).width(1.dp).background(Color(0xFF393B3D))
        )

        SlackPanelButton(
            onClick = {
                openLinkDialog.value = true
            }, isSelected = state.isLink, icon = FontAwesomeIcons.Solid.Link
        )

        SlackPanelButton(
            onClick = { state.toggleCodeSpan() },
            isSelected = state.isCodeSpan,
            icon = FontAwesomeIcons.Solid.Code,
        )

        Box(
            Modifier.height(24.dp).width(1.dp).background(Color(0xFF393B3D))
        )

        SlackPanelButton(
            onClick = {
                state.toggleSpanStyle(SpanStyle(fontSize = 28.sp))
            },
            isSelected = state.currentSpanStyle.fontSize == 28.sp,
            icon = FontAwesomeIcons.Solid.TextHeight
        )

        SlackPanelButton(
            onClick = {
                if (state.currentSpanStyle.color != Color(selectedTextColor)) {
                    colorState.show()
                } else {
                    state.toggleSpanStyle(
                        SpanStyle(color = Color(selectedTextColor))
                    )
                }
            },
            isSelected = state.currentSpanStyle.color == Color(selectedTextColor),
            icon = FontAwesomeIcons.Solid.Circle,
            tint = Color(selectedTextColor)
        )

        SlackPanelButton(
            onClick = {
                state.toggleSpanStyle(
                    SpanStyle(background = Color.Yellow)
                )
            },
            isSelected = state.currentSpanStyle.background == Color.Yellow,
            icon = FontAwesomeIcons.Solid.Highlighter,
            tint = Color.Yellow
        )

        val windowSizeClass = calculateWindowSizeClass()

        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                Box(modifier = Modifier.fillMaxWidth())
            }

            else -> {
                Box(
                    Modifier.height(24.dp).width(1.dp).background(Color(0xFF393B3D))
                )
            }
        }

        SlackPanelButton(
            onClick = {
                state.addParagraphStyle(
                    ParagraphStyle(textAlign = TextAlign.Left)
                )
            },
            isSelected = state.currentParagraphStyle.textAlign == TextAlign.Left,
            icon = FontAwesomeIcons.Solid.AlignLeft
        )

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

        Box(
            Modifier.height(24.dp).width(1.dp).background(Color(0xFF393B3D))
        )


        SlackPanelButton(
            onClick = {
                state.toggleUnorderedList()
            },
            isSelected = state.isUnorderedList,
            icon = FontAwesomeIcons.Solid.List,
        )

        SlackPanelButton(
            onClick = {
                state.toggleOrderedList()
            },
            isSelected = state.isOrderedList,
            icon = FontAwesomeIcons.Solid.ListOl,
        )

    }
}
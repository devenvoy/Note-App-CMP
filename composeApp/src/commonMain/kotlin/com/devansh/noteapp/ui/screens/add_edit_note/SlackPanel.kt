package com.devansh.noteapp.ui.screens.add_edit_note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.AlignHorizontalCenter
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.FormatStrikethrough
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.Highlight
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
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
import androidx.window.core.layout.WindowWidthSizeClass
import com.devansh.noteapp.ui.screens.core.textColors
import com.devansh.noteapp.ui.theme.CrimsonRed
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.color.ColorDialog
import com.maxkeppeler.sheets.color.models.ColorConfig
import com.maxkeppeler.sheets.color.models.ColorSelection
import com.maxkeppeler.sheets.color.models.ColorSelectionMode
import com.maxkeppeler.sheets.color.models.MultipleColors
import com.maxkeppeler.sheets.color.models.SingleColor
import com.mohamedrejeb.richeditor.model.RichTextState

@OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun SlackPanel(
    state: RichTextState,
    openLinkDialog: MutableState<Boolean>,
    modifier: Modifier = Modifier,
) {

    var selectedTextColor by remember { mutableStateOf(CrimsonRed.toArgb()) }
    val windowSizeClass = currentWindowAdaptiveInfo()

    val colorState = rememberUseCaseState(
        onFinishedRequest = { state.toggleSpanStyle(SpanStyle(color = Color(selectedTextColor))) })

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
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        SlackPanelButton(
            onClick = { state.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold)) },
            isSelected = state.currentSpanStyle.fontWeight == FontWeight.Bold,
            icon = Icons.Filled.FormatBold
        )

        SlackPanelButton(
            onClick = { state.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic)) },
            isSelected = state.currentSpanStyle.fontStyle == FontStyle.Italic,
            icon = Icons.Filled.FormatItalic
        )

        SlackPanelButton(
            onClick = { state.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline)) },
            isSelected = state.currentSpanStyle.textDecoration?.contains(TextDecoration.Underline) == true,
            icon = Icons.Filled.FormatUnderlined
        )

        SlackPanelButton(
            onClick = { state.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) },
            isSelected = state.currentSpanStyle.textDecoration?.contains(TextDecoration.LineThrough) == true,
            icon = Icons.Filled.FormatStrikethrough
        )

        Box(
            Modifier.height(24.dp).width(1.dp).background(Color(0xFF393B3D))
        )

        SlackPanelButton(
            onClick = { openLinkDialog.value = true },
            isSelected = state.isLink,
            icon = Icons.Filled.Link
        )

        SlackPanelButton(
            onClick = { state.toggleCodeSpan() },
            isSelected = state.isCodeSpan,
            icon = Icons.Filled.Code,
        )

        Box(
            Modifier.height(24.dp).width(1.dp).background(Color(0xFF393B3D))
        )

        SlackPanelButton(
            onClick = { state.toggleSpanStyle(SpanStyle(fontSize = 28.sp)) },
            isSelected = state.currentSpanStyle.fontSize == 28.sp,
            icon = Icons.Filled.FormatSize
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
            icon = Icons.Filled.Circle,
            tint = Color(selectedTextColor)
        )

        SlackPanelButton(
            onClick = { state.toggleSpanStyle(SpanStyle(background = Color.Yellow)) },
            isSelected = state.currentSpanStyle.background == Color.Yellow,
            icon = Icons.Filled.Highlight,
            tint = Color.Yellow
        )


        when (windowSizeClass.windowSizeClass.windowWidthSizeClass) {
            WindowWidthSizeClass.COMPACT -> Box(modifier = Modifier.fillMaxWidth())
            else -> Box(
                Modifier.height(24.dp).width(1.dp).background(Color(0xFF393B3D))
            )
        }

        SlackPanelButton(
            onClick = { state.addParagraphStyle(ParagraphStyle(textAlign = TextAlign.Left)) },
            isSelected = state.currentParagraphStyle.textAlign == TextAlign.Left,
            icon = Icons.AutoMirrored.Filled.FormatAlignLeft
        )

        SlackPanelButton(
            onClick = { state.addParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center)) },
            isSelected = state.currentParagraphStyle.textAlign == TextAlign.Center,
            icon = Icons.Filled.AlignHorizontalCenter
        )

        SlackPanelButton(
            onClick = { state.addParagraphStyle(ParagraphStyle(textAlign = TextAlign.Right)) },
            isSelected = state.currentParagraphStyle.textAlign == TextAlign.Right,
            icon = Icons.AutoMirrored.Filled.FormatAlignRight
        )

        Box(Modifier.height(24.dp).width(1.dp).background(Color(0xFF393B3D)))


        SlackPanelButton(
            onClick = { state.toggleUnorderedList() },
            isSelected = state.isUnorderedList,
            icon = Icons.AutoMirrored.Filled.FormatListBulleted
        )

        SlackPanelButton(
            onClick = { state.toggleOrderedList() },
            isSelected = state.isOrderedList,
            icon = Icons.Filled.FormatListNumbered,
        )
    }
}

package com.devansh.noteapp.ui.screens.add_edit_note

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.automirrored.filled.FormatIndentDecrease
import androidx.compose.material.icons.automirrored.filled.FormatIndentIncrease
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
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devansh.noteapp.ui.screens.core.textColors
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.color.ColorDialog
import com.maxkeppeler.sheets.color.models.ColorConfig
import com.maxkeppeler.sheets.color.models.ColorSelection
import com.maxkeppeler.sheets.color.models.ColorSelectionMode
import com.maxkeppeler.sheets.color.models.MultipleColors
import com.maxkeppeler.sheets.color.models.SingleColor
import com.mohamedrejeb.richeditor.model.RichTextState

data class TypographyStyle(
    val name: String,
    val fontSize: Int,
    val fontWeight: FontWeight,
    val description: String
)

val typographyStyles = listOf(
    TypographyStyle("Title", 32, FontWeight.Bold, "Large title text"),
    TypographyStyle("Subtitle", 28, FontWeight.SemiBold, "Subtitle text"),
    TypographyStyle("Headline 1", 24, FontWeight.Bold, "Main heading"),
    TypographyStyle("Headline 2", 20, FontWeight.SemiBold, "Sub heading"),
    TypographyStyle("Headline 3", 18, FontWeight.Medium, "Section heading"),
    TypographyStyle("Body Large", 16, FontWeight.Normal, "Large body text"),
    TypographyStyle("Body", 14, FontWeight.Normal, "Regular body text"),
    TypographyStyle("Caption", 12, FontWeight.Normal, "Small caption text")
)

@OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun SlackPanel(
    state: RichTextState,
    openLinkDialog: MutableState<Boolean>,
    modifier: Modifier = Modifier,
) {

    var selectedTextColor by remember { mutableStateOf(Color.White.toArgb()) }
    var showColorPalette by remember { mutableStateOf(false) }
    var showTypographyMenu by remember { mutableStateOf(false) }
    var currentIndentLevel by remember { mutableStateOf(0) }

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

    Column(
        modifier = Modifier.imePadding()
            .then(modifier)
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Typography Selector
            Box {
                SlackPanelButton(
                    onClick = { showTypographyMenu = !showTypographyMenu },
                    isSelected = false,
                    icon = Icons.Filled.TextFields
                )

                DropdownMenu(
                    expanded = showTypographyMenu,
                    onDismissRequest = { showTypographyMenu = false },
                    modifier = Modifier.width(200.dp)
                ) {
                    typographyStyles.forEach { style ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = style.name,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = (style.fontSize * 0.7f).sp,
                                            fontWeight = style.fontWeight
                                        )
                                    )
                                    Text(
                                        text = style.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            onClick = {
                                state.toggleSpanStyle(
                                    SpanStyle(
                                        fontSize = style.fontSize.sp,
                                        fontWeight = style.fontWeight
                                    )
                                )
                                showTypographyMenu = false
                            }
                        )
                    }
                }
            }

            // Color Palette Selector
            Box {
                SlackPanelButton(
                    onClick = { _ -> showColorPalette = !showColorPalette },
                    isSelected = showColorPalette,
                    icon = Icons.Filled.Palette,
                    tint = Color(selectedTextColor)
                )

                if (showColorPalette) {
                    Card(
                        modifier = Modifier.padding(top = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        ColorPalette(
                            colors = textColors,
                            selectedColor = Color(selectedTextColor),
                            onColorSelected = { color ->
                                selectedTextColor = color.toArgb()
                                state.toggleSpanStyle(SpanStyle(color = color))
                                showColorPalette = false
                            },
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Basic Formatting
            item {
                SlackPanelButton(
                    onClick = { state.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold)) },
                    isSelected = state.currentSpanStyle.fontWeight == FontWeight.Bold,
                    icon = Icons.Filled.FormatBold
                )
            }

            item {
                SlackPanelButton(
                    onClick = { state.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic)) },
                    isSelected = state.currentSpanStyle.fontStyle == FontStyle.Italic,
                    icon = Icons.Filled.FormatItalic
                )
            }

            item {
                SlackPanelButton(
                    onClick = { state.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline)) },
                    isSelected = state.currentSpanStyle.textDecoration?.contains(TextDecoration.Underline) == true,
                    icon = Icons.Filled.FormatUnderlined
                )
            }

            item {
                SlackPanelButton(
                    onClick = { state.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) },
                    isSelected = state.currentSpanStyle.textDecoration?.contains(TextDecoration.LineThrough) == true,
                    icon = Icons.Filled.FormatStrikethrough
                )
            }

            item {
                SlackPanelButton(
                    onClick = { state.toggleSpanStyle(SpanStyle(background = Color.Yellow)) },
                    isSelected = state.currentSpanStyle.background == Color.Yellow,
                    icon = Icons.Filled.Highlight,
                    tint = Color.Yellow
                )
            }

            // Divider
            item {
                Box(
                    Modifier.height(28.dp).width(2.dp)
                        .background(MaterialTheme.colorScheme.onBackground)
                )
            }

            // Special Formatting

            item {
                SlackPanelButton(
                    onClick = { openLinkDialog.value = true },
                    isSelected = state.isLink,
                    icon = Icons.Filled.Link
                )
            }

            item {
                SlackPanelButton(
                    onClick = { state.toggleCodeSpan() },
                    isSelected = state.isCodeSpan,
                    icon = Icons.Filled.Code,
                )
            }

            // Divider
            item {
                Box(
                    Modifier.height(28.dp).width(2.dp)
                        .background(MaterialTheme.colorScheme.onBackground)
                )
            }

            item {
                SlackPanelButton(
                    onClick = { state.addParagraphStyle(ParagraphStyle(textAlign = TextAlign.Left)) },
                    isSelected = state.currentParagraphStyle.textAlign == TextAlign.Left,
                    icon = Icons.AutoMirrored.Filled.FormatAlignLeft
                )
            }

            item {
                SlackPanelButton(
                    onClick = { state.addParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center)) },
                    isSelected = state.currentParagraphStyle.textAlign == TextAlign.Center,
                    icon = Icons.Filled.AlignHorizontalCenter
                )
            }

            item {
                SlackPanelButton(
                    onClick = { state.addParagraphStyle(ParagraphStyle(textAlign = TextAlign.Right)) },
                    isSelected = state.currentParagraphStyle.textAlign == TextAlign.Right,
                    icon = Icons.AutoMirrored.Filled.FormatAlignRight
                )
            }

            item {
                Box(
                    Modifier.height(24.dp).width(1.dp)
                        .background(MaterialTheme.colorScheme.outline)
                )
            }

            // Indentation
            item {
                SlackPanelButton(
                    onClick = {
                        if (currentIndentLevel > 0) {
                            currentIndentLevel--
                            state.addParagraphStyle(
                                ParagraphStyle(
                                    textIndent = TextIndent(firstLine = (currentIndentLevel * 24).sp)
                                )
                            )
                        }
                    },
                    isSelected = false,
                    icon = Icons.AutoMirrored.Filled.FormatIndentDecrease,
                    enabled = currentIndentLevel > 0
                )
            }

            item {
                SlackPanelButton(
                    onClick = {
                        if (currentIndentLevel < 5) {
                            currentIndentLevel++
                            state.addParagraphStyle(
                                ParagraphStyle(
                                    textIndent = TextIndent(firstLine = (currentIndentLevel * 24).sp)
                                )
                            )
                        }
                    },
                    isSelected = false,
                    icon = Icons.AutoMirrored.Filled.FormatIndentIncrease,
                    enabled = currentIndentLevel < 5
                )
            }

            item {
                Modifier.height(28.dp).width(2.dp)
                    .background(MaterialTheme.colorScheme.outline)
            }

            item {
                SlackPanelButton(
                    onClick = { state.toggleUnorderedList() },
                    isSelected = state.isUnorderedList,
                    icon = Icons.AutoMirrored.Filled.FormatListBulleted
                )
            }
            item {
                SlackPanelButton(
                    onClick = { state.toggleOrderedList() },
                    isSelected = state.isOrderedList,
                    icon = Icons.Filled.FormatListNumbered,
                )
            }
        }
    }
}


@Composable
private fun ColorPalette(
    colors: List<Color>,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Text Colors",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Color grid
        val chunkedColors = colors.chunked(6)
        chunkedColors.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                row.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (color == selectedColor) 3.dp else 1.dp,
                                color = if (color == selectedColor) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.outline
                                },
                                shape = CircleShape
                            )
                            .clickable { onColorSelected(color) }
                    )
                }
            }
        }

        // Add a "More Colors" option
        Text(
            text = "More colors...",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable { /* Open full color picker */ }
                .padding(top = 8.dp)
        )
    }
}
package com.devansh.noteapp.ui.components.formateToolbar

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListNumbered
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.devansh.noteapp.core.util.DeviceConfiguration
import com.devansh.noteapp.ui.components.formateToolbar.TypographyStyle.Companion.typographyStyles
import com.devansh.noteapp.ui.screens.core.textColors
import com.mohamedrejeb.richeditor.model.RichTextState

@OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun FormattingToolBar(
    state: RichTextState,
    openLinkDialog: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    deviceInfo: DeviceConfiguration,
) {

    var selectedTextColor by remember { mutableStateOf(Color.White.toArgb()) }
    var showColorPalette by remember { mutableStateOf(false) }
    var showTypographyMenu by remember { mutableStateOf(false) }
    var currentIndentLevel by remember { mutableStateOf(0) }

    var paletteCords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var paletteIconPosition by remember { mutableStateOf(IntOffset.Zero) }


    if (showColorPalette && paletteCords != null) {
        val posInRoot = paletteCords!!.positionInWindow()
        ColorPopUp(
            selectedTextColor = selectedTextColor,
            onDismissRequest = { showColorPalette = false },
            onColorSelected = { color ->
                selectedTextColor = color.toArgb()
                state.toggleSpanStyle(SpanStyle(color = color))
                showColorPalette = false
            },
            intOffSet = IntOffset(posInRoot.x.toInt() - 100, posInRoot.y.toInt() - 200),
            paletteIconPosition = paletteIconPosition
        )
    }

    Column(
        modifier = Modifier.imePadding().then(modifier),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Basic Formatting
            item {
                Box {
                    FormateButton(
                        onClick = { showTypographyMenu = !showTypographyMenu },
                        isSelected = false,
                        icon = Icons.Filled.TextFields,
                        modifier = Modifier
                    )

                    DropdownMenu(
                        expanded = showTypographyMenu,
                        onDismissRequest = { showTypographyMenu = false },
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        typographyStyles.forEach { style ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(
                                            text = style.name,
                                            color = if (state.currentSpanStyle.fontSize == style.fontSize) MaterialTheme.colorScheme.primary
                                            else Color.Unspecified,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontSize = style.fontSize,
                                                fontWeight = style.fontWeight
                                            )
                                        )
                                    }
                                },
                                onClick = {
                                    state.toggleSpanStyle(
                                        SpanStyle(
                                            fontSize = style.fontSize,
                                            fontWeight = style.fontWeight
                                        )
                                    )
                                    showTypographyMenu = false
                                }
                            )
                        }
                    }
                }
            }

            item {
                FormateButton(
                    onClick = { state.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold)) },
                    isSelected = state.currentSpanStyle.fontWeight == FontWeight.Bold,
                    icon = Icons.Filled.FormatBold
                )
            }

            item {
                FormateButton(
                    onClick = { state.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic)) },
                    isSelected = state.currentSpanStyle.fontStyle == FontStyle.Italic,
                    icon = Icons.Filled.FormatItalic
                )
            }

            item {
                FormateButton(
                    onClick = { state.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline)) },
                    isSelected = state.currentSpanStyle.textDecoration?.contains(TextDecoration.Underline) == true,
                    icon = Icons.Filled.FormatUnderlined
                )
            }

            item {
                FormateButton(
                    onClick = { state.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) },
                    isSelected = state.currentSpanStyle.textDecoration?.contains(TextDecoration.LineThrough) == true,
                    icon = Icons.Filled.FormatStrikethrough
                )
            }

            item {
                FormateButton(
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

            item {
                FormateButton(
                    onClick = { _ -> showColorPalette = !showColorPalette },
                    isSelected = showColorPalette,
                    icon = Icons.Filled.Palette,
                    tint = Color(selectedTextColor),
                    modifier = Modifier.onGloballyPositioned {
                        paletteCords = it
                        val posInWindow = it.positionInWindow()
                        paletteIconPosition = IntOffset(
                            x = (posInWindow.x + it.size.width / 2).toInt(),
                            y = (posInWindow.y + it.size.height / 2).toInt()
                        )
                    }
                )
            }

            item {
                FormateButton(
                    onClick = { openLinkDialog.value = true },
                    isSelected = state.isLink,
                    icon = Icons.Filled.Link
                )
            }

            item {
                FormateButton(
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
                FormateButton(
                    onClick = { state.addParagraphStyle(ParagraphStyle(textAlign = TextAlign.Left)) },
                    isSelected = state.currentParagraphStyle.textAlign == TextAlign.Left,
                    icon = Icons.AutoMirrored.Filled.FormatAlignLeft
                )
            }

            item {
                FormateButton(
                    onClick = { state.addParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center)) },
                    isSelected = state.currentParagraphStyle.textAlign == TextAlign.Center,
                    icon = Icons.Filled.AlignHorizontalCenter
                )
            }

            item {
                FormateButton(
                    onClick = { state.addParagraphStyle(ParagraphStyle(textAlign = TextAlign.Right)) },
                    isSelected = state.currentParagraphStyle.textAlign == TextAlign.Right,
                    icon = Icons.AutoMirrored.Filled.FormatAlignRight
                )
            }

            item {
                Box(
                    Modifier.height(28.dp).width(2.dp)
                        .background(MaterialTheme.colorScheme.onBackground)
                )
            }

            // Indentation
            item {
                FormateButton(
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
                FormateButton(
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
                FormateButton(
                    onClick = { state.toggleUnorderedList() },
                    isSelected = state.isUnorderedList,
                    icon = Icons.AutoMirrored.Filled.FormatListBulleted
                )
            }
            item {
                FormateButton(
                    onClick = { state.toggleOrderedList() },
                    isSelected = state.isOrderedList,
                    icon = Icons.Filled.FormatListNumbered,
                )
            }
        }
    }
}

@Composable
fun ColorPopUp(
    selectedTextColor: Int,
    onDismissRequest: (() -> Unit)?,
    onColorSelected: (Color) -> Unit,
    intOffSet: IntOffset,
    paletteIconPosition: IntOffset
) {
    var cardWidth by remember { mutableStateOf(0) }
    Popup(
        onDismissRequest = onDismissRequest,
        offset = intOffSet
    ) {
        val color = MaterialTheme.colorScheme.surfaceContainer
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.onGloballyPositioned {
                cardWidth = it.size.width
            }
        ) {
            Card(
                modifier = Modifier.padding(top = 4.dp),
                colors = CardDefaults.cardColors(containerColor = color),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                ColorPalette(
                    colors = textColors,
                    selectedColor = Color(selectedTextColor),
                    onColorSelected = onColorSelected,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Canvas(modifier = Modifier.width(cardWidth.dp).height(20.dp)) {
                if (size.width > 0) {
                    val iconRelativeX = (paletteIconPosition.x - intOffSet.x).toFloat()

                    val minPadding = 15f
                    val maxPadding = 15f

                    val minBound = minPadding
                    val maxBound = maxOf(size.width - maxPadding, minBound + 1f) // Ensure max > min

                    val pointerX = if (iconRelativeX < minBound) {
                        minBound
                    } else if (iconRelativeX > maxBound) {
                        maxBound
                    } else {
                        iconRelativeX
                    }

                    val path = Path().apply {
                        moveTo(pointerX - 8f, 0f)
                        lineTo(pointerX, 15f)
                        lineTo(pointerX + 8f, 0f)
                        close()
                    }
                    drawPath(path, color)
                }
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
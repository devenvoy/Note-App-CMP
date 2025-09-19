package com.devansh.noteapp.ui.screens.add_edit_note

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
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.devansh.noteapp.core.util.DeviceConfiguration
import com.devansh.noteapp.ui.screens.core.textColors
import com.mohamedrejeb.richeditor.model.RichTextState

data class TypographyStyle(
    val name: String,
    val fontSize: TextUnit,
    val fontWeight: FontWeight,
    val description: String
)

val typographyStyles = listOf(
    TypographyStyle("Title", 32.sp, FontWeight.Bold, "Large title text"),
    TypographyStyle("Subtitle", 28.sp, FontWeight.SemiBold, "Subtitle text"),
    TypographyStyle("Headline 1", 24.sp, FontWeight.Bold, "Main heading"),
    TypographyStyle("Headline 2", 20.sp, FontWeight.SemiBold, "Sub heading"),
    TypographyStyle("Headline 3", 18.sp, FontWeight.Medium, "Section heading"),
    TypographyStyle("Body Large", 16.sp, FontWeight.Normal, "Large body text"),
    TypographyStyle("Body", 14.sp, FontWeight.Normal, "Regular body text"),
    TypographyStyle("Caption", 12.sp, FontWeight.Normal, "Small caption text")
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

    val windowInfo = currentWindowAdaptiveInfo()
    val deviceInfo = DeviceConfiguration.fromWindowSizeClass(windowInfo.windowSizeClass)
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
            intOffSet = IntOffset(
                ((posInRoot.x.toInt()) / 2),
                (posInRoot.y.toInt() - if (deviceInfo.isExpanded()) 220 else 550)
            ),
            paletteIconPosition = paletteIconPosition
        )
    }

    Column(
        modifier = Modifier.imePadding()
            .then(modifier)
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Basic Formatting
            item {
                Box {
                    SlackPanelButton(
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

            item {
                SlackPanelButton(
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
fun ColorPopUp(
    selectedTextColor: Int,
    onDismissRequest: (() -> Unit)?,
    onColorSelected: (Color) -> Unit,
    intOffSet: IntOffset,
    paletteIconPosition: IntOffset
) {
    var cardWidth by remember { mutableStateOf(0) }
    /*
    val configuration = LocalWindowInfo.current.containerSize
    val screenWidth = configuration.width
    val windowInfo = currentWindowAdaptiveInfo()
    val deviceConfig = DeviceConfiguration.fromWindowSizeClass(windowInfo.windowSizeClass)

    val adaptiveOffset = remember(intOffSet, paletteIconPosition, screenWidth) {
        when {
            deviceConfig.isMobile() -> {
                IntOffset(
                    x = (screenWidth / 2 - 120),
                    y = intOffSet.y - 250
                )
            }
            deviceConfig.isMobile() -> {
                IntOffset(
                    x = (paletteIconPosition.x - 140).coerceIn(20, screenWidth - 280),
                    y = intOffSet.y - 230
                )
            }
            else -> {
                IntOffset(
                    x = (paletteIconPosition.x - 120).coerceIn(20, screenWidth - 240),
                    y = intOffSet.y - 220
                )
            }
        }
    }
*/


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

            /*when {
                deviceConfig.isMobile() -> {
                    Canvas(modifier = Modifier.width(cardWidth.dp).height(16.dp)) {
                        if (size.width > 0) {
                            val centerX = size.width / 2f
                            val path = Path().apply {
                                moveTo(centerX - 10f, 0f)
                                lineTo(centerX, 12f)
                                lineTo(centerX + 10f, 0f)
                                close()
                            }
                            drawPath(path, color)
                        }
                    }
                }

                else -> {
                    Canvas(modifier = Modifier.width(cardWidth.dp).height(20.dp)) {
                        if (size.width > 0) {
                            val iconRelativeX = (paletteIconPosition.x - adaptiveOffset.x).toFloat()
                            val minBound = 15f
                            val maxBound = maxOf(size.width - 15f, minBound + 1f)

                            val pointerX = iconRelativeX.coerceIn(minBound, maxBound)

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
            }*/
            Canvas(modifier = Modifier.width(cardWidth.dp).height(20.dp)) {
                if (size.width > 0) { // Safety check to ensure canvas has valid size
                    // Calculate where the palette icon is relative to the popup
                    val iconRelativeX = (paletteIconPosition.x - intOffSet.x).toFloat()

                    // Define safe bounds with minimum padding
                    val minPadding = 15f
                    val maxPadding = 15f

                    // Ensure minimum bounds are valid (min < max)
                    val minBound = minPadding
                    val maxBound = maxOf(size.width - maxPadding, minBound + 1f) // Ensure max > min

                    // Clamp the pointer position to stay within safe bounds
                    val pointerX = if (iconRelativeX < minBound) {
                        minBound
                    } else if (iconRelativeX > maxBound) {
                        maxBound
                    } else {
                        iconRelativeX
                    }

                    val path = Path().apply {
                        moveTo(pointerX - 8f, 0f)   // Left point of triangle (smaller triangle)
                        lineTo(pointerX, 15f)       // Bottom point (tip pointing to icon)
                        lineTo(pointerX + 8f, 0f)   // Right point of triangle
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
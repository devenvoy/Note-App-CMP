package com.devansh.noteapp.ui.screens.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FormatAlignLeft
import androidx.compose.material.icons.automirrored.outlined.FormatAlignRight
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.outlined.FormatAlignCenter
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.SettingsSwitch
import com.devansh.noteapp.navigation.NavRoute
import com.devansh.noteapp.ui.components.button.BackButton
import com.devansh.noteapp.ui.components.button.PrimaryButton
import com.devansh.noteapp.ui.screens.core.ListType
import com.devansh.noteapp.ui.utils.ListItemShapes.firstIndexShape
import com.devansh.noteapp.ui.utils.ListItemShapes.lastIndexShape
import com.devansh.noteapp.ui.utils.ListItemShapes.middleIndexShape
import com.devansh.noteapp.ui.utils.ListItemShapes.singleItemIndex
import note_app_cmp.composeapp.generated.resources.Res
import note_app_cmp.composeapp.generated.resources.center
import note_app_cmp.composeapp.generated.resources.left
import note_app_cmp.composeapp.generated.resources.right
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

fun NavGraphBuilder.settingsScreen(navHostController: NavHostController) {
    composable<NavRoute.Setting> {
        val viewModel = koinViewModel<SettingViewModel>()
        SettingScreenContent(
            viewModel = viewModel,
            navigateBack = { navHostController.navigateUp() },
            logOut = {
                viewModel.logOut()
                navHostController.navigate(NavRoute.Auth) {
                    popUpTo(NavRoute.BaseScreen) { inclusive = true }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreenContent(
    viewModel: SettingViewModel,
    navigateBack: () -> Unit,
    logOut: () -> Unit,
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)

    val hapticFeedback = LocalHapticFeedback.current
    val autoSync by viewModel.autoSyncDB.collectAsStateWithLifecycle()
    val listType by viewModel.listType.collectAsStateWithLifecycle()
    val settingState by viewModel.settingsStateFlow.collectAsStateWithLifecycle()

    var showPasswordDialog by remember { mutableStateOf(false) }
    var showDateFormatDialog by remember { mutableStateOf(false) }
    var showTimeFormatDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                scrollBehavior = scrollBehavior,
                modifier = Modifier.clip(RoundedCornerShape(0, 0, 4, 4)),
                title = {
                    Column {
                        Text(
                            text = "Settings",
                            style = if (scrollBehavior.state.collapsedFraction >= .75) MaterialTheme.typography.titleLarge else MaterialTheme.typography.headlineMedium
                        )
                        AnimatedVisibility(scrollBehavior.state.collapsedFraction <= .75) {
                            Text(
                                text = "User: " + viewModel.userEmail,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(start = 8.dp),
                                color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                            )
                        }
                    }
                },
                navigationIcon = { BackButton { navigateBack() } },
                actions = {
                    PrimaryButton(
                        contentPadding = PaddingValues(horizontal = 30.dp),
                        onClick = { logOut() }
                    ) { Text("Logout") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
                ),
            )
        }
    ) { ip ->
        Column(
            modifier = Modifier
                .padding(ip)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Server Settings
            SettingsGroup(
                enabled = true,
                title = { Text(text = "Server Setting") },
                contentPadding = PaddingValues(horizontal = 8.dp),
            ) {
                SettingsSwitch(
                    modifier = Modifier.clip(singleItemIndex),
                    state = autoSync,
                    title = { Text(text = "Auto Sync", fontSize = 16.sp, fontWeight = W500) },
                    subtitle = {
                        Text(
                            text = "Upload any unSynced or updated notes to server automatically before app start",
                            fontSize = 12.sp
                        )
                    },
                    onCheckedChange = viewModel::updateAutoSyncDB
                )
            }

            // Theme & Appearance Settings
            SettingsGroup(
                enabled = true,
                title = { Text(text = "Theme & Appearance") },
                contentPadding = PaddingValues(horizontal = 8.dp),
            ) {

                // Theme Selection

                val appThemeData = AppTheme.entries.filter { it != AppTheme.UNDEFINED }
                val appThemeIconData = listOf(
                    Icons.Default.BrightnessAuto,
                    Icons.Default.LightMode,
                    Icons.Default.DarkMode
                )
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    appThemeData.forEachIndexed { index, option ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = appThemeData.size
                            ),
                            onClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentTick)
                                viewModel.updateTitleAlignment(index)
                            },
                            selected = settingState.titleAlignment == index
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = appThemeIconData[index],
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    option.toString().lowercase()
                                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                                    maxLines = 1,
                                    modifier = Modifier.basicMarquee()
                                )
                            }
                        }
                    }
                }

                SettingsSectionDivider()

                SettingsSwitch(
                    modifier = Modifier.clip(firstIndexShape),
                    state = settingState.shouldFollowSystem,
                    title = {
                        Text(
                            text = "Follow System Theme",
                            fontSize = 16.sp,
                            fontWeight = W500
                        )
                    },
                    subtitle = {
                        Text(
                            "Automatically switch theme based on system settings",
                            fontSize = 12.sp
                        )
                    },
                    onCheckedChange = viewModel::updateShouldFollowSystem
                )

                SettingsSectionDivider()

                SettingsSwitch(
                    modifier = Modifier.clip(lastIndexShape),
                    state = settingState.isAppInDarkMode,
                    title = { Text(text = "Dark Mode", fontSize = 16.sp, fontWeight = W500) },
                    subtitle = { Text("Override system theme with dark mode", fontSize = 12.sp) },
                    onCheckedChange = viewModel::updateDarkMode,
                    enabled = !settingState.shouldFollowSystem
                )
        }

        // Display Settings
            SettingsGroup(
                enabled = true,
                title = { Text(text = "Display Settings") },
                contentPadding = PaddingValues(horizontal = 8.dp),
            ) {
                SettingsSwitch(
                    modifier = Modifier.clip(firstIndexShape),
                    state = listType == ListType.GRID,
                    title = { Text(text = "Notes Grid", fontSize = 16.sp, fontWeight = W500) },
                    subtitle = { Text("Show notes in grid or list", fontSize = 12.sp) },
                    onCheckedChange = {
                        viewModel.updateListType(if (it) ListType.GRID else ListType.LIST)
                    }
                )

                SettingsSectionDivider()

                SettingsSwitch(
                    modifier = Modifier.clip(lastIndexShape),
                    state = settingState.isListView,
                    title = { Text(text = "List View", fontSize = 16.sp, fontWeight = W500) },
                    subtitle = { Text("Alternative list display mode", fontSize = 12.sp) },
                    onCheckedChange = viewModel::updateListView
                )

                SettingsSectionDivider()

                // Content Display Mode

                SettingTitle(text = "Display Mode")

                SettingSegmentedButtons(
                    modifier = Modifier.clip(firstIndexShape),
                    currentValue = settingState.enumDisplayMode,
                    onSelected = viewModel::updateDisplayMode,
                    data = ListNoteContentDisplayMode.entries,
                    hapticFeedback = hapticFeedback
                )

                SettingsSectionDivider()

                SettingTitle(text = "Content Size")
                // Content Size
                SettingSegmentedButtons(
                    modifier = Modifier.clip(middleIndexShape),
                    currentValue = settingState.enumContentSize,
                    onSelected = viewModel::updateContentSize,
                    data = ListNoteContentSize.entries,
                    hapticFeedback = hapticFeedback
                )

                SettingsSectionDivider()

                SettingTitle(text = "Overflow Style")

                SettingSegmentedButtons(
                    modifier = Modifier.clip(middleIndexShape),
                    currentValue = settingState.enumOverflowStyle,
                    onSelected = viewModel::updateOverflowStyle,
                    data = ListNoteContentOverflowStyle.entries,
                    hapticFeedback = hapticFeedback
                )
            }

        // Editor Settings
        SettingsGroup(
            enabled = true,
            title = { Text(text = "Editor Settings") },
            contentPadding = PaddingValues(horizontal = 8.dp),
        ) {

            // Font Scale Slider
            FontScaleSliderSetting(
                modifier = Modifier.clip(middleIndexShape),
                currentScale = settingState.fontScale,
                onScaleChanged = viewModel::updateFontScale
            )

            SettingsSectionDivider()

            SettingsSwitch(
                modifier = Modifier.clip(firstIndexShape),
                state = settingState.isAutoSaveEnabled,
                title = { Text(text = "Auto Save", fontSize = 16.sp, fontWeight = W500) },
                subtitle = {
                    Text(
                        "Automatically save changes while typing",
                        fontSize = 12.sp
                    )
                },
                onCheckedChange = viewModel::updateAutoSave
            )

            SettingsSectionDivider()

            SettingsSwitch(
                modifier = Modifier.clip(lastIndexShape),
                state = settingState.showLineNumbers,
                title = {
                    Text(
                        text = "Show Line Numbers",
                        fontSize = 16.sp,
                        fontWeight = W500
                    )
                },
                subtitle = { Text("Display line numbers in editor", fontSize = 12.sp) },
                onCheckedChange = viewModel::updateShowLineNumbers
            )

            SettingsSectionDivider()

            // Title Alignment
            SettingTitle("Title Alignment")

            val alignOptions = listOf(
                stringResource(Res.string.left),
                stringResource(Res.string.center),
                stringResource(Res.string.right)
            )
            val alignIcons = remember {
                listOf(
                    Icons.AutoMirrored.Outlined.FormatAlignLeft,
                    Icons.Outlined.FormatAlignCenter,
                    Icons.AutoMirrored.Outlined.FormatAlignRight
                )
            }

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                alignOptions.forEachIndexed { index, option ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = alignOptions.size
                        ),
                        onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentTick)
                            viewModel.updateTitleAlignment(index)
                        },
                        selected = settingState.titleAlignment == index
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = alignIcons[index],
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(option, maxLines = 1, modifier = Modifier.basicMarquee())
                        }
                    }
                }
            }
        }

        // Format Settings
        SettingsGroup(
            enabled = true,
            title = { Text(text = "Format Settings") },
            contentPadding = PaddingValues(horizontal = 8.dp),
        ) {
            SettingsMenuLink(
                modifier = Modifier.clip(firstIndexShape),
                title = { Text(text = "Date Format", fontSize = 16.sp, fontWeight = W500) },
                subtitle = {
                    Text(
                        settingState.dateFormatter.ifEmpty { "Default" },
                        fontSize = 12.sp
                    )
                },
                onClick = { showDateFormatDialog = true }
            )

            SettingsSectionDivider()

            SettingsMenuLink(
                modifier = Modifier.clip(lastIndexShape),
                title = { Text(text = "Time Format", fontSize = 16.sp, fontWeight = W500) },
                subtitle = {
                    Text(
                        settingState.timeFormatter.ifEmpty { "Default" },
                        fontSize = 12.sp
                    )
                },
                onClick = { showTimeFormatDialog = true }
            )
        }

        // Security Settings
        SettingsGroup(
            enabled = true,
            title = { Text(text = "Security Settings") },
            contentPadding = PaddingValues(horizontal = 8.dp),
        ) {
            SettingsSwitch(
                modifier = Modifier.clip(firstIndexShape),
                state = settingState.isScreenProtected,
                title = {
                    Text(
                        text = "Screen Protection",
                        fontSize = 16.sp,
                        fontWeight = W500
                    )
                },
                subtitle = {
                    Text(
                        "Prevent screenshots and screen recording",
                        fontSize = 12.sp
                    )
                },
                onCheckedChange = viewModel::updateScreenProtection
            )

            SettingsSectionDivider()

            SettingsSwitch(
                modifier = Modifier.clip(middleIndexShape),
                state = settingState.biometricAuthEnabled,
                title = {
                    Text(
                        text = "Biometric Authentication",
                        fontSize = 16.sp,
                        fontWeight = W500
                    )
                },
                subtitle = { Text("Use fingerprint or face unlock", fontSize = 12.sp) },
                onCheckedChange = viewModel::updateBiometricAuth
            )

            SettingsSectionDivider()

            SettingsMenuLink(
                modifier = Modifier.clip(lastIndexShape),
                title = { Text(text = "Password", fontSize = 16.sp, fontWeight = W500) },
                subtitle = {
                    Text(
                        if (settingState.password.isNotEmpty()) "Password set" else "No password",
                        fontSize = 12.sp
                    )
                },
                onClick = { showPasswordDialog = true }
            )
        }

        // Backup Settings
        SettingsGroup(
            enabled = true,
            title = { Text(text = "Backup Settings") },
            contentPadding = PaddingValues(horizontal = 8.dp),
        ) {
            // Backup Frequency
            BackupFrequencyDropdownSetting(
                modifier = Modifier.clip(singleItemIndex),
                currentFrequency = settingState.backupFrequency,
                onFrequencySelected = viewModel::updateBackupFrequency
            )
        }
    }
}

// Dialogs
if (showPasswordDialog) {
    PasswordDialog(
        currentPassword = settingState.password,
        onPasswordSet = { password ->
            viewModel.updatePassword(password)
            showPasswordDialog = false
        },
        onDismiss = { showPasswordDialog = false }
    )
}

if (showDateFormatDialog) {
    FormatDialog(
        title = "Date Format",
        currentFormat = settingState.dateFormatter,
        suggestions = listOf("dd/MM/yyyy", "MM/dd/yyyy", "yyyy-MM-dd", "dd MMM yyyy"),
        onFormatSet = { format ->
            viewModel.updateDateFormatter(format)
            showDateFormatDialog = false
        },
        onDismiss = { showDateFormatDialog = false }
    )
}

if (showTimeFormatDialog) {
    FormatDialog(
        title = "Time Format",
        currentFormat = settingState.timeFormatter,
        suggestions = listOf("HH:mm", "hh:mm a", "HH:mm:ss", "hh:mm:ss a"),
        onFormatSet = { format ->
            viewModel.updateTimeFormatter(format)
            showTimeFormatDialog = false
        },
        onDismiss = { showTimeFormatDialog = false }
    )
}
}

@Composable
fun <T> SettingSegmentedButtons(
    modifier: Modifier = Modifier,
    currentValue: T,
    onSelected: (T) -> Unit,
    data: List<T>,
    hapticFeedback: HapticFeedback = LocalHapticFeedback.current,
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .then(modifier),
    ) {
        data.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = data.size),
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentTick)
                    onSelected(label)
                },
                selected = label == currentValue,
                label = {
                    Text(
                        label.toString()
                            .lowercase()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() })
                },
            )
        }
    }
}


@Composable
fun SettingTitle(text: String) = Text(
    text = text,
    fontSize = 16.sp,
    fontWeight = W500,
    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
)

@Composable
fun FontScaleSliderSetting(
    modifier: Modifier = Modifier,
    currentScale: Float,
    onScaleChanged: (Float) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Font Scale", fontSize = 16.sp, fontWeight = W500)
            Text(text = "${(currentScale * 100).roundToInt()}%", fontSize = 14.sp)
        }
        Slider(
            value = currentScale,
            onValueChange = onScaleChanged,
            valueRange = 0.5f..2.0f,
            steps = 15,
            modifier = Modifier.padding(top = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "50%",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
            )
            Text(
                text = "200%",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
            )
        }
    }
}

// Additional dropdown components for other enums...
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentDisplayModeDropdownSetting(
    modifier: Modifier = Modifier,
    currentMode: ListNoteContentDisplayMode,
    onModeSelected: (ListNoteContentDisplayMode) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            readOnly = true,
            value = when (currentMode) {
                ListNoteContentDisplayMode.RAW -> "Raw Content"
                ListNoteContentDisplayMode.PREVIEW -> "Preview Mode"
            },
            onValueChange = {},
            label = { Text("Display Mode") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            ListNoteContentDisplayMode.entries.forEach { mode ->
                DropdownMenuItem(
                    text = {
                        Text(
                            when (mode) {
                                ListNoteContentDisplayMode.RAW -> "Raw Content"
                                ListNoteContentDisplayMode.PREVIEW -> "Preview Mode"
                            }
                        )
                    },
                    onClick = {
                        onModeSelected(mode)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentSizeDropdownSetting(
    modifier: Modifier = Modifier,
    currentSize: ListNoteContentSize,
    onSizeSelected: (ListNoteContentSize) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            readOnly = true,
            value = when (currentSize) {
                ListNoteContentSize.DEFAULT -> "Default"
                ListNoteContentSize.COMPACT -> "Compact"
                ListNoteContentSize.FLAT -> "Flat"
            },
            onValueChange = {},
            label = { Text("Content Size") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            ListNoteContentSize.entries.forEach { size ->
                DropdownMenuItem(
                    text = {
                        Text(
                            when (size) {
                                ListNoteContentSize.DEFAULT -> "Default"
                                ListNoteContentSize.COMPACT -> "Compact"
                                ListNoteContentSize.FLAT -> "Flat"
                            }
                        )
                    },
                    onClick = {
                        onSizeSelected(size)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverflowStyleDropdownSetting(
    modifier: Modifier = Modifier,
    currentStyle: ListNoteContentOverflowStyle,
    onStyleSelected: (ListNoteContentOverflowStyle) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            readOnly = true,
            value = when (currentStyle) {
                ListNoteContentOverflowStyle.ELLIPSIS -> "Ellipsis (...)"
                ListNoteContentOverflowStyle.CLIP -> "Clip"
            },
            onValueChange = {},
            label = { Text("Overflow Style") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            ListNoteContentOverflowStyle.entries.forEach { style ->
                DropdownMenuItem(
                    text = {
                        Text(
                            when (style) {
                                ListNoteContentOverflowStyle.ELLIPSIS -> "Ellipsis (...)"
                                ListNoteContentOverflowStyle.CLIP -> "Clip"
                            }
                        )
                    },
                    onClick = {
                        onStyleSelected(style)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleAlignmentDropdownSetting(
    modifier: Modifier = Modifier,
    currentAlignment: Int,
    onAlignmentSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            readOnly = true,
            value = when (currentAlignment) {
                0 -> "Left"
                1 -> "Center"
                2 -> "Right"
                else -> "Left"
            },
            onValueChange = {},
            label = { Text("Title Alignment") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            listOf(0 to "Left", 1 to "Center", 2 to "Right").forEach { (value, label) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        onAlignmentSelected(value)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupFrequencyDropdownSetting(
    modifier: Modifier = Modifier,
    currentFrequency: Int,
    onFrequencySelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            readOnly = true,
            value = when (currentFrequency) {
                0 -> "Never"
                1 -> "Daily"
                7 -> "Weekly"
                30 -> "Monthly"
                else -> "Custom (${currentFrequency} days)"
            },
            onValueChange = {},
            label = { Text("Backup Frequency") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            listOf(
                0 to "Never",
                1 to "Daily",
                7 to "Weekly",
                30 to "Monthly"
            ).forEach { (value, label) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        onFrequencySelected(value)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Composable
fun PasswordDialog(
    currentPassword: String,
    onPasswordSet: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var password by remember { mutableStateOf(currentPassword) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Password") },
        text = {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { onPasswordSet(password) }) {
                Text("Set")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun FormatDialog(
    title: String,
    currentFormat: String,
    suggestions: List<String>,
    onFormatSet: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var format by remember { mutableStateOf(currentFormat) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                OutlinedTextField(
                    value = format,
                    onValueChange = { format = it },
                    label = { Text("Format") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Suggestions:",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )

                suggestions.forEach { suggestion ->
                    TextButton(
                        onClick = { format = suggestion },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = suggestion,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Start
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onFormatSet(format) }) {
                Text("Set")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
package com.devansh.noteapp.ui.screens.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devansh.noteapp.ui.screens.setting.AppColor.Companion.toInt
import com.devansh.noteapp.ui.screens.setting.AppTheme.Companion.toInt
import noteapp.composeapp.generated.resources.Res
import noteapp.composeapp.generated.resources.color_platte
import noteapp.composeapp.generated.resources.dark
import noteapp.composeapp.generated.resources.dynamic_only_android_12
import noteapp.composeapp.generated.resources.light
import noteapp.composeapp.generated.resources.system_default
import org.jetbrains.compose.resources.stringResource

@Composable
fun StylePane(sharedViewModel: SettingViewModel) {

    val settingsState by sharedViewModel.settingsStateFlow.collectAsStateWithLifecycle()

    val modeOptions = listOf(
        stringResource(Res.string.system_default),
        stringResource(Res.string.light),
        stringResource(Res.string.dark)
    )


    val isSystemDarkTheme = isSystemInDarkTheme()
    val hapticFeedback = LocalHapticFeedback.current

    Column(Modifier.verticalScroll(rememberScrollState())) {

        Box(
            modifier = Modifier
                .sizeIn(maxWidth = 673.dp)
                .padding(16.dp)
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
//            PaletteImage()
        }

//        SettingsHeader(text = stringResource(Res.string.color_platte))

        Row(
            Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentTick)
                    /*sharedViewModel.putPreferenceValue(
                        Constants.Preferences.APP_COLOR,
                        AppColor.DYNAMIC.toInt()
                    )*/
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                modifier = Modifier.padding(start = 32.dp),
                selected = settingsState.color == AppColor.DYNAMIC,
                onClick = null
            )
            Icon(
                modifier = Modifier
                    .padding(start = 16.dp),
                imageVector = Icons.Default.Colorize,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = ""
            )
            Text(
                text = stringResource(Res.string.dynamic_only_android_12),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 16.dp)
            )
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(
                8.dp,
                Alignment.Start
            )
        ) {
            Spacer(modifier = Modifier.width(24.dp))
            /*colorSchemes.forEach { colorSchemePair ->
                SelectableColorPlatte(
                    selected = settingsState.color == colorSchemePair.first,
                    colorScheme = colorSchemePair.second
                ) {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentTick)
                    sharedViewModel.putPreferenceValue(
                        Constants.Preferences.APP_COLOR,
                        colorSchemePair.first.toInt()
                    )
                }
            }*/
            Spacer(modifier = Modifier.width(32.dp))
        }

        Column(Modifier.selectableGroup()) {
            modeOptions.forEachIndexed { index, text ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (index == settingsState.theme.toInt()),
                            onClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentTick)
                                if (settingsState.theme.toInt() != index) {
                                    when (index) {
                                        0 -> {
                                            if (isSystemDarkTheme != settingsState.isAppInDarkMode) {
//                                                sharedViewModel.putPreferenceValue(
//                                                    Constants.Preferences.IS_SWITCH_ACTIVE,
//                                                    true
//                                                )
                                            } else {
//                                                sharedViewModel.putPreferenceValue(
//                                                    Constants.Preferences.APP_THEME,
//                                                    AppTheme.SYSTEM.toInt()
//                                                )
                                            }
//                                            sharedViewModel.putPreferenceValue(
//                                                Constants.Preferences.SHOULD_FOLLOW_SYSTEM,
//                                                true
//                                            )
                                        }

                                        1 -> {
                                            if (settingsState.isAppInDarkMode) {
//                                                sharedViewModel.putPreferenceValue(
//                                                    Constants.Preferences.IS_SWITCH_ACTIVE,
//                                                    true
//                                                )
                                            }
//                                            sharedViewModel.putPreferenceValue(
//                                                Constants.Preferences.SHOULD_FOLLOW_SYSTEM,
//                                                false
//                                            )
//                                            sharedViewModel.putPreferenceValue(
//                                                Constants.Preferences.APP_THEME,
//                                                AppTheme.LIGHT.toInt()
//                                            )
                                        }

                                        2 -> {
                                            if (!settingsState.isAppInDarkMode) {
//                                                sharedViewModel.putPreferenceValue(
//                                                    Constants.Preferences.IS_SWITCH_ACTIVE,
//                                                    true
//                                                )
                                            }
//                                            sharedViewModel.putPreferenceValue(
//                                                Constants.Preferences.SHOULD_FOLLOW_SYSTEM,
//                                                false
//                                            )
//                                            sharedViewModel.putPreferenceValue(
//                                                Constants.Preferences.APP_THEME,
//                                                AppTheme.DARK.toInt()
//                                            )
                                        }
                                    }
                                }
                            },
                            role = Role.RadioButton
                        )
                        .padding(start = 32.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (index == settingsState.theme.toInt()),
                        onClick = null
                    )

                    Icon(
                        modifier = Modifier
                            .padding(start = 16.dp),
                        imageVector = when (index) {
                            AppTheme.LIGHT.toInt() -> Icons.Default.LightMode
                            AppTheme.DARK.toInt() -> Icons.Default.DarkMode
                            else -> Icons.Default.BrightnessAuto
                        },
                        tint = MaterialTheme.colorScheme.secondary,
                        contentDescription = ""
                    )

                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(start = 16.dp)

                    )
                }
            }
        }

        Spacer(Modifier.navigationBarsPadding())
    }
}
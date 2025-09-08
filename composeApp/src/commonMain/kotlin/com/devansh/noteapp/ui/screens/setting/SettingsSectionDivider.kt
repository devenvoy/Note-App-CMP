package com.devansh.noteapp.ui.screens.setting

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun SettingsSectionDivider() = HorizontalDivider(
    thickness = 4.dp,
    color = MaterialTheme.colorScheme.background
)
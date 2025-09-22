package com.devansh.noteapp.core.designsystem.components.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devansh.noteapp.core.utils.UnitCBF

@Composable
fun TextOptionButton(
    buttonText: String,
    onButtonClick: UnitCBF
) = TextButton(
    modifier = Modifier
        .fillMaxWidth()
        .padding(top = 8.dp),
    shape = MaterialTheme.shapes.medium,
    colors = ButtonDefaults.textButtonColors().copy(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ),
    onClick = onButtonClick
) {
    Text(
        modifier = Modifier.padding(vertical = 4.dp),
        text = buttonText,
        style = MaterialTheme.typography.titleMedium
    )
}
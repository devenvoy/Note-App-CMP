package com.devansh.noteapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(8.dp),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        contentColor = Color.White,
        containerColor = MaterialTheme.colorScheme.primary,

        ),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(
        defaultElevation = 1.dp
    ),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = PaddingValues(vertical = 10.dp),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        contentPadding = contentPadding,
        colors = colors,
        shape = shape,
        elevation = elevation,
        border = border,
        interactionSource = interactionSource,
        content = content
    )
}

@Composable
fun SecondaryOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(8.dp),
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = MaterialTheme.colorScheme.background
    ),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(defaultElevation = 1.dp),
    border: BorderStroke? = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
    contentPadding: PaddingValues = PaddingValues(vertical = 10.dp),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit = {
        Text(
            text = "Create Account",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        colors = colors,
        shape = shape,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        elevation = elevation,
        content = content
    )
}
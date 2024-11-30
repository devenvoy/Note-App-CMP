package com.devansh.noteapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import network.chaintech.sdpcomposemultiplatform.ssp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextArea(
    modifier: Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    interactionSource: MutableInteractionSource? = null,
    enabled: Boolean = true,
    singleLine: Boolean = false,
    colors: TextFieldColors = colors(),
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
) {

    BasicTextField(
        value = text,
        textStyle = TextStyle(fontSize = 16.ssp),
        onValueChange = onValueChange,
        modifier = modifier.background(color = Color.Transparent),
        interactionSource = interactionSource ?: MutableInteractionSource(),
        enabled = enabled,
        singleLine = singleLine
    ) {
        val itSrc by remember {
            mutableStateOf(
                interactionSource ?: MutableInteractionSource()
            )
        }
        TextFieldDefaults.DecorationBox(
            value = text,
            innerTextField = it,
            enabled = enabled,
            singleLine = singleLine,
            visualTransformation = VisualTransformation.None,
            interactionSource = itSrc,
            label = label,
            placeholder = placeholder,
            trailingIcon = trailingIcon,
            leadingIcon = leadingIcon,
            prefix = prefix,
            suffix = suffix,
            colors = colors,
            contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(0.dp, 0.dp, 0.dp, 0.dp),
        )

    }
}
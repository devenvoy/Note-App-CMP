package com.devansh.noteapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Eye
import compose.icons.fontawesomeicons.solid.EyeSlash

@Composable
fun CustomInputField(
    fieldTitle: String?= null,
    textFieldValue: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    placeholder: @Composable() (() -> Unit)? = null,
    trailingIcon: @Composable() (() -> Unit)? = null,
    suffix: @Composable() (() -> Unit)? = null,
    prefix: @Composable() (() -> Unit)? = null,
    supportingText: @Composable() (() -> Unit)? = null,
    isEnable: Boolean = true,
    keyboardOption: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
    readOnly: Boolean = false
) {
    Column(
        modifier = Modifier
            .widthIn(max =400.dp, min = Dp.Infinity)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        fieldTitle?.let {
            Text(
                text = fieldTitle,
                modifier = Modifier.padding(start = 2.dp),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp
            )
        }
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(8.dp),
            trailingIcon = trailingIcon,
            enabled = isEnable,
            readOnly = readOnly,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.onBackground.copy(.1f),
                unfocusedContainerColor = MaterialTheme.colorScheme.onBackground.copy(.1f),
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = .7f)
            ),
            suffix = suffix,
            prefix = prefix,
            singleLine = singleLine,
            keyboardOptions = keyboardOption.copy(
                imeAction = ImeAction.Next
            ),
            supportingText = supportingText,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 12.sp,
            ),
            placeholder = placeholder,
        )
    }
}

@Composable
fun CustomInputPasswordField(
    fieldTitle: String,
    textFieldValue: String,
    onValueChange: (String) -> Unit,
    minHeight: Dp = 56.dp,
    singleLine: Boolean = true,
    placeholder: @Composable (() -> Unit)? = null,
    isPasswordField: Boolean = false,
    isEnable: Boolean = true
) {
    // State to manage password visibility
    var passwordVisible by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .widthIn(max =400.dp, min = Dp.Infinity)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = fieldTitle,
            modifier = Modifier.padding(start = 2.dp),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp
        )
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(8.dp),
            trailingIcon = {
                if (isPasswordField) {
                    val visibilityIcon =
                        if (passwordVisible) FontAwesomeIcons.Solid.Eye else FontAwesomeIcons.Solid.EyeSlash

                    IconButton(
                        onClick = { passwordVisible = !passwordVisible }
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = visibilityIcon,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }
            },
            enabled = isEnable,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.onBackground.copy(.1f),
                unfocusedContainerColor = MaterialTheme.colorScheme.onBackground.copy(.1f),
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = .7f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(minHeight),
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPasswordField && !passwordVisible) KeyboardType.Password else KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            visualTransformation = if (isPasswordField && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 12.sp,
            ),
            placeholder = placeholder,
        )
    }
}

@Composable
fun CustomInputArea(
    fieldTitle: String,
    textFieldValue: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = false,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isEnable: Boolean = true,
    keyboardOption: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
    height: Dp = 80.dp
) {
    Column(
        modifier = Modifier
            .widthIn(max =400.dp, min = Dp.Infinity)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = fieldTitle,
            modifier = Modifier.padding(start = 2.dp),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp
        )
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(8.dp),
            trailingIcon = trailingIcon,
            enabled = isEnable,
            modifier = Modifier.fillMaxWidth().height(height),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.onBackground.copy(.1f),
                unfocusedContainerColor = MaterialTheme.colorScheme.onBackground.copy(.1f),
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = .7f)
            ),
            suffix = suffix,
            prefix = prefix,
            singleLine = singleLine,
            keyboardOptions = keyboardOption.copy(
                imeAction = ImeAction.Next
            ),
            supportingText = supportingText,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 12.sp,
            ),
            placeholder = placeholder,
        )
    }
}
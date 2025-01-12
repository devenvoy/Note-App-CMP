package com.devansh.noteapp.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import network.chaintech.sdpcomposemultiplatform.ssp

@Composable
fun ExpandableSearchView(
    searchDisplay: String,
    onSearchDisplayChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onExpandedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    expandedInitially: Boolean = false,
    collapsedSearchView: @Composable () -> Unit = {}
) {

    Crossfade(targetState = expandedInitially, label = "") { isSearchFieldVisible ->
        when (isSearchFieldVisible) {
            true -> ExpandedSearchView(
                searchDisplay = searchDisplay,
                onSearchDisplayChanged = onSearchDisplayChanged,
                onSearch = onSearch,
                onExpandedChanged = onExpandedChanged,
                modifier = modifier,
                tint = MaterialTheme.colorScheme.primary
            )

            false -> collapsedSearchView()
        }
    }
}

@Composable
fun ExpandedSearchView(
    searchDisplay: String,
    onSearchDisplayChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onExpandedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = Color.Blue,
) {
    val textFieldFocusRequester = remember { FocusRequester() }

    SideEffect {
        textFieldFocusRequester.requestFocus()
    }

    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(searchDisplay, TextRange(searchDisplay.length)))
    }

    Row(
        modifier = modifier.statusBarsPadding().fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
            ),
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
                onSearchDisplayChanged(it.text)
            },
            leadingIcon = {
                IconButton(
                    onClick = {
                        onExpandedChanged(false)
                    }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back icon",
                        tint = tint
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        onSearch()
                    }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "back icon",
                        tint = tint
                    )
                }
            },
            modifier = Modifier.fillMaxWidth().focusRequester(textFieldFocusRequester),
            placeholder = {
                Text(text = "Search here .. ")
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {
                onSearch()
            })
        )
    }
}

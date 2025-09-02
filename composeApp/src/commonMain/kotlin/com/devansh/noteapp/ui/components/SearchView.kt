package com.devansh.noteapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    query: String,
    expanded: Boolean,
    onQueryChange: (String) -> Unit,
    onExpandedChange: (Boolean) -> Unit = {},
    onSearch: (String) -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {

//    BackHandler(expanded){
//        onExpandedChange(false)
//    }

    SearchBar(
        modifier = Modifier.then(
            if (expanded) Modifier.padding(0.dp) else Modifier.padding(16.dp, 0.dp)
        ).fillMaxWidth(),
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                expanded = expanded,
                modifier = Modifier.padding(16.dp, 0.dp).fillMaxWidth(),
                onExpandedChange = onExpandedChange,
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Default.Cancel else Icons.Default.Search,
                        modifier = Modifier.clickable(
                            enabled = expanded,
                            onClick = { onExpandedChange(false) }),
                        contentDescription = if (expanded) "Close" else "Search"
                    )
                },
                placeholder = {
                    Text(
                        text = "Search members...",
                        fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        },
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        content = content
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SearchbarInputField(
    searchBarState: SearchBarState,
    state: TextFieldState,
    scope: CoroutineScope,
    onSearchClick: (String) -> Unit
) {
    SearchBarDefaults.InputField(
        modifier = Modifier,
        searchBarState = searchBarState,
        textFieldState = state,
        onSearch = onSearchClick,
        placeholder = {
            if (searchBarState.currentValue == SearchBarValue.Collapsed) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Search members.."
                )
            }
        },
        leadingIcon = {
            if (searchBarState.currentValue == SearchBarValue.Expanded) {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberTooltipPositionProvider(),
                    tooltip = { PlainTooltip { Text(text = "Back") } },
                    state = rememberTooltipState(),
                ) {
                    IconButton(onClick = { scope.launch { searchBarState.animateToCollapsed() } }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = "back"
                        )
                    }
                }
            }
        },
        trailingIcon = {
            TooltipBox(
                positionProvider =
                    TooltipDefaults.rememberTooltipPositionProvider(),
                tooltip = { PlainTooltip { Text(text = "search") } },
                state = rememberTooltipState(),
            ) {
                IconButton(onClick = { onSearchClick(state.text as String) }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "search"
                    )
                }
            }
        },
    )
}
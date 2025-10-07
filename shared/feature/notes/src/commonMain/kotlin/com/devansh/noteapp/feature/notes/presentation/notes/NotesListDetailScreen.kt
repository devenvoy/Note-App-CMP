package com.devansh.noteapp.feature.notes.presentation.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.devansh.noteapp.core.designsystem.SettingsState
import com.devansh.noteapp.core.designsystem.components.EmptyScreen
import com.devansh.noteapp.core.designsystem.resources.NoteAppDrawables
import com.devansh.noteapp.core.designsystem.theme.LocalAppTheme
import com.devansh.noteapp.core.utils.StringCBF
import com.devansh.noteapp.feature.notes.presentation.add_edit.AddEditNoteViewModel
import com.devansh.noteapp.feature.notes.presentation.add_edit.AddEditScreenContent
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3AdaptiveApi::class
)
@Composable
fun NotesListDetailScreen(
    homeScreenModel: HomeScreenViewModel,
    settingsState: SettingsState,
    onShareText: StringCBF,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Long>()
    val noteState by homeScreenModel.noteState.collectAsState()
    val scope = rememberCoroutineScope()

    // State for the selected note in detail pane
    var selectedNoteId by remember { mutableStateOf<Long?>(null) }
    val theme = LocalAppTheme.current

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                HomeScreenContent(
                    viewModel = homeScreenModel,
                    settingsState=settingsState,
                    onNavigateToAddEditNote = { noteId ->
                        selectedNoteId = noteId
                        scope.launch {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, noteId)
                        }
                    },
                    onShareText = onShareText
                )
            }
        },
        detailPane = {
            AnimatedPane(
                modifier = Modifier.clip(
                    ShapeDefaults.ExtraLarge.copy(
                        topEnd = CornerSize(0.dp),
                        bottomEnd = CornerSize(0.dp)
                    )
                )
                    .background(MaterialTheme.colorScheme.onBackground.copy(.1f))
            ) {
                if (selectedNoteId != null) {
                    // Detail pane contains your AddEditScreenContent
                    val viewModel = koinViewModel<AddEditNoteViewModel> {
                        parametersOf(selectedNoteId)
                    }

                    AddEditScreenContent(
                        viewModel = viewModel,
                        onNavigateUp = {
                            selectedNoteId = null
                            scope.launch {
                                navigator.navigateTo(ListDetailPaneScaffoldRole.List)
                            }
                        }
                    )
                } else {
                    // Empty state when no note is selected
                    EmptyScreen(
                        text = "No note selected",
                        image = if (theme.dark) NoteAppDrawables.noTask else NoteAppDrawables.noTaskLight,
                    )
                }
            }
        }
    )
}
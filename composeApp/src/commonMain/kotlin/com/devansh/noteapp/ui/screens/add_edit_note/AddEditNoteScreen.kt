package com.devansh.noteapp.ui.screens.add_edit_note

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.devansh.noteapp.di.platform_di.clipEntryOf
import com.devansh.noteapp.domain.model.Note
import com.devansh.noteapp.navigation.NavRoute
import com.devansh.noteapp.ui.components.BackButton
import com.devansh.noteapp.ui.components.HintUI
import com.devansh.noteapp.ui.screens.home.NoteMenuBottomSheet
import com.dokar.sonner.ToastType
import com.dokar.sonner.Toaster
import com.dokar.sonner.ToasterDefaults
import com.dokar.sonner.rememberToasterState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel


fun NavGraphBuilder.addNoteScreen(navHostController: NavHostController) {
    composable<NavRoute.AddNote> {
        val noteId = it.toRoute<NavRoute.AddNote>().noteId
        val viewModel = koinViewModel<AddEditNoteViewModel>()
        LaunchedEffect(Unit) { viewModel.initState(noteId = noteId) }
        AddEditScreenContent(
            viewModel = viewModel,
            onNavigateUp = { navHostController.navigateUp() }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreenContent(
    viewModel: AddEditNoteViewModel, onNavigateUp: () -> Unit
) {
    val titleState by viewModel.noteTitle
    val selectedBgColor by viewModel.noteColor.collectAsState()
    val noteBgAnimation = remember { Animatable(Color(selectedBgColor)) }
    val richTextState = rememberRichTextState()
    val openLinkDialog = remember { mutableStateOf(false) }
    val toasterState = rememberToasterState()
    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboard.current

    val scope = rememberCoroutineScope()

    LaunchedEffect(viewModel.noteContent.value) {
        richTextState.setHtml(viewModel.noteContent.value)
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    toasterState.show(
                        message = event.message,
                        duration = ToasterDefaults.DurationShort,
                        type = ToastType.Normal
                    )
                }

                is AddEditNoteViewModel.UiEvent.SaveNote -> onNavigateUp()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    noteBgAnimation.value.copy(.4f)
                ),
                actions = {
                    IconButton(onClick = { isBottomSheetVisible = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null,
                        )
                    }
                },
                navigationIcon = { BackButton { onNavigateUp() } },
                title = {}
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.imePadding(),
                onClick = {
                    viewModel.onEvent(
                        AddEditNoteEvent.SaveNote(richTextState.toHtml())
                    )
                }
            ) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save Note"
                )
            }
        })
    { padding ->

        Toaster(
            state = toasterState,
            richColors = true,
            darkTheme = isSystemInDarkTheme(),
            showCloseButton = true,
            alignment = Alignment.TopCenter,
        )

        if (isBottomSheetVisible) {
            val dismissSheet = {
                scope.launch { sheetState.hide() }
                isBottomSheetVisible = false
            }

            ModalBottomSheet(
                onDismissRequest = { dismissSheet() },
                sheetState = sheetState,
                tonalElevation = 0.dp,
                dragHandle = null
            ) {
                NoteMenuBottomSheet(
                    onEditClick = {},
                    onShareClick = {},
                    onDeleteClick = {
                        viewModel.deleteNoteById()
                        toasterState.show(
                            "Note deleted successfully",
                            duration = ToasterDefaults.DurationLong,
                            type = ToastType.Warning
                        )
                        dismissSheet()
                        onNavigateUp()
                    },
                    onCopyClick = {
                        scope.launch {
                            clipboardManager.setClipEntry(
                                clipEntryOf(AnnotatedString("$titleState \n\n ${richTextState.toText()}").toString())
                            )
                        }
                        toasterState.show(
                            "Copied to clipboard",
                            duration = ToasterDefaults.DurationShort,
                            type = ToastType.Info
                        )
                        dismissSheet()
                    },
                    showEditOption = false
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(noteBgAnimation.value.copy(alpha = .4f))
                .padding(vertical = 12.dp, horizontal = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Note.colors.forEach { colorInt ->
                    val color = Color(colorInt)

                    Box(
                        modifier = Modifier.size(40.dp).shadow(15.dp, CircleShape)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = 3.dp,
                                color = if (selectedBgColor == colorInt) {
                                    Color.White
                                } else {
                                    Color.Transparent  //color is deselected
                                },
                                shape = CircleShape
                            )
                            .clickable(
                                onClick = {
                                    scope.launch {
                                        noteBgAnimation.animateTo(
                                            targetValue = Color(colorInt),
                                            animationSpec = tween(durationMillis = 500)
                                        )
                                    }

                                    viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                                }
                            )
                    )
                }
            }

            SlackPanel(
                state = richTextState,
                openLinkDialog = openLinkDialog,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Spacer(Modifier.height(12.dp))

            HintUI(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(12.dp))

            RichTextEditor(
                state = richTextState,
                placeholder = {
                    Text(text = "Note #write-note-content-here")
                },
                colors = RichTextEditorDefaults.richTextEditorColors(
                    textColor = MaterialTheme.colorScheme.onSurface,
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    placeholderColor = Color.Gray.copy(alpha = .6f),
                ),
                modifier = Modifier.fillMaxWidth()
            )

            if (openLinkDialog.value)
                Dialog(
                    onDismissRequest = {
                        openLinkDialog.value = false
                    }
                ) {
                    SlackLinkDialog(
                        state = richTextState,
                        openLinkDialog = openLinkDialog
                    )
                }
        }
    }
}

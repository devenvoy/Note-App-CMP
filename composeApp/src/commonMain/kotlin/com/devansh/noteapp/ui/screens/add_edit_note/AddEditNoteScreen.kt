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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.devansh.noteapp.di.platform_di.clipEntryOf
import com.devansh.noteapp.domain.model.Note
import com.devansh.noteapp.domain.utils.UnitCBF
import com.devansh.noteapp.navigation.NavRoute
import com.devansh.noteapp.ui.components.HintUI
import com.devansh.noteapp.ui.components.button.BackButton
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
import org.koin.core.parameter.parametersOf


fun NavGraphBuilder.addNoteScreen(navHostController: NavHostController) {
    composable<NavRoute.AddNote> {
        val noteId = it.toRoute<NavRoute.AddNote>().noteId
        val viewModel = koinViewModel<AddEditNoteViewModel> { parametersOf(noteId) }
        AddEditScreenContent(
            viewModel = viewModel, onNavigateUp = { navHostController.navigateUp() })
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AddEditScreenContent(
    viewModel: AddEditNoteViewModel, onNavigateUp: UnitCBF
) {
    val clipboardManager = LocalClipboard.current

    val titleState by viewModel.noteTitle
    val currentNote by viewModel.currentNote.collectAsStateWithLifecycle()
    val selectedBgColor = currentNote.colorRes

    val scope = rememberCoroutineScope()
    val toasterState = rememberToasterState()
    val richTextState = rememberRichTextState()
    val sheetState = rememberModalBottomSheetState()

    val openLinkDialog = remember { mutableStateOf(false) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    val noteBgAnimation = remember(selectedBgColor) { Animatable(Color(selectedBgColor)) }


    LaunchedEffect(currentNote.content) {
        if (richTextState.toHtml() != currentNote.content) {
            richTextState.setHtml(currentNote.content)
        }
    }

    // Sync rich text content with ViewModel whenever it changes
    LaunchedEffect(richTextState.toHtml()) {
        val htmlContent = richTextState.toHtml()
        if (htmlContent != currentNote.content) {
            viewModel.onEvent(AddEditNoteEvent.EnteredContent(htmlContent))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    toasterState.show(
                        message = event.message,
                        duration = ToasterDefaults.DurationShort,
                        type = ToastType.Normal
                    )
                }

                is UiEvent.SaveNote -> onNavigateUp()
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
                    FilledTonalIconButton(
                        modifier = Modifier.size(width = 52.dp, height = 32.dp),
                        shapes = IconButtonDefaults.shapes(),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ), onClick = {
                            viewModel.onEvent(AddEditNoteEvent.EnteredContent(richTextState.toHtml()))
                            viewModel.onEvent(AddEditNoteEvent.SaveNote)
                        }) {
                        Icon(
                            modifier = Modifier.padding(4.dp),
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save Note"
                        )
                    }
                    FilledTonalIconButton(
                        modifier = Modifier.padding(4.dp).size(width = 20.dp, height = 32.dp),
                        shapes = IconButtonDefaults.shapes(),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                        onClick = { isBottomSheetVisible = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null,
                        )
                    }
                },
                navigationIcon = { BackButton { onNavigateUp() } },
                title = {},
            )
        }) { padding ->

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
                    onEditClick = {}, onShareClick = {}, onDeleteClick = {
                        viewModel.deleteNoteById()
                        toasterState.show(
                            "Note deleted successfully",
                            duration = ToasterDefaults.DurationLong,
                            type = ToastType.Warning
                        )
                        dismissSheet()
                        onNavigateUp()
                    }, onCopyClick = {
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
                    }, showEditOption = false
                )
            }
        }

        Column(
            modifier = Modifier.padding(padding).fillMaxSize()
                .background(noteBgAnimation.value.copy(alpha = .4f))
        ) {
            Row(
                modifier = Modifier.padding(4.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Note.colors.forEach { colorInt ->
                    val color = Color(colorInt)

                    Box(
                        modifier = Modifier.size(40.dp).shadow(15.dp, CircleShape).clip(CircleShape)
                            .background(color).border(
                                width = 3.dp, color = if (selectedBgColor == colorInt) {
                                    Color.White
                                } else {
                                    Color.Transparent  //color is deselected
                                }, shape = CircleShape
                            ).clickable(
                                onClick = {
                                    scope.launch {
                                        noteBgAnimation.animateTo(
                                            targetValue = Color(colorInt),
                                            animationSpec = tween(durationMillis = 500)
                                        )
                                    }

                                    viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                                })
                    )
                }
            }


            HintUI(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = { viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it)) },
                onFocusChange = { viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it)) },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(12.dp))

            RichTextEditor(
                state = richTextState,
                placeholder = { Text(text = "#write note content here") },
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = RichTextEditorDefaults.richTextEditorColors(
                    textColor = MaterialTheme.colorScheme.onSurface,
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    placeholderColor = Color.Gray.copy(alpha = .6f),
                ),
                modifier = Modifier.fillMaxWidth().weight(1f)
            )
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            SlackPanel(
                state = richTextState,
                openLinkDialog = openLinkDialog,
                modifier = Modifier.fillMaxWidth().systemBarsPadding()
            )
        }

        if (openLinkDialog.value) {
            Dialog(onDismissRequest = { openLinkDialog.value = false }) {
                SlackLinkDialog(
                    state = richTextState, openLinkDialog = openLinkDialog
                )
            }
        }
    }
}

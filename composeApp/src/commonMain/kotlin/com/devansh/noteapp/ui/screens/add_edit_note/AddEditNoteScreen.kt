package com.devansh.noteapp.ui.screens.add_edit_note

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.devansh.noteapp.domain.model.Note
import com.devansh.noteapp.ui.components.HintUI
import com.devansh.noteapp.ui.screens.core.Constants
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.Save
import compose.icons.fontawesomeicons.solid.ArrowLeft
import compose.icons.fontawesomeicons.solid.EllipsisV
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import network.chaintech.sdpcomposemultiplatform.sdp

class AddEditNoteScreen(private val noteId: Long) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel { AddEditNoteViewModel(Constants.noteDatabase) }
        LaunchedEffect(Unit) {
            viewModel.initState(noteId = noteId)
        }
        AddEditScreenContent(viewModel = viewModel, onNavigateUp = { navigator.pop() })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreenContent(
    viewModel: AddEditNoteViewModel, onNavigateUp: () -> Unit
) {
    val titleState = viewModel.noteTitle.value
    val selectedBgColor by viewModel.noteColor.collectAsState()
    val noteBgAnimation = remember { Animatable(Color(selectedBgColor)) }
    val richTextState = rememberRichTextState()
    val openLinkDialog = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(viewModel.noteContent.value) {
        richTextState.setHtml(viewModel.noteContent.value)
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    SnackbarHostState().showSnackbar(
                        message = event.message
                    )
                }

                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    onNavigateUp()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = noteBgAnimation.value.copy(alpha = .4f)
                ),
                actions = {
                    IconButton(onClick = { onNavigateUp() }) {
                        Icon(
                            modifier = Modifier.size(14.sdp),
                            imageVector = FontAwesomeIcons.Solid.EllipsisV,
                            contentDescription = null,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateUp() }) {
                        Icon(
                            modifier = Modifier.size(16.sdp),
                            imageVector = FontAwesomeIcons.Solid.ArrowLeft,
                            contentDescription = null,
                        )
                    }
                },
                title = {}
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.imePadding(),
                onClick = {
                    viewModel.onEvent(
                        AddEditNoteEvent.SaveNote(
                            richTextState.toHtml()
                        )
                    )
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(
                    modifier = Modifier.size(20.sdp),
                    imageVector = FontAwesomeIcons.Regular.Save,
                    contentDescription = "Save Note"
                )
            }
        }) { padding ->
        //making a Row to select colors
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(noteBgAnimation.value.copy(alpha = .4f))
                .padding(vertical = 12.sdp, horizontal = 8.sdp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Note.colors.forEach { colorInt ->
                    val color = Color(colorInt)

                    Box(
                        modifier = Modifier.size(30.sdp).shadow(15.sdp, CircleShape)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = 2.sdp,
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
                    .padding(top = 12.sdp)
            )
            Spacer(Modifier.height(12.sdp))

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

            Spacer(Modifier.height(12.sdp))

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
                    placeholderColor = Color.White.copy(alpha = .6f),
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


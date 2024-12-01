package com.devansh.noteapp.ui.composable.add_edit_note

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.devansh.noteapp.domain.model.Note
import com.devansh.noteapp.ui.composable.core.Constants
import com.devansh.noteapp.ui.components.HintUI
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

@Composable
fun AddEditScreenContent(
    viewModel: AddEditNoteViewModel, onNavigateUp: () -> Unit
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value
    val noteBgAnimation = remember { Animatable(Color(viewModel.noteColor.value)) }

    val scope = rememberCoroutineScope()

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

    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = {
                viewModel.onEvent(AddEditNoteEvent.SaveNote)
            }, containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Save Note")
        }
    }) { padding ->
        //making a Row to select colors
        Column(
            modifier = Modifier.fillMaxSize().background(noteBgAnimation.value).padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //make circles for each color we have
                Note.colors.forEach { colorInt ->
                    val color = Color(colorInt)

                    Box(modifier = Modifier.size(50.dp).shadow(15.dp, CircleShape).clip(CircleShape)
                        .background(color).border(
                            width = 4.dp,
                            color = if (viewModel.noteColor.value == colorInt) {
                                Color.Black  //color is selected
                            } else {
                                Color.Transparent  //color is deselected
                            },
                            shape = CircleShape
                        ).clickable {
                            scope.launch {
                                noteBgAnimation.animateTo(
                                    targetValue = Color(colorInt),
                                    animationSpec = tween(durationMillis = 500)
                                )
                            }

                            viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                        }

                    )
                }
            }

            Spacer(Modifier.height(16.dp))

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
                textStyle = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(16.dp))

            HintUI(
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                },
                isHintVisible = contentState.isHintVisible,
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}

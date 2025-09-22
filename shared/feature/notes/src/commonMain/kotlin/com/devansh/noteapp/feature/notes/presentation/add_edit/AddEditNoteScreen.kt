package com.devansh.noteapp.feature.notes.presentation.add_edit

import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devansh.noteapp.core.designsystem.components.button.BackButton
import com.devansh.noteapp.core.designsystem.components.colorPickerBottomSheet.ColorPickerBottomSheet
import com.devansh.noteapp.core.designsystem.components.formateToolbar.FormattingToolBar
import com.devansh.noteapp.core.designsystem.resources.NoteAppDrawables
import com.devansh.noteapp.core.designsystem.utils.LocalDeviceConfiguration
import com.devansh.noteapp.core.utils.BooleanCBF
import com.devansh.noteapp.core.utils.UnitCBF
import com.devansh.noteapp.core.utils.clipEntryOf
import com.devansh.noteapp.data.models.dto.Category
import com.devansh.noteapp.feature.notes.presentation.category.ModifyFolderDialog
import com.devansh.noteapp.feature.notes.presentation.notes.NoteMenuBottomSheet
import com.dokar.sonner.ToastType
import com.dokar.sonner.Toaster
import com.dokar.sonner.ToasterDefaults
import com.dokar.sonner.rememberToasterState
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.mohamedrejeb.calf.ui.sheet.AdaptiveBottomSheet
import com.mohamedrejeb.calf.ui.sheet.rememberAdaptiveSheetState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource


@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class,
    FlowPreview::class
)
@Composable
fun AddEditScreenContent(
    viewModel: AddEditNoteViewModel,
    onNavigateUp: UnitCBF
) {
    val clipboardManager = LocalClipboard.current

    val currentNote by viewModel.currentNote.collectAsStateWithLifecycle()
    val titleState by viewModel.noteTitle
    val selectedBgColor = currentNote.colorRes?.let { Color(it) }
        ?: MaterialTheme.colorScheme.surface

    val richTextState = viewModel.richTextState
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val toasterState = rememberToasterState()
    val sheetState = rememberAdaptiveSheetState()
    val controller = rememberColorPickerController()
    val colorSheetState = rememberAdaptiveSheetState()
    val deviceInfo = LocalDeviceConfiguration.current

    var openBottomSheet by remember { mutableStateOf(false) }
    var showModifyDialog by remember { mutableStateOf(false) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    val openLinkDialog = remember { mutableStateOf(false) }
    val noteBgAnimation = remember(selectedBgColor) { Animatable(selectedBgColor) }

    LaunchedEffect(richTextState.toHtml()) {
        if (richTextState.toHtml() != currentNote.content) {
            viewModel.onEvent(AddEditNoteEvent.OnContentChange(richTextState.toHtml()))
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

                is UiEvent.Navigate -> {
                    if (event.route == null) onNavigateUp()
                }
            }
        }
    }

    ColorPickerBottomSheet(
        isOpen = openBottomSheet,
        onDismiss = { openBottomSheet = false },
        currentColor = currentNote.colorRes,
        onColorChanged = { color ->
            viewModel.onEvent(AddEditNoteEvent.OnColorChange(color))
        },
        sheetState = colorSheetState,
        controller = controller
    )

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(noteBgAnimation.value),
                actions = {
                    FilledTonalIconButton(
                        modifier = Modifier.padding(end = 4.dp).size(width = 52.dp, height = 32.dp),
                        shapes = IconButtonDefaults.shapes(),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ), onClick = { viewModel.onEvent(AddEditNoteEvent.SaveNote) }) {
                        Icon(
                            modifier = Modifier.padding(4.dp),
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save Note"
                        )
                    }
                    FilledTonalIconButton(
                        modifier = Modifier.size(width = 52.dp, height = 32.dp),
                        shapes = IconButtonDefaults.shapes(),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ), onClick = { openBottomSheet = true }) {
                        Icon(
                            modifier = Modifier.padding(4.dp),
                            painter = painterResource(NoteAppDrawables.apparel24px),
                            contentDescription = "Note Skin color"
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
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                    }
                },
                navigationIcon = { BackButton { onNavigateUp() } },
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CategoryDropdownSelector(
                            selectedCategory = selectedCategory,
                            categories = categories,
                            expanded = categoryDropdownExpanded,
                            onExpandedChange = { categoryDropdownExpanded = it },
                            onCategorySelected = { category ->
                                viewModel.onEvent(AddEditNoteEvent.OnCategoryChange(category))
                                categoryDropdownExpanded = false
                            },
                            onAddCategory = {
                                showModifyDialog = true
                                categoryDropdownExpanded = false
                            },
                        )
                        Spacer(modifier = Modifier.padding(start = 8.dp))
                    }
                },
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

            AdaptiveBottomSheet(
                onDismissRequest = { dismissSheet() },
                adaptiveSheetState = sheetState,
                dragHandle = null
            ) {
                NoteMenuBottomSheet(
                    onShareClick = {},
                    onDeleteClick = {
                        viewModel.deleteNoteById()
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
                    }
                )
            }
        }

        if (showModifyDialog) {
            ModifyFolderDialog(
                category = Category(),
                onDismissRequest = { showModifyDialog = false })
            { viewModel.addCategory(it) }
        }

        Column(
            modifier = Modifier.padding(padding).fillMaxSize()
                .background(noteBgAnimation.value.copy(alpha = .8f)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = titleState.text,
                onValueChange = { viewModel.onEvent(AddEditNoteEvent.OnTitleChange(it)) },
                placeholder = { Text(titleState.hint) },
                colors = OutlinedTextFieldDefaults.colors(
                    errorBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                ),
                textStyle = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 12.dp).widthIn(max = 720.dp, min = Dp.Infinity)
            )

            RichTextEditor(
                state = richTextState,
                placeholder = { Text(text = "# write note content here") },
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = RichTextEditorDefaults.richTextEditorColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    placeholderColor = richTextState.currentSpanStyle.color.copy(alpha = .6f),
                ),
                modifier = Modifier.widthIn(max = 720.dp, min = Dp.Infinity).weight(1f)
            )
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            FormattingToolBar(
                state = richTextState,
                openLinkDialog = openLinkDialog,
                deviceInfo = deviceInfo,
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(4.dp)
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(25))
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
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

@Composable
fun CategoryDropdownSelector(
    selectedCategory: Category,
    categories: List<Category>,
    expanded: Boolean,
    onExpandedChange: BooleanCBF,
    onCategorySelected: (Category) -> Unit,
    onAddCategory: UnitCBF
) {
    Box {
        Box(
            modifier = Modifier
                .background(
                    color = Color(selectedCategory.color!!),
                    shape = RoundedCornerShape(20.dp)
                )
                .clickable { onExpandedChange(true) }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = when {
                        categories.isEmpty() -> ""
                        else -> selectedCategory.name
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        onCategorySelected(category)
                        onExpandedChange(false)
                    }
                )
            }
            TextButton(onClick = onAddCategory) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "add category")
                Text(
                    text = "Add Category",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

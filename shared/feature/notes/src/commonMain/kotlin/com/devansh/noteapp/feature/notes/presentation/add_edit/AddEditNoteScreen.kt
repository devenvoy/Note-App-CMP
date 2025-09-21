package com.devansh.noteapp.feature.notes.presentation.add_edit

import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devansh.noteapp.core.designsystem.components.HintUI
import com.devansh.noteapp.core.designsystem.components.button.BackButton
import com.devansh.noteapp.core.designsystem.components.formateToolbar.FormattingToolBar
import com.devansh.noteapp.core.designsystem.resources.NoteAppDrawables
import com.devansh.noteapp.core.utils.DeviceConfiguration
import com.devansh.noteapp.core.utils.UnitCBF
import com.devansh.noteapp.core.utils.clipEntryOf
import com.devansh.noteapp.feature.notes.presentation.notes.NoteMenuBottomSheet
import com.dokar.sonner.ToastType
import com.dokar.sonner.Toaster
import com.dokar.sonner.ToasterDefaults
import com.dokar.sonner.rememberToasterState
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.mohamedrejeb.calf.ui.sheet.AdaptiveBottomSheet
import com.mohamedrejeb.calf.ui.sheet.rememberAdaptiveSheetState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class,
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
    val selectedBgColor = currentNote.colorRes

    val scope = rememberCoroutineScope()
    val toasterState = rememberToasterState()
    val richTextState = rememberRichTextState()
    val sheetState = rememberModalBottomSheetState()
    val windowInfo = currentWindowAdaptiveInfo()
    val controller = rememberColorPickerController()
    val colorSheetState = rememberAdaptiveSheetState(true)
    val deviceInfo = DeviceConfiguration.fromWindowSizeClass(windowInfo.windowSizeClass)

    var openBottomSheet by remember { mutableStateOf(false) }
    val openLinkDialog = remember { mutableStateOf(false) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var isUpdatingFromViewModel by remember { mutableStateOf(false) }
    val noteBgAnimation = remember(selectedBgColor) { Animatable(Color(selectedBgColor)) }


    LaunchedEffect(currentNote.content) {
        val currentHtml = richTextState.toHtml()
        if (currentNote.content != currentHtml && !isUpdatingFromViewModel) {
            isUpdatingFromViewModel = true
            richTextState.setHtml(currentNote.content)
            delay(50)
            isUpdatingFromViewModel = false
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { richTextState.toHtml() }
            .drop(1)
            .filter { !isUpdatingFromViewModel }
            .debounce(100)
            .distinctUntilChanged()
            .collect { htmlContent ->
                if (htmlContent != currentNote.content) {
                    viewModel.onEvent(AddEditNoteEvent.OnContentChange(htmlContent))
                }
            }
    }

    LaunchedEffect(Unit) {
        {
            richTextState.config.linkColor = Color.Blue
            richTextState.config.linkTextDecoration = TextDecoration.Underline
            richTextState.config.codeSpanColor = Color.Yellow
            richTextState.config.codeSpanBackgroundColor = Color.Transparent
            richTextState.config.codeSpanStrokeColor = Color.LightGray
        }
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

    if (openBottomSheet) {
        AdaptiveBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            adaptiveSheetState = colorSheetState,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp),
                    controller = controller,
                    initialColor = Color(selectedBgColor),
                    onColorChanged = { colorEnvelope: ColorEnvelope ->
                        viewModel.onEvent(
                            AddEditNoteEvent.OnColorChange(
                                colorEnvelope.color.toArgb().toLong()
                            )
                        )
                    }
                )
                AlphaSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .height(35.dp)
                        .align(Alignment.CenterHorizontally),
                    controller = controller,
                )

                BrightnessSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .height(35.dp)
                        .align(Alignment.CenterHorizontally),
                    controller = controller,
                )

            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(noteBgAnimation.value),
                actions = {
                    /*     FilledTonalIconButton(
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
                         }*/
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
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
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

        Column(
            modifier = Modifier.padding(padding).fillMaxSize()
                .background(noteBgAnimation.value.copy(alpha = .8f))
        ) {

            HintUI(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = { viewModel.onEvent(AddEditNoteEvent.OnTitleChange(it)) },
                onFocusChange = { viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it)) },
                isHintVisible = titleState.isHintVisible,
                singleLine = false,
                textStyle = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp)
                    .widthIn(max = 720.dp, min = Dp.Infinity)
            )

            Spacer(Modifier.height(12.dp))

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
                    .background(MaterialTheme.colorScheme.surface)
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

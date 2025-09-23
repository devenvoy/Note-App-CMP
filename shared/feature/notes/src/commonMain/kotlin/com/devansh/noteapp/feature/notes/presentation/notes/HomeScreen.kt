package com.devansh.noteapp.feature.notes.presentation.notes

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devansh.noteapp.core.designsystem.SettingsState
import com.devansh.noteapp.core.designsystem.components.EmptyScreen
import com.devansh.noteapp.core.designsystem.components.ExpandableSearchView
import com.devansh.noteapp.core.designsystem.components.button.SecondaryOutlinedButton
import com.devansh.noteapp.core.designsystem.resources.NoteAppDrawables
import com.devansh.noteapp.core.designsystem.theme.LocalAppTheme
import com.devansh.noteapp.core.utils.LongCBF
import com.devansh.noteapp.core.utils.StringCBF
import com.devansh.noteapp.core.utils.UnitCBF
import com.devansh.noteapp.core.utils.clipEntryOf
import com.devansh.noteapp.data.models.dto.NoteResponse
import com.dokar.sonner.ToastType
import com.dokar.sonner.Toaster
import com.dokar.sonner.ToasterDefaults
import com.dokar.sonner.rememberToasterState
import com.mohamedrejeb.calf.ui.sheet.AdaptiveBottomSheet
import com.mohamedrejeb.calf.ui.sheet.rememberAdaptiveSheetState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3AdaptiveApi::class
)
@Composable
fun HomeScreenContent(
    viewModel: HomeScreenViewModel,
    settingsState: SettingsState,
    onNavigateToAddEditNote: LongCBF,
    onNavigateToSettings: UnitCBF,
    onShareText: StringCBF,
) {
    val theme = LocalAppTheme.current
    val clipboard = LocalClipboard.current

    val scope = rememberCoroutineScope()
    val toasterState = rememberToasterState()
    val state = rememberPullToRefreshState()
    val sheetState = rememberAdaptiveSheetState()

    val noteState by viewModel.noteState.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }

    var selectedNoteResponse by remember { mutableStateOf<NoteResponse?>(null) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }

    val onRefresh: UnitCBF = {
        isRefreshing = true
        scope.launch {
            delay(500)
            viewModel.getAllNotes()
            isRefreshing = false
        }
    }

    val scaleFraction = {
        if (isRefreshing) {
            1f
        } else {
            LinearOutSlowInEasing.transform(state.distanceFraction).coerceIn(0f, 1f)
        }
    }

    Scaffold(
        modifier = Modifier.pullToRefresh(
            state = state,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
        ), topBar = {
            ExpandableSearchView(
                modifier = Modifier.widthIn(max = 600.dp, min = Dp.Infinity),
                expandedInitially = noteState.isSearchActive,
                onExpandedChanged = { b -> viewModel.onToggleSearch() },
                searchDisplay = noteState.searchText,
                onSearchDisplayChanged = viewModel::onSearchTextChange,
                onSearch = { viewModel.onSearchTextChange("") },
            ) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        actionIconContentColor = MaterialTheme.colorScheme.primary
                    ),
                    title = {
                        Text(
                            text = "Notes",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    },
                    actions = {
                        IconButton(onClick = viewModel::onToggleSearch) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "search")
                    }
                        IconButton(onClick = onNavigateToSettings) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "setting"
                            )
                    }
                })
            }
        }) {

        if (isBottomSheetVisible && selectedNoteResponse != null) {
            val richContent = rememberRichTextState().setHtml(selectedNoteResponse?.content ?: "")
            val dismissSheet = {
                scope.launch { sheetState.hide() }
                isBottomSheetVisible = false
                selectedNoteResponse = null
            }

            AdaptiveBottomSheet(
                onDismissRequest = { dismissSheet() },
                adaptiveSheetState = sheetState,
                dragHandle = null
            ) {
                NoteMenuBottomSheet(
                    onEditClick = {
                        onNavigateToAddEditNote(selectedNoteResponse?.id ?: 0); dismissSheet()
                    },
                    onShareClick = { onShareText("${selectedNoteResponse?.title} \n\n ${richContent.toText()}") },
                    onDeleteClick = {
                        selectedNoteResponse?.noteId?.let {
                            viewModel.deleteNoteById(selectedNoteResponse?.noteId!!)
                            toasterState.show(
                                message = "Note deleted successfully",
                                duration = ToasterDefaults.DurationLong,
                                type = ToastType.Error
                            )
                        };dismissSheet()
                }, onCopyClick = {
                        scope.launch { clipboard.setClipEntry(clipEntryOf(AnnotatedString("${selectedNoteResponse?.title} \n\n ${richContent.toText()}").text)) }
                        toasterState.show(message = "Copied to clipboard", type = ToastType.Info)
                        dismissSheet()
                    }
                )
            }
        }

        Box(Modifier.padding(it)) {
            if (noteState.noteResponses.isEmpty()) {
                if (noteState.isSearchActive) {
                    EmptyScreen(
                        "\"No Notes Found !!\"",
                        image = if (theme.dark) NoteAppDrawables.noResults else NoteAppDrawables.noResultsLight,
                    )
                } else {
                    EmptyScreen(
                        text = "\"No Notes !!\"",
                        image = if (theme.dark) NoteAppDrawables.noConversation else NoteAppDrawables.noConversationLight,
//                        buttonState = Pair("Get Started") { onNavigateToAddEditNote(null) }
                    )
                }
            } else {
                NoteScreenContent(
                    state = noteState,
                    onNavigateToAddEditNote = onNavigateToAddEditNote,
                    overflow = settingsState.enumOverflowStyle.toTextOverFlow(),
                    textAlign = settingsState.titleAlignment.toTextAlign(),
                    maxLines = settingsState.enumContentSize.toMaxLines(),
                    isGridLayout = !settingsState.isListView,
                    onLongPress = {
                        selectedNoteResponse = it
                        isBottomSheetVisible = true
                    }
                )
            }

            Toaster(
                modifier = Modifier.navigationBarsPadding(),
                state = toasterState,
                richColors = true,
                darkTheme = isSystemInDarkTheme(),
                showCloseButton = true,
                alignment = Alignment.BottomCenter,
            )

            Box(
                Modifier.align(Alignment.TopCenter)
                    .graphicsLayer {
                        scaleX = scaleFraction()
                        scaleY = scaleFraction()
                    },
            ) {
                PullToRefreshDefaults.LoadingIndicator(
                    state = state,
                    isRefreshing = isRefreshing,
                    containerColor = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteMenuBottomSheet(
    onCopyClick: UnitCBF,
    onShareClick: UnitCBF,
    onDeleteClick: UnitCBF,
    onEditClick: UnitCBF? = null,
) {
    Column(
        modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(16.dp)
    ) {

        BottomSheetDefaults.DragHandle(Modifier.width(50.dp).align(Alignment.CenterHorizontally))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

            if (onEditClick != null) {
                BottomSheetOptionItem("Edit", NoteAppDrawables.icMenuEdit, onEditClick)
            }

            // copy
            BottomSheetOptionItem("Copy", NoteAppDrawables.icMenuCopy, onCopyClick)

            // Share
            BottomSheetOptionItem("Share", NoteAppDrawables.icMenuShare, onShareClick)

            HorizontalDivider(thickness = 2.dp)

            // delete
            BottomSheetOptionItem("Delete", NoteAppDrawables.icMenuDelete, onDeleteClick)

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun BottomSheetOptionItem(
    text: String, drawableResource: DrawableResource, onClick: UnitCBF
) {
    SecondaryOutlinedButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        contentPadding = PaddingValues(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(.5f)),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Image(
                modifier = Modifier.size(32.dp),
                painter = painterResource(drawableResource),
                contentDescription = text
            )

            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = TextStyle(fontSize = 16.sp, fontWeight = W500)
            )
        }
    }
}
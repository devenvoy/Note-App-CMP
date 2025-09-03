package com.devansh.noteapp.ui.screens.home

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.devansh.noteapp.di.platform_di.clipEntryOf
import com.devansh.noteapp.di.platform_di.shareText
import com.devansh.noteapp.domain.model.Note
import com.devansh.noteapp.domain.utils.UnitCBF
import com.devansh.noteapp.navigation.NavRoute
import com.devansh.noteapp.ui.components.EmptyScreen
import com.devansh.noteapp.ui.components.ExpandableSearchView
import com.devansh.noteapp.ui.components.SecondaryOutlinedButton
import com.devansh.noteapp.ui.screens.core.ListType
import com.devansh.noteapp.ui.screens.home.notes.NoteScreenContent
import com.devansh.noteapp.ui.theme.localTheme
import com.dokar.sonner.ToastType
import com.dokar.sonner.Toaster
import com.dokar.sonner.ToasterDefaults
import com.dokar.sonner.rememberToasterState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import network.chaintech.sdpcomposemultiplatform.ssp
import note_app_cmp.composeapp.generated.resources.Res
import note_app_cmp.composeapp.generated.resources.ic_menu_copy
import note_app_cmp.composeapp.generated.resources.ic_menu_delete
import note_app_cmp.composeapp.generated.resources.ic_menu_edit
import note_app_cmp.composeapp.generated.resources.ic_menu_share
import note_app_cmp.composeapp.generated.resources.no_results
import note_app_cmp.composeapp.generated.resources.no_results_light
import note_app_cmp.composeapp.generated.resources.no_task
import note_app_cmp.composeapp.generated.resources.no_task_light
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.homeScreen(mainNavController: NavHostController) {
    composable<NavRoute.HomeScreen> {
        val homeScreenModel = koinViewModel<HomeScreenViewModel>()
        HomeScreenContent(
            homeScreenModel = homeScreenModel,
            onNavigateToAddEditNote = { mainNavController.navigate(NavRoute.AddNote(it)) },
            goToSettings = { mainNavController.navigate(NavRoute.Setting) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreenContent(
    homeScreenModel: HomeScreenViewModel,
    onNavigateToAddEditNote: (String?) -> Unit,
    goToSettings: UnitCBF,
) {
    val theme = localTheme.current!!
    val clipboard = LocalClipboard.current

    val scope = rememberCoroutineScope()
    val toasterState = rememberToasterState()
    val state = rememberPullToRefreshState()
    val sheetState = rememberModalBottomSheetState()

    val noteState by homeScreenModel.noteState.collectAsState()
    var isRefreshing by homeScreenModel.isRefreshing

    var selectedNote by remember { mutableStateOf<Note?>(null) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }

    val onRefresh: () -> Unit = {
        isRefreshing = true
        scope.launch {
            delay(500)
            homeScreenModel.getAllNotes()
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
        modifier =
            Modifier.pullToRefresh(
                state = state,
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
            ),
        topBar = {
            ExpandableSearchView(
                modifier = Modifier.fillMaxWidth(),
                expandedInitially = noteState.isSearchActive,
                onExpandedChanged = { b -> homeScreenModel.onToggleSearch() },
                searchDisplay = noteState.searchText,
                onSearchDisplayChanged = homeScreenModel::onSearchTextChange,
                onSearch = { homeScreenModel.onSearchTextChange("") },
            ) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        actionIconContentColor = MaterialTheme.colorScheme.primary
                    ),
                    title = { Text(text = "Notes", fontSize = 16.ssp) },
                    actions = {
                        IconButton(onClick = homeScreenModel::onToggleSearch) {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = "search")
                        }
                        IconButton(onClick = goToSettings) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "setting"
                            )
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if (noteState.notes.isNotEmpty()) {
                FloatingActionButton(
                    modifier = Modifier.imePadding(),
                    onClick = { onNavigateToAddEditNote(null) }
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.Add,
                        contentDescription = "Save Note"
                    )
                }
            }
        }) {

        if (isBottomSheetVisible && selectedNote != null) {
            val richContent = rememberRichTextState().setHtml(selectedNote?.content ?: "")
            val dismissSheet = {
                scope.launch { sheetState.hide() }
                isBottomSheetVisible = false
                selectedNote = null
            }

            ModalBottomSheet(
                onDismissRequest = { dismissSheet() },
                sheetState = sheetState,
                tonalElevation = 0.dp,
                dragHandle = null
            ) {
                NoteMenuBottomSheet(
                    onEditClick = {
                        onNavigateToAddEditNote(selectedNote?.id)
                        dismissSheet()
                    },
                    onShareClick = {
                        shareText(
                            text = "${selectedNote?.title} \n\n ${richContent.toText()}",
                            mimeType = "plain/text"
                        )
                    },
                    onDeleteClick = {
                        homeScreenModel.deleteNoteById(selectedNote?.id!!)
                        toasterState.show(
                            message = "Note deleted successfully",
                            duration = ToasterDefaults.DurationLong,
                            type = ToastType.Error
                        )
                        dismissSheet()
                    },
                    onCopyClick = {
                        scope.launch {
                            clipboard.setClipEntry(clipEntryOf(AnnotatedString("${selectedNote?.title} \n\n ${richContent.toText()}").text))
                        }

                        toasterState.show(
                            message = "Copied to clipboard",
                            type = ToastType.Info
                        )
                        dismissSheet()
                    },
                    showEditOption = true
                )
            }
        }

        Box(Modifier.padding(it)) {
            if (noteState.notes.isEmpty()) {
                if (noteState.isSearchActive) {
                    EmptyScreen(
                        "\"No Notes Found !!\"",
                        image = if (theme.dark) Res.drawable.no_results else Res.drawable.no_results_light,
                    )
                } else {
                    EmptyScreen(
                        text = "\"No Notes !!\"",
                        image = if (theme.dark) Res.drawable.no_task else Res.drawable.no_task_light,
                        buttonState = Pair("Get Started") { onNavigateToAddEditNote(null) }
                    )
                }
            } else {
                NoteScreenContent(
                    state = noteState,
                    onNavigateToAddEditNote = onNavigateToAddEditNote,
                    isGridLayout = homeScreenModel.isGridLayout.collectAsState().value == ListType.GRID,
                    onLongPress = {
                        selectedNote = it
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
    onEditClick: UnitCBF,
    onCopyClick: UnitCBF,
    onShareClick: UnitCBF,
    onDeleteClick: UnitCBF,
    showEditOption: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {

        BottomSheetDefaults.DragHandle(Modifier.width(50.dp).align(Alignment.CenterHorizontally))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

            if (showEditOption) {
                BottomSheetOptionItem("Edit", Res.drawable.ic_menu_edit, onEditClick)
            }

            // copy
            BottomSheetOptionItem("Copy", Res.drawable.ic_menu_copy, onCopyClick)

            // Share
            BottomSheetOptionItem("Share", Res.drawable.ic_menu_share, onShareClick)

            HorizontalDivider(thickness = 2.dp)

            // delete
            BottomSheetOptionItem("Delete", Res.drawable.ic_menu_delete, onDeleteClick)

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun BottomSheetOptionItem(
    text: String,
    drawableResource: DrawableResource,
    onClick: UnitCBF
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
package com.devansh.noteapp.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.devansh.noteapp.di.platform_di.isDesktop
import com.devansh.noteapp.di.platform_di.shareText
import com.devansh.noteapp.domain.model.Note
import com.devansh.noteapp.domain.repo.AppCacheSetting
import com.devansh.noteapp.domain.utils.koinScreenModel
import com.devansh.noteapp.ui.components.ExpandableSearchView
import com.devansh.noteapp.ui.components.MyCustomIndicator
import com.devansh.noteapp.ui.components.SecondaryOutlinedButton
import com.devansh.noteapp.ui.screens.SettingScreen
import com.devansh.noteapp.ui.screens.add_edit_note.AddEditNoteScreen
import com.devansh.noteapp.ui.screens.core.ListType
import com.devansh.noteapp.ui.screens.home.notes.NoteScreenContent
import com.devansh.noteapp.ui.theme.getMontBFont
import com.dokar.sonner.ToastType
import com.dokar.sonner.Toaster
import com.dokar.sonner.ToasterDefaults
import com.dokar.sonner.rememberToasterState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Plus
import io.ktor.util.Platform
import kotlinx.coroutines.launch
import network.chaintech.sdpcomposemultiplatform.ssp
import note_app_cmp.composeapp.generated.resources.Res
import note_app_cmp.composeapp.generated.resources.ic_menu_copy
import note_app_cmp.composeapp.generated.resources.ic_menu_delete
import note_app_cmp.composeapp.generated.resources.ic_menu_edit
import note_app_cmp.composeapp.generated.resources.ic_menu_share
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val homeScreenModel = koinScreenModel<HomeScreenModel>()
        val pref = koinInject<AppCacheSetting>()
        HomeScreenContent(
            homeScreenModel = homeScreenModel,
            onNavigateToAddEditNote = {
                navigator.push(AddEditNoteScreen(it))
            },
            goToSettings = {
                navigator.push(SettingScreen())
            },
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeScreenContent(
        homeScreenModel: HomeScreenModel,
        onNavigateToAddEditNote: (Long) -> Unit,
        goToSettings: () -> Unit,
    ) {
        val scope = rememberCoroutineScope()
        val noteState by homeScreenModel.noteState.collectAsState()
        val state = rememberPullToRefreshState()
        val clipboardManager = LocalClipboardManager.current
        val sheetState = rememberModalBottomSheetState()
        var selectedNote by remember { mutableStateOf<Note?>(null) }
        var isBottomSheetVisible by remember { mutableStateOf(false) }
        val toasterState = rememberToasterState()

        Scaffold(topBar = {
            ExpandableSearchView(
                modifier = Modifier.fillMaxWidth(),
                expandedInitially = noteState.isSearchActive,
                onExpandedChanged = { b -> homeScreenModel.onToggleSearch() },
                searchDisplay = noteState.searchText,
                onSearchDisplayChanged = {
                    homeScreenModel.onSearchTextChange(it)
                },
                onSearch = {
                    homeScreenModel.onSearchTextChange("")
                },
            ) {
                TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.primary,
                ), title = {
                    Text(text = "Notes", fontSize = 16.ssp, fontFamily = getMontBFont())
                }, actions = {
                    IconButton(onClick = {
                        homeScreenModel.onToggleSearch()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Search, contentDescription = "search"
                        )
                    }
                    IconButton(onClick = goToSettings) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "setting"
                        )
                    }
                })
            }
        },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.imePadding(),
                    onClick = { onNavigateToAddEditNote(-1) }
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.Add,
                        contentDescription = "Save Note"
                    )
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
                    onDismissRequest = {
                        dismissSheet()
                    },
                    sheetState = sheetState,
                    tonalElevation = 0.dp,
                    dragHandle = null
                ) {
                    NoteMenuBottomSheet(
                        onEditClick = {
                            onNavigateToAddEditNote(selectedNote?.id ?: -1)
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
                                "Note deleted successfully",
                                duration = ToasterDefaults.DurationLong,
                                type = ToastType.Error
                            )
                            dismissSheet()
                        },
                        onCopyClick = {
                            clipboardManager.setText(
                                AnnotatedString("${selectedNote?.title} \n\n ${richContent.toText()}")
                            )
                            toasterState.show(
                                "Copied to clipboard",
                                duration = ToasterDefaults.DurationShort,
                                type = ToastType.Info
                            )
                            dismissSheet()
                        },
                        showEditOption = true
                    )
                }
            }

            PullToRefreshBox(
                modifier = Modifier.padding(it).fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                isRefreshing = homeScreenModel.isRefreshing.value,
                onRefresh = { homeScreenModel.getAllNotes() },
                state = state,
                indicator = {
                    MyCustomIndicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        isRefreshing = homeScreenModel.isRefreshing.value,
                        state = state
                    )
                },
            ) {
                NoteScreenContent(
                    state = noteState,
                    onNavigateToAddEditNote = onNavigateToAddEditNote,
                    isGridLayout = homeScreenModel.isGridLayout.collectAsState().value == ListType.GRID,
                    onLongPress = {
                        selectedNote = it
                        isBottomSheetVisible = true
                    }
                )

                Toaster(
                    modifier = Modifier.navigationBarsPadding(),
                    state = toasterState,
                    richColors = true,
                    darkTheme = isSystemInDarkTheme(),
                    showCloseButton = true,
                    alignment = Alignment.BottomCenter,
                )
            }
        }
    }
}


@Composable
fun NoteMenuBottomSheet(
    onEditClick: () -> Unit,
    onCopyClick: () -> Unit,
    onShareClick: () -> Unit,
    onDeleteClick: () -> Unit,
    showEditOption: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .navigationBarsPadding()
            .padding(16.dp)
    ) {

        Box(
            modifier = Modifier
                .padding(bottom = 24.dp)
                .width(50.dp)
                .height(3.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFBBC0C4))
                .align(Alignment.CenterHorizontally)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            if (showEditOption)
                SecondaryOutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onEditClick,
                    contentPadding = PaddingValues(8.dp),
                    border = BorderStroke(1.dp, Color(0xffF9F8FA)),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        Image(
                            modifier = Modifier.size(32.dp),
                            painter = painterResource(Res.drawable.ic_menu_edit),
                            contentDescription = "edit"
                        )

                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            text = "Edit",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = W500,
                                color = Color.Black
                            )
                        )
                    }
                }

            // copy
            SecondaryOutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onCopyClick,
                contentPadding = PaddingValues(8.dp),

                border = BorderStroke(1.dp, Color(0xffF9F8FA)),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Image(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(Res.drawable.ic_menu_copy),
                        contentDescription = "copy"
                    )

                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = "Copy",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = W500,
                            color = Color.Black
                        )
                    )
                }
            }

            // share
            if(isDesktop().not()){
                SecondaryOutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onShareClick,
                    contentPadding = PaddingValues(8.dp),
                    border = BorderStroke(1.dp, Color(0xffF9F8FA)),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        Image(
                            modifier = Modifier.size(32.dp),
                            painter = painterResource(Res.drawable.ic_menu_share),
                            contentDescription = "edit"
                        )

                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            text = "Share",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = W500,
                                color = Color.Black
                            )
                        )
                    }
                }
            }

            HorizontalDivider(
                thickness = 1.dp, color = Color(0xffF5F1F1)
            )

            // delete
            SecondaryOutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onDeleteClick,
                contentPadding = PaddingValues(8.dp),
                border = BorderStroke(1.dp, Color(0xffF9F8FA)),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Image(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(Res.drawable.ic_menu_delete),
                        contentDescription = "edit"
                    )

                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = "Delete",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = W500,
                            color = Color.Black
                        )
                    )
                }
            }
        }
    }
}
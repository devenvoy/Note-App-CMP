package com.devansh.noteapp.ui.screens.categoryFolder

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.outlined.CreateNewFolder
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DriveFileRenameOutline
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.devansh.noteapp.domain.model.Category
import com.devansh.noteapp.domain.utils.UnitCBF
import com.devansh.noteapp.navigation.NavRoute
import com.devansh.noteapp.ui.components.EmptyScreen
import com.devansh.noteapp.ui.components.dialog.ModifyFolderDialog
import com.devansh.noteapp.ui.components.dialog.WarningDialog
import com.devansh.noteapp.ui.theme.LocalAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import noteapp.composeapp.generated.resources.Res
import noteapp.composeapp.generated.resources.delete
import noteapp.composeapp.generated.resources.deleting_a_folder_will_also_delete_all_the_notes_it_contains_and_they_cannot_be_restored_do_you_want_to_continue
import noteapp.composeapp.generated.resources.folders
import noteapp.composeapp.generated.resources.modify
import noteapp.composeapp.generated.resources.navigate_back
import noteapp.composeapp.generated.resources.no_task
import noteapp.composeapp.generated.resources.no_task_light
import noteapp.composeapp.generated.resources.note
import noteapp.composeapp.generated.resources.notes
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


fun NavGraphBuilder.categoryScreen(mainNavController: NavHostController, navigateToHome: UnitCBF) {
    composable<NavRoute.Category> {
        CategoryScreen(navigateToHome)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CategoryScreen(navigateUp: UnitCBF) {

    val theme = LocalAppTheme.current
    val viewModel = koinViewModel<CategoryViewModel>()

    val categories: List<Category> by viewModel.categories.collectAsStateWithLifecycle()

    var showAddFolderDialog by rememberSaveable { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.folders),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBackIos,
                            contentDescription = stringResource(Res.string.navigate_back)
                        )
                    }
                },
                actions = {
                    FilledIconButton(
                        onClick = { showAddFolderDialog = true },
                        shapes = IconButtonShapes(
                            shape = ShapeDefaults.ExtraLarge,
                            pressedShape = ShapeDefaults.Small,
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CreateNewFolder,
                            contentDescription = "Create New Folder"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(scrolledContainerColor = TopAppBarDefaults.topAppBarColors().containerColor),
                scrollBehavior = scrollBehavior
            )
        }) { paddingValues ->

        LazyVerticalGrid(
            modifier = Modifier.padding(horizontal = 16.dp),
            columns = GridCells.Adaptive(360.dp),
            contentPadding = paddingValues,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(categories, key = { it.id ?: it.name }, contentType = { "FolderItem" }) {
                FolderItem(
                    category = it,
                    onModify = { viewModel.updateCategory(it) },
                    onDelete = { viewModel.deleteCategory(it) }
                )
            }
        }

        if (categories.isEmpty()) {
            EmptyScreen(
                modifier = Modifier.padding(paddingValues),
                text = "No Folder",
                image = if (theme.dark) Res.drawable.no_task else Res.drawable.no_task_light,
            )
        }

        if (showAddFolderDialog) {
            ModifyFolderDialog(
                category = Category(),
                onDismissRequest = { showAddFolderDialog = false })
            { viewModel.addCategory(it) }
        }
    }
}

@Composable
fun LazyGridItemScope.FolderItem(
    category: Category,
    onModify: (Category) -> Unit,
    onDelete: UnitCBF,
    colorScheme: ColorScheme = MaterialTheme.colorScheme
) {
    var showModifyDialog by remember { mutableStateOf(false) }
    var showWarningDialog by remember { mutableStateOf(false) }
    val folderColor by remember(category, colorScheme) {
        mutableStateOf(if (category.color != null) Color(category.color) else colorScheme.primary)
    }

    val dismissState = rememberSwipeToDismissBoxState()
    val scope = rememberCoroutineScope()
    var showContextMenu by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    LaunchedEffect(isHovered) {
        delay(100L)
        showContextMenu = isHovered
    }

    SwipeToDismissBox(
        state = dismissState,
        onDismiss = { dismissDirection ->
            when (dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    // Edit action (right swipe)
                    showModifyDialog = true
                    scope.launch { dismissState.reset() }
                }

                SwipeToDismissBoxValue.EndToStart -> {
                    // Delete action (left swipe)
                    showWarningDialog = true
                    scope.launch { dismissState.reset() }
                }

                SwipeToDismissBoxValue.Settled -> {}
            }
        },
        backgroundContent = {
            val direction = dismissState.targetValue
            val progress = dismissState.progress
            val iconOffset = 30.dp * (progress * 1.5f)
            val backgroundColor = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> folderColor.copy(alpha = 0.1f)
                SwipeToDismissBoxValue.EndToStart -> colorScheme.errorContainer
                SwipeToDismissBoxValue.Settled -> Color.Unspecified
            }

            val cornerLeftRadius =
                if (direction == SwipeToDismissBoxValue.StartToEnd) 16.dp * (progress * 6f) else 0.dp
            val cornerRightRadius =
                if (direction == SwipeToDismissBoxValue.EndToStart) 16.dp * (progress * 6f) else 0.dp
            Box(
                Modifier.fillMaxSize()
                    .clip(
                    shape = RoundedCornerShape(
                        topStart = cornerLeftRadius,
                        topEnd = cornerRightRadius,
                        bottomStart = cornerLeftRadius,
                        bottomEnd = cornerRightRadius
                    )
                ).background(backgroundColor).padding(horizontal = 20.dp)
            ) {
                if (direction == SwipeToDismissBoxValue.StartToEnd) Icon(
                    imageVector = Icons.Outlined.DriveFileRenameOutline,
                    contentDescription = null,
                    tint = folderColor,
                    modifier = Modifier.align(Alignment.CenterStart).size(30.dp)
                        .offset { IntOffset(x = iconOffset.roundToPx(), y = 0) })
                if (direction == SwipeToDismissBoxValue.EndToStart) Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = null,
                    tint = colorScheme.onErrorContainer,
                    modifier = Modifier.align(Alignment.CenterEnd).size(30.dp)
                        .offset { IntOffset(x = -iconOffset.roundToPx(), y = 0) })
            }
        },
        modifier = Modifier.padding(top = 12.dp)
            .clip(CardDefaults.elevatedShape)
            .animateItem()
            .hoverable(interactionSource)
            .pointerInput(Unit) { detectTapGestures(onLongPress = { showContextMenu = true }) })
    {
        ElevatedCard {
            Row(
                modifier = Modifier.fillMaxWidth().background(folderColor.copy(alpha = 0.1f))
                    .padding(16.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Folder,
                    contentDescription = "Folder",
                    tint = folderColor,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = category.name, style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold, color = folderColor
                        ), maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                    val notesCountInFolder = category.notesCount.toInt()
                    val text = "$notesCountInFolder ${
                        if (notesCountInFolder == 1 || notesCountInFolder == 0) stringResource(Res.string.note)
                        else stringResource(Res.string.notes)
                    }"
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                }
            }
        }

        DropdownMenu(
            expanded = showContextMenu, onDismissRequest = { showContextMenu = false }) {
            DropdownMenuItem(text = { Text(stringResource(Res.string.modify)) }, leadingIcon = {
                Icon(Icons.Outlined.DriveFileRenameOutline, contentDescription = null)
            }, onClick = {
                showModifyDialog = true
                showContextMenu = false
            })
            DropdownMenuItem(text = { Text(stringResource(Res.string.delete)) }, leadingIcon = {
                Icon(Icons.Outlined.Delete, contentDescription = null)
            }, onClick = {
                showWarningDialog = true
                showContextMenu = false
            })
        }
    }

    if (showWarningDialog) {
        WarningDialog(
            message = stringResource(Res.string.deleting_a_folder_will_also_delete_all_the_notes_it_contains_and_they_cannot_be_restored_do_you_want_to_continue),
            onDismissRequest = { showWarningDialog = false },
            onConfirm = onDelete
        )
    }

    if (showModifyDialog) {
        ModifyFolderDialog(
            category = category, onDismissRequest = { showModifyDialog = false }) {
            onModify(it)
        }
    }
}
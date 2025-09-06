package com.devansh.noteapp.ui.screens.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsSwitch
import com.devansh.noteapp.navigation.NavRoute
import com.devansh.noteapp.ui.components.button.BackButton
import com.devansh.noteapp.ui.components.button.PrimaryButton
import com.devansh.noteapp.ui.screens.core.ListType
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.settingsScreen(navHostController: NavHostController) {
    composable<NavRoute.Setting> {
        val viewModel = koinViewModel<SettingViewModel>()
        SettingScreenContent(
            viewModel = viewModel,
            navigateBack = { navHostController.navigateUp() },
            logOut = {
                viewModel.logOut()
                navHostController.navigate(NavRoute.Auth) {
                    popUpTo(NavRoute.HomeScreen) { inclusive = true }
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreenContent(
    viewModel: SettingViewModel,
    navigateBack: () -> Unit,
    logOut: () -> Unit,
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Column {
                        Text(
                            text = "Settings",
                            style = if (scrollBehavior.state.collapsedFraction >= .75) MaterialTheme.typography.titleLarge else MaterialTheme.typography.headlineMedium
                        )
                        AnimatedVisibility(scrollBehavior.state.collapsedFraction <= .75) {
                            Text(
                                text = "User: " + viewModel.userEmail,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(start = 8.dp),
                                color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                            )
                        }
                    }
                },
                navigationIcon = { BackButton { navigateBack() } },
                actions = {
                    PrimaryButton(
                        contentPadding = PaddingValues(horizontal = 30.dp),
                        onClick = { logOut() }
                    ) { Text("Logout") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
                ),
            )
        }
    ) { ip ->
        Column(
            modifier = Modifier.padding(ip).fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            SettingsGroup(
                enabled = true,
                title = { Text(text = "Server Setting") },
                contentPadding = PaddingValues(horizontal = 8.dp),
            ) {
                SettingsSwitch(
                    state = viewModel.autoSyncDB.collectAsState(true).value,
                    title = { Text(text = "Auto Sync", fontSize = 16.sp, fontWeight = W500) },
                    subtitle = {
                        Text(
                            "Upload any unSynced or updated notes to server automatically before app start",
                            fontSize = 12.sp
                        )
                    },
                    onCheckedChange = viewModel::updateAutoSyncDB
                )
            }

            SettingsGroup(
                enabled = true,
                title = { Text(text = "App Setting") },
                contentPadding = PaddingValues(horizontal = 8.dp),
            ) {
                SettingsSwitch(
                    state = viewModel.listType.collectAsState(ListType.GRID).value == ListType.GRID,
                    title = { Text(text = "Notes Grid", fontSize = 16.sp, fontWeight = W500) },
                    subtitle = { Text("show notes in grid or list", fontSize = 12.sp) },
                    onCheckedChange = {
                        viewModel.updateListType(if (it) ListType.GRID else ListType.LIST)
                    }
                )
            }
        }
    }
}


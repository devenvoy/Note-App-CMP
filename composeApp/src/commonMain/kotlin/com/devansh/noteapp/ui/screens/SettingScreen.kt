package com.devansh.noteapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsSwitch
import com.devansh.noteapp.domain.repo.AppCacheSetting
import com.devansh.noteapp.domain.repo.NoteDataSource
import com.devansh.noteapp.ui.components.PrimaryButton
import com.devansh.noteapp.ui.screens.auth.AuthScreen
import com.devansh.noteapp.ui.screens.core.ListType
import kotlinx.coroutines.launch
import network.chaintech.sdpcomposemultiplatform.ssp
import org.koin.compose.koinInject

class SettingScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val pref = koinInject<AppCacheSetting>()
        val noteRepo = koinInject<NoteDataSource>()
        val scope = rememberCoroutineScope()
        SettingScreenContent(pref,
            navigateBack = { navigator.pop() },
            logOut = {
                pref.logout { scope.launch { noteRepo.emptyNoteTable() } }
                navigator.replace(AuthScreen())
            }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SettingScreenContent(
        pref: AppCacheSetting, navigateBack: () -> Unit,
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
                                "Settings",
                                fontSize = if (scrollBehavior.state.collapsedFraction >= .75) 24.sp else 36.sp
                            )
                            AnimatedVisibility(scrollBehavior.state.collapsedFraction <= .75) {
                                Text(
                                    "User: " + pref.userEmail,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(start = 8.dp),
                                    color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        Icon(
                            modifier = Modifier.padding(4.dp).clickable(onClick = navigateBack),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        scrolledContainerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.primary,
                        navigationIconContentColor = MaterialTheme.colorScheme.primary
                    ),
                )
            }
        ) { ip ->
            Column(
                modifier = Modifier.padding(ip).fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                SettingsGroup(
                    modifier = Modifier,
                    enabled = true,
                    title = { Text(text = "Server Setting") },
                    contentPadding = PaddingValues(horizontal = 8.dp),
                ) {
                    SettingsSwitch(
                        state = pref.observableAutoSyncDB.collectAsState(true).value,
                        title = { Text(text = "Auto Sync", fontSize = 14.ssp, fontWeight = W500) },
                        subtitle = {
                            Text(
                                "Upload any unSynced or updated notes to server automatically before app start",
                                fontSize = 10.ssp
                            )
                        },
                        onCheckedChange = {
                            pref.autoSyncDB = it
                        }
                    )
                }

                SettingsGroup(
                    modifier = Modifier,
                    enabled = true,
                    title = { Text(text = "App Setting") },
                    contentPadding = PaddingValues(horizontal = 8.dp),
                ) {
                    SettingsSwitch(
                        state = pref.observableListType.collectAsState(ListType.GRID).value == ListType.GRID,
                        title = { Text(text = "Notes Grid", fontSize = 14.ssp, fontWeight = W500) },
                        subtitle = { Text("show notes in grid or list", fontSize = 10.ssp) },
                        onCheckedChange = {
                            pref.listType = if (it) 0 else 1
                        }
                    )
                }

                PrimaryButton(
                    modifier = Modifier.padding(top = 60.dp).align(Alignment.CenterHorizontally),
                    contentPadding = PaddingValues(horizontal = 30.dp),
                    onClick = { logOut() }
                ) {
                    Text("Logout")
                }
            }
        }
    }
}

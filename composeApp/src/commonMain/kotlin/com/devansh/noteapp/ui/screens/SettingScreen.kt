package com.devansh.noteapp.ui.screens

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsSwitch
import com.devansh.noteapp.domain.repo.AppCacheSetting
import com.devansh.noteapp.ui.screens.core.ListType
import org.koin.compose.koinInject

class SettingScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val pref = koinInject<AppCacheSetting>()
        SettingScreenContent(pref,
            navigateBack = { navigator.pop() }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SettingScreenContent(pref: AppCacheSetting, navigateBack: () -> Unit) {
        val topAppBarState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                LargeTopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = {
                        Text(
                            "Settings",
                            fontSize = if (scrollBehavior.state.collapsedFraction == 1f) 24.sp else 48.sp
                        )
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
                        title = { Text(text = "Auto Sync") },
                        subtitle = { Text("Sync Database with server") },
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
                        title = { Text(text = "Notes Grid") },
                        subtitle = { Text("show notes in grid or list") },
                        onCheckedChange = {
                            pref.listType = if (it) 0 else 1
                        }
                    )
                }
            }
        }
    }
}

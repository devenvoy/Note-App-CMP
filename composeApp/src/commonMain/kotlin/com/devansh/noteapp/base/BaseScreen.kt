package com.devansh.noteapp.base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Feed
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.automirrored.outlined.Feed
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.devansh.noteapp.core.designsystem.BottomNavItem
import com.devansh.noteapp.core.designsystem.resources.NoteAppStrings
import com.devansh.noteapp.core.designsystem.theme.LocalAppTheme
import com.devansh.noteapp.core.designsystem.utils.LocalDeviceConfiguration
import com.devansh.noteapp.core.utils.DeviceConfiguration.DESKTOP
import com.devansh.noteapp.core.utils.DeviceConfiguration.MOBILE_LANDSCAPE
import com.devansh.noteapp.core.utils.DeviceConfiguration.MOBILE_PORTRAIT
import com.devansh.noteapp.core.utils.DeviceConfiguration.TABLET_LANDSCAPE
import com.devansh.noteapp.core.utils.DeviceConfiguration.TABLET_PORTRAIT
import com.devansh.noteapp.feature.settings.presentation.BaseScreenViewModel
import com.devansh.noteapp.navigation.NavRoute
import com.devansh.noteapp.navigation.categoryScreen
import com.devansh.noteapp.navigation.homeScreen
import com.devansh.noteapp.navigation.settingsScreen
import org.jetbrains.compose.resources.stringResource


fun NavGraphBuilder.baseScreen(
    baseScreenViewModel: BaseScreenViewModel,
    mainNavController: NavHostController
) {
    composable<NavRoute.BaseScreen> {
        val bottomNavController = rememberNavController()
        BaseScreen(baseScreenViewModel, mainNavController, bottomNavController)
    }
}

@Composable
private fun BaseScreen(
    viewModel: BaseScreenViewModel,
    mainNavController: NavHostController,
    bottomNavController: NavHostController
) {
    val theme = LocalAppTheme.current
    val deviceConfiguration = LocalDeviceConfiguration.current

    var isRailExpanded by rememberSaveable { mutableStateOf(false) }
    val settingsState by viewModel.settingsStateFlow.collectAsStateWithLifecycle()
    val currentBackStack by bottomNavController.currentBackStackEntryAsState()

    val bottomNavItems = listOf(
        BottomNavItem(
            title = NoteAppStrings.notes,
            defaultIcon = Icons.AutoMirrored.Outlined.Feed,
            selectedIcon = Icons.AutoMirrored.Filled.Feed,
            route = NavRoute.HomeScreen,
        ),
        BottomNavItem(
            title = NoteAppStrings.category,
            defaultIcon = Icons.Filled.Folder,
            selectedIcon = Icons.Filled.FolderOpen,
            route = NavRoute.Category
        ),
        BottomNavItem(
            title = NoteAppStrings.settings,
            defaultIcon = Icons.Outlined.Settings,
            selectedIcon = Icons.Filled.Settings,
            route = NavRoute.Setting
        )
    )

    val layoutType = when (deviceConfiguration) {
        MOBILE_LANDSCAPE -> NavigationSuiteType.ShortNavigationBarMedium
        MOBILE_PORTRAIT -> NavigationSuiteType.NavigationBar
        DESKTOP,
        TABLET_PORTRAIT,
        TABLET_LANDSCAPE -> if (isRailExpanded) NavigationSuiteType.WideNavigationRailExpanded else NavigationSuiteType.WideNavigationRailCollapsed
    }
    val navigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState()

    val navigationSuiteItems = @Composable {
        bottomNavItems.forEach { navItem ->
            val currentDestination = currentBackStack?.destination
            val isSelected =
                currentDestination?.hierarchy?.any { it.hasRoute(navItem.route::class) } == true
            NavigationSuiteItem(
                selected = isSelected,
                onClick = {
                    bottomNavController.navigate(navItem.route) {
                        popUpTo(NavRoute.HomeScreen) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = {
                    Text(
                        text = stringResource(navItem.title).replaceFirstChar { it.uppercase() },
                        textAlign = TextAlign.Center,
                        color = if (isSelected) theme.colorScheme.primary else theme.colorScheme.onSurface
                    )
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) navItem.selectedIcon else navItem.defaultIcon,
                        contentDescription = stringResource(navItem.title),
                        tint = if (isSelected) theme.colorScheme.primary else theme.colorScheme.onSurfaceVariant
                    )
                },
                navigationSuiteType = layoutType,
            )
        }
    }

    NavigationSuiteScaffold(
        navigationSuiteType = layoutType,
        navigationItemVerticalArrangement = Arrangement.Center,
        state = navigationSuiteScaffoldState,
        content = {
            Box {
                NavHost(
                    navController = bottomNavController,
                    startDestination = NavRoute.HomeScreen
                ) {
                    homeScreen(settingsState = settingsState, mainNavController = mainNavController)
                    categoryScreen(mainNavController = mainNavController){
                        bottomNavController.navigateUp()
                    }
                    settingsScreen(
                        baseScreenViewModel = viewModel,
                        navHostController = mainNavController
                    )
                }
            }
        },
        primaryActionContent = {
            Column(
                modifier = Modifier.padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ExtendedFloatingActionButton(
                    onClick = { mainNavController.navigate(NavRoute.AddNote()) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    content = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Note"
                        )
                        AnimatedVisibility(deviceConfiguration.isMobile() || isRailExpanded) {
                            Text(text = "Add Note")
                        }
                    }
                )
                if (!deviceConfiguration.isMobile()) {
                    Box(
                        modifier = Modifier.width(IntrinsicSize.Min),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { isRailExpanded = !isRailExpanded }) {
                            Icon(
                                imageVector = if (isRailExpanded) Icons.AutoMirrored.Filled.MenuOpen else Icons.Default.Menu,
                                contentDescription = "menu"
                            )
                        }
                    }
                }
            }
        },
        primaryActionContentHorizontalAlignment = if (!deviceConfiguration.isMobile()) {
            Alignment.CenterHorizontally
        } else {
            Alignment.End
        },
        navigationItems = navigationSuiteItems
    )
}

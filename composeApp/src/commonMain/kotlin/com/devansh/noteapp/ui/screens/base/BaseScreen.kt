package com.devansh.noteapp.ui.screens.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.devansh.noteapp.core.util.DeviceConfiguration
import com.devansh.noteapp.core.util.DeviceConfiguration.DESKTOP
import com.devansh.noteapp.core.util.DeviceConfiguration.MOBILE_LANDSCAPE
import com.devansh.noteapp.core.util.DeviceConfiguration.MOBILE_PORTRAIT
import com.devansh.noteapp.core.util.DeviceConfiguration.TABLET_LANDSCAPE
import com.devansh.noteapp.core.util.DeviceConfiguration.TABLET_PORTRAIT
import com.devansh.noteapp.navigation.NavRoute
import com.devansh.noteapp.ui.screens.categoryFolder.categoryScreen
import com.devansh.noteapp.ui.screens.home.homeScreen
import com.devansh.noteapp.ui.theme.LocalAppTheme
import org.koin.compose.viewmodel.koinViewModel


fun NavGraphBuilder.baseScreen(mainNavController: NavHostController) {
    composable<NavRoute.BaseScreen> {
        val bottomNavController = rememberNavController()
        BaseScreen(mainNavController, bottomNavController)
    }
}

@Composable
private fun BaseScreen(
    mainNavController: NavHostController,
    bottomNavController: NavHostController
) {
    val viewModel = koinViewModel<BaseScreenViewModel>()
    val theme = LocalAppTheme.current

    var isRailExpanded by rememberSaveable { mutableStateOf(false) }

    val windowAdaptiveInfo = currentWindowAdaptiveInfo()

    val currentBackStack by bottomNavController.currentBackStackEntryAsState()

    val deviceConfiguration =
        DeviceConfiguration.fromWindowSizeClass(windowAdaptiveInfo.windowSizeClass)

    val layoutType = when (deviceConfiguration) {
        MOBILE_LANDSCAPE -> NavigationSuiteType.ShortNavigationBarMedium
        MOBILE_PORTRAIT -> NavigationSuiteType.NavigationBar
        DESKTOP,
        TABLET_PORTRAIT,
        TABLET_LANDSCAPE -> if (isRailExpanded) NavigationSuiteType.WideNavigationRailExpanded else NavigationSuiteType.WideNavigationRailCollapsed
    }
    val navigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState()

    val navigationSuiteItems = @Composable {
        viewModel.bottomNavItems.forEach { navItem ->
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
                        text = navItem.title,
                        textAlign = TextAlign.Center,
                        color = if (isSelected) theme.colorScheme.primary else theme.colorScheme.onSurface
                    )
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) navItem.selectedIcon else navItem.defaultIcon,
                        contentDescription = navItem.title,
                        tint = if (isSelected) theme.colorScheme.primary else theme.colorScheme.onSurfaceVariant
                    )
                },
                navigationSuiteType = layoutType,
            )
        }
    }

    CompositionLocalProvider(LocalDeviceConfiguration provides deviceConfiguration) {
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
                        homeScreen(mainNavController)
                        categoryScreen(mainNavController)
                    }
                }
            },
            primaryActionContent = {
                if (!deviceConfiguration.isMobile()) {
                    IconButton(onClick = { isRailExpanded = !isRailExpanded }) {
                        androidx.compose.material3.Icon(
                            imageVector = if (isRailExpanded) Icons.AutoMirrored.Filled.MenuOpen else Icons.Default.Menu,
                            contentDescription = "menu"
                        )
                    }
                }
            },
            navigationItems = navigationSuiteItems
        )
    }
}

val LocalDeviceConfiguration =
    staticCompositionLocalOf<DeviceConfiguration> { error("No Configuration Provided") }
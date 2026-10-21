package com.example.paintcover.ui

import android.view.HapticFeedbackConstants
import androidx.compose.animation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.paintcover.ui.about.AboutScreen
import com.example.paintcover.ui.history.HistoryScreen
import com.example.paintcover.ui.home.HomeScreen
import com.example.paintcover.ui.navigation.PaintCoverBottomBar
import com.example.paintcover.ui.navigation.Screen
import com.example.paintcover.ui.settings.SettingsScreen
import com.example.paintcover.ui.splash.SplashScreen
import kotlinx.coroutines.launch

@Composable
fun PaintCoverApp(
    viewModel: PaintCoverViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val view = LocalView.current

    val triggerHaptic: () -> Unit = {
        if (uiState.settings.hapticFeedback) {
            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        }
    }

    val triggerErrorHaptic: () -> Unit = {
        if (uiState.settings.hapticFeedback) {
            view.performHapticFeedback(HapticFeedbackConstants.REJECT)
        }
    }

    // React to user notices (e.g. Saved to history, restored, etc.)
    LaunchedEffect(uiState.userNotice) {
        uiState.userNotice?.let { notice ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(notice, withDismissAction = true)
                viewModel.clearUserNotice()
            }
        }
    }

    // Trigger error haptic when error appears
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            triggerErrorHaptic()
        }
    }

    val showBottomBar = currentRoute in Screen.bottomNavScreens.map { it.route }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                PaintCoverBottomBar(
                    currentDestination = currentDestination,
                    onNavigateToRoute = { route ->
                        triggerHaptic()
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    onSplashFinished = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    uiState = uiState,
                    onSurfaceTypeSelected = {
                        triggerHaptic()
                        viewModel.onSurfaceTypeSelected(it)
                    },
                    onInputModeChanged = {
                        triggerHaptic()
                        viewModel.onInputModeSelected(it)
                    },
                    onSurfaceAreaChanged = { viewModel.onSurfaceAreaChanged(it) },
                    onSurfaceAreaUnitChanged = {
                        triggerHaptic()
                        viewModel.onSurfaceAreaUnitChanged(it)
                    },
                    onSurfaceLengthChanged = { viewModel.onSurfaceLengthChanged(it) },
                    onSurfaceLengthUnitChanged = {
                        triggerHaptic()
                        viewModel.onSurfaceLengthUnitChanged(it)
                    },
                    onSurfaceWidthChanged = { viewModel.onSurfaceWidthChanged(it) },
                    onSurfaceWidthUnitChanged = {
                        triggerHaptic()
                        viewModel.onSurfaceWidthUnitChanged(it)
                    },
                    onCoverageChanged = { viewModel.onCoverageChanged(it) },
                    onCoverageUnitChanged = {
                        triggerHaptic()
                        viewModel.onCoverageUnitChanged(it)
                    },
                    onCoatsChanged = {
                        triggerHaptic()
                        viewModel.onCoatsChanged(it)
                    },
                    onWastePercentChanged = {
                        triggerHaptic()
                        viewModel.onWastePercentChanged(it)
                    },
                    onCanSizeChanged = { viewModel.onCanSizeChanged(it) },
                    onCanSizeUnitChanged = {
                        triggerHaptic()
                        viewModel.onCanSizeUnitChanged(it)
                    },
                    onOpeningsEnabledChanged = {
                        triggerHaptic()
                        viewModel.onOpeningsEnabledChanged(it)
                    },
                    onAddDoor = {
                        triggerHaptic()
                        viewModel.addDoor()
                    },
                    onUpdateDoor = { id, w, h, wu, hu ->
                        viewModel.updateDoor(id, w, h, wu, hu)
                    },
                    onRemoveDoor = { id ->
                        triggerHaptic()
                        viewModel.removeDoor(id)
                    },
                    onAddWindow = {
                        triggerHaptic()
                        viewModel.addWindow()
                    },
                    onUpdateWindow = { id, w, h, wu, hu ->
                        viewModel.updateWindow(id, w, h, wu, hu)
                    },
                    onRemoveWindow = { id ->
                        triggerHaptic()
                        viewModel.removeWindow(id)
                    },
                    onSaveToHistory = {
                        triggerHaptic()
                        viewModel.saveCurrentToHistory()
                    },
                    onReset = {
                        triggerHaptic()
                        viewModel.reset()
                    },
                    onValueCopied = { message ->
                        triggerHaptic()
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(message)
                        }
                    }
                )
            }

            composable(Screen.History.route) {
                HistoryScreen(
                    history = uiState.history,
                    confirmDeleteSetting = uiState.settings.confirmDeleteHistory,
                    onRestoreHistory = {
                        triggerHaptic()
                        viewModel.restoreHistory(it)
                    },
                    onDeleteEntry = {
                        triggerHaptic()
                        viewModel.deleteHistory(it)
                    },
                    onClearAll = {
                        triggerHaptic()
                        viewModel.clearAllHistory()
                    },
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    settings = uiState.settings,
                    onUpdateSettings = {
                        triggerHaptic()
                        viewModel.updateSettings(it)
                    },
                    onClearHistory = {
                        triggerHaptic()
                        viewModel.clearAllHistory()
                    },
                    onResetSettings = {
                        triggerHaptic()
                        viewModel.resetSettings()
                    }
                )
            }

            composable(Screen.About.route) {
                AboutScreen()
            }
        }
    }
}

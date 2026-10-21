package com.example.paintcover.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.paintcover.data.model.AppSettings
import com.example.paintcover.data.model.AppTheme
import com.example.paintcover.data.model.DisplayRoundingMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: AppSettings,
    onUpdateSettings: (AppSettings) -> Unit,
    onClearHistory: () -> Unit,
    onResetSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var showClearHistoryConfirm by remember { mutableStateOf(false) }
    var showResetSettingsConfirm by remember { mutableStateOf(false) }

    if (showClearHistoryConfirm) {
        AlertDialog(
            onDismissRequest = { showClearHistoryConfirm = false },
            title = { Text("Clear History") },
            text = { Text("Clear all saved paint calculations?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onClearHistory()
                        showClearHistoryConfirm = false
                    }
                ) {
                    Text("Clear", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearHistoryConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showResetSettingsConfirm) {
        AlertDialog(
            onDismissRequest = { showResetSettingsConfirm = false },
            title = { Text("Reset Settings") },
            text = { Text("Restore all app settings to default values?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onResetSettings()
                        showResetSettingsConfirm = false
                    }
                ) {
                    Text("Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetSettingsConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Appearance Group
            SettingsGroup(title = "Appearance", icon = Icons.Default.Palette) {
                // Theme selection
                Text(
                    text = "App Theme",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AppTheme.values().forEach { themeOption ->
                        FilterChip(
                            selected = settings.theme == themeOption,
                            onClick = { onUpdateSettings(settings.copy(theme = themeOption)) },
                            label = { Text(themeOption.displayName) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                SettingsToggleRow(
                    title = "Dynamic Color",
                    subtitle = "Use Material You wallpaper colors (Android 12+)",
                    checked = settings.dynamicColor,
                    onCheckedChange = { onUpdateSettings(settings.copy(dynamicColor = it)) }
                )
            }

            // Behavior Group
            SettingsGroup(title = "Behavior", icon = Icons.Default.TouchApp) {
                SettingsToggleRow(
                    title = "Haptic Feedback",
                    subtitle = "Tactile feedback on selections and actions",
                    checked = settings.hapticFeedback,
                    onCheckedChange = { onUpdateSettings(settings.copy(hapticFeedback = it)) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                SettingsToggleRow(
                    title = "Animations",
                    subtitle = "Smooth mode and result transitions",
                    checked = settings.animationsEnabled,
                    onCheckedChange = { onUpdateSettings(settings.copy(animationsEnabled = it)) }
                )
            }

            // Number Display Group
            SettingsGroup(title = "Number Display", icon = Icons.Default.Numbers) {
                Text(
                    text = "Decimal Places: ${settings.decimalPlaces}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                val decimalOptions = listOf(0, 1, 2, 3, 4, 6, 8, 10, 12)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    decimalOptions.take(5).forEach { dpVal ->
                        FilterChip(
                            selected = settings.decimalPlaces == dpVal,
                            onClick = { onUpdateSettings(settings.copy(decimalPlaces = dpVal)) },
                            label = { Text("$dpVal") }
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    decimalOptions.drop(5).forEach { dpVal ->
                        FilterChip(
                            selected = settings.decimalPlaces == dpVal,
                            onClick = { onUpdateSettings(settings.copy(decimalPlaces = dpVal)) },
                            label = { Text("$dpVal") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                var roundingMenuExpanded by remember { mutableStateOf(false) }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Rounding Mode",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "For measurement display. Can counts always use ceiling.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box {
                        OutlinedButton(onClick = { roundingMenuExpanded = true }) {
                            Text(settings.roundingMode.displayName)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }

                        DropdownMenu(
                            expanded = roundingMenuExpanded,
                            onDismissRequest = { roundingMenuExpanded = false }
                        ) {
                            DisplayRoundingMode.values().forEach { mode ->
                                DropdownMenuItem(
                                    text = { Text(mode.displayName) },
                                    onClick = {
                                        onUpdateSettings(settings.copy(roundingMode = mode))
                                        roundingMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Display Sections Group
            SettingsGroup(title = "Display Sections", icon = Icons.Default.Visibility) {
                SettingsToggleRow(
                    title = "Show Paint Diagram",
                    subtitle = "Surface illustration and coat visualizer",
                    checked = settings.showPaintDiagram,
                    onCheckedChange = { onUpdateSettings(settings.copy(showPaintDiagram = it)) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                SettingsToggleRow(
                    title = "Show Purchase Details",
                    subtitle = "Can quantity, total volume, and leftover",
                    checked = settings.showPurchaseDetails,
                    onCheckedChange = { onUpdateSettings(settings.copy(showPurchaseDetails = it)) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                SettingsToggleRow(
                    title = "Show Calculation Steps",
                    subtitle = "Exact mathematical step-by-step breakdown",
                    checked = settings.showCalculationSteps,
                    onCheckedChange = { onUpdateSettings(settings.copy(showCalculationSteps = it)) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                SettingsToggleRow(
                    title = "Show Formula Reference",
                    subtitle = "Standard paint calculation formulas card",
                    checked = settings.showFormula,
                    onCheckedChange = { onUpdateSettings(settings.copy(showFormula = it)) }
                )
            }

            // History & Data Group
            SettingsGroup(title = "History & Data", icon = Icons.Default.Storage) {
                SettingsToggleRow(
                    title = "Confirm Delete",
                    subtitle = "Prompt before deleting single history items",
                    checked = settings.confirmDeleteHistory,
                    onCheckedChange = { onUpdateSettings(settings.copy(confirmDeleteHistory = it)) }
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedButton(
                    onClick = { showClearHistoryConfirm = true },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.DeleteSweep, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Clear All Saved Calculations")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { showResetSettingsConfirm = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.RestartAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Reset Settings to Default")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SettingsGroup(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            content()
        }
    }
}

@Composable
fun SettingsToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

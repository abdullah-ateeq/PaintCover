package com.example.paintcover.ui.home.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.Window
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.paintcover.data.model.LengthUnit
import com.example.paintcover.data.model.Opening

@Composable
fun OpeningsDeductionSection(
    enabled: Boolean,
    onEnabledChanged: (Boolean) -> Unit,
    doors: List<Opening>,
    windows: List<Opening>,
    onAddDoor: () -> Unit,
    onUpdateDoor: (String, String, String, LengthUnit, LengthUnit) -> Unit,
    onRemoveDoor: (String) -> Unit,
    onAddWindow: () -> Unit,
    onUpdateWindow: (String, String, String, LengthUnit, LengthUnit) -> Unit,
    onRemoveWindow: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "DEDUCT OPENINGS",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Exclude doors and windows from paint area",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Switch(
                    checked = enabled,
                    onCheckedChange = onEnabledChanged
                )
            }

            AnimatedVisibility(
                visible = enabled,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Doors Section
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.DoorFront,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Doors (${doors.size})",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            FilledTonalButton(
                                onClick = onAddDoor,
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add Door", style = MaterialTheme.typography.labelMedium)
                            }
                        }

                        if (doors.isEmpty()) {
                            Text(
                                text = "No doors added. Tap 'Add Door' if needed.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        } else {
                            doors.forEachIndexed { index, door ->
                                OpeningRow(
                                    title = "Door #${index + 1}",
                                    width = door.width,
                                    height = door.height,
                                    widthUnit = door.widthUnit,
                                    heightUnit = door.heightUnit,
                                    onUpdate = { w, h, wu, hu -> onUpdateDoor(door.id, w, h, wu, hu) },
                                    onDelete = { onRemoveDoor(door.id) }
                                )
                            }
                        }
                    }

                    HorizontalDivider()

                    // Windows Section
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Window,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Windows (${windows.size})",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            FilledTonalButton(
                                onClick = onAddWindow,
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add Window", style = MaterialTheme.typography.labelMedium)
                            }
                        }

                        if (windows.isEmpty()) {
                            Text(
                                text = "No windows added. Tap 'Add Window' if needed.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        } else {
                            windows.forEachIndexed { index, window ->
                                OpeningRow(
                                    title = "Window #${index + 1}",
                                    width = window.width,
                                    height = window.height,
                                    widthUnit = window.widthUnit,
                                    heightUnit = window.heightUnit,
                                    onUpdate = { w, h, wu, hu -> onUpdateWindow(window.id, w, h, wu, hu) },
                                    onDelete = { onRemoveWindow(window.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OpeningRow(
    title: String,
    width: String,
    height: String,
    widthUnit: LengthUnit,
    heightUnit: LengthUnit,
    onUpdate: (String, String, LengthUnit, LengthUnit) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            OutlinedTextField(
                value = width,
                onValueChange = { onUpdate(it, height, widthUnit, heightUnit) },
                label = { Text("Width") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f)
            )

            LengthUnitDropdown(
                selectedUnit = widthUnit,
                onUnitSelected = { onUpdate(width, height, it, heightUnit) }
            )

            OutlinedTextField(
                value = height,
                onValueChange = { onUpdate(width, it, widthUnit, heightUnit) },
                label = { Text("Height") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f)
            )

            LengthUnitDropdown(
                selectedUnit = heightUnit,
                onUnitSelected = { onUpdate(width, height, widthUnit, it) }
            )

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Remove $title",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

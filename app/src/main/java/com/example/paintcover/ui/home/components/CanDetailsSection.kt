package com.example.paintcover.ui.home.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.paintcover.data.model.PaintVolumeUnit

@Composable
fun CanDetailsSection(
    canSizeInput: String,
    onCanSizeChanged: (String) -> Unit,
    canSizeUnit: PaintVolumeUnit,
    onCanSizeUnitChanged: (PaintVolumeUnit) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedSection by remember { mutableStateOf(canSizeInput.isNotEmpty()) }

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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.FormatPaint,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "PAINT CAN DETAILS (OPTIONAL)",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (canSizeInput.isNotEmpty()) "Can size configured" else "Calculate whole cans to buy",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                TextButton(
                    onClick = { expandedSection = !expandedSection }
                ) {
                    Text(if (expandedSection) "Hide" else "Show")
                }
            }

            AnimatedVisibility(
                visible = expandedSection,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = canSizeInput,
                            onValueChange = onCanSizeChanged,
                            label = { Text("Can Size") },
                            placeholder = { Text("e.g. 5") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier
                                .weight(1f)
                                .semantics { contentDescription = "Paint can size." }
                        )

                        PaintVolumeUnitDropdown(
                            selectedUnit = canSizeUnit,
                            onUnitSelected = onCanSizeUnitChanged
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Common can size presets
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("1", "2.5", "5", "10").forEach { preset ->
                            FilterChip(
                                selected = canSizeInput == preset,
                                onClick = { onCanSizeChanged(preset) },
                                label = { Text("$preset ${canSizeUnit.symbol}") }
                            )
                        }
                        if (canSizeInput.isNotEmpty()) {
                            TextButton(
                                onClick = { onCanSizeChanged("") }
                            ) {
                                Text("Clear")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Leave empty to calculate paint volume only without can breakdown.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun PaintVolumeUnitDropdown(
    selectedUnit: PaintVolumeUnit,
    onUnitSelected: (PaintVolumeUnit) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { expanded = true },
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 14.dp)
        ) {
            Text(text = selectedUnit.symbol, fontWeight = FontWeight.Bold)
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Volume Unit")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            PaintVolumeUnit.values().forEach { unit ->
                DropdownMenuItem(
                    text = { Text("${unit.symbol} (${unit.displayName})") },
                    onClick = {
                        onUnitSelected(unit)
                        expanded = false
                    }
                )
            }
        }
    }
}

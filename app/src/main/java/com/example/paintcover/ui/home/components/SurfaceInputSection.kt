package com.example.paintcover.ui.home.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.paintcover.data.model.AreaUnit
import com.example.paintcover.data.model.InputMode
import com.example.paintcover.data.model.LengthUnit

@Composable
fun SurfaceInputSection(
    inputMode: InputMode,
    onInputModeChanged: (InputMode) -> Unit,
    surfaceAreaInput: String,
    onSurfaceAreaChanged: (String) -> Unit,
    surfaceAreaUnit: AreaUnit,
    onSurfaceAreaUnitChanged: (AreaUnit) -> Unit,
    surfaceLengthInput: String,
    onSurfaceLengthChanged: (String) -> Unit,
    surfaceLengthUnit: LengthUnit,
    onSurfaceLengthUnitChanged: (LengthUnit) -> Unit,
    surfaceWidthInput: String,
    onSurfaceWidthChanged: (String) -> Unit,
    surfaceWidthUnit: LengthUnit,
    onSurfaceWidthUnitChanged: (LengthUnit) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Mode toggle header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (inputMode == InputMode.AREA) "SURFACE AREA" else "SURFACE DIMENSIONS",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Mode toggle button
                TextButton(
                    onClick = {
                        val newMode = if (inputMode == InputMode.AREA) InputMode.DIMENSIONS else InputMode.AREA
                        onInputModeChanged(newMode)
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (inputMode == InputMode.AREA) "CALCULATE FROM DIMENSIONS" else "ENTER AREA DIRECTLY",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Animated content between Area and Dimensions
            AnimatedContent(
                targetState = inputMode,
                transitionSpec = {
                    fadeIn(animationSpec = tween(250)) togetherWith fadeOut(animationSpec = tween(250))
                },
                label = "inputModeTransition"
            ) { mode ->
                if (mode == InputMode.AREA) {
                    // Direct Area Input
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = surfaceAreaInput,
                            onValueChange = onSurfaceAreaChanged,
                            label = { Text("Surface Area") },
                            placeholder = { Text("e.g. 50") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier
                                .weight(1f)
                                .semantics { contentDescription = "Paintable surface area." }
                        )

                        AreaUnitDropdown(
                            selectedUnit = surfaceAreaUnit,
                            onUnitSelected = onSurfaceAreaUnitChanged
                        )
                    }
                } else {
                    // Dimensions Inputs (Length x Width)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = surfaceLengthInput,
                                onValueChange = onSurfaceLengthChanged,
                                label = { Text("Length") },
                                placeholder = { Text("e.g. 5") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier
                                    .weight(1f)
                                    .semantics { contentDescription = "Surface length." }
                            )

                            LengthUnitDropdown(
                                selectedUnit = surfaceLengthUnit,
                                onUnitSelected = onSurfaceLengthUnitChanged
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = surfaceWidthInput,
                                onValueChange = onSurfaceWidthChanged,
                                label = { Text("Width / Height") },
                                placeholder = { Text("e.g. 10") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier
                                    .weight(1f)
                                    .semantics { contentDescription = "Surface width or height." }
                            )

                            LengthUnitDropdown(
                                selectedUnit = surfaceWidthUnit,
                                onUnitSelected = onSurfaceWidthUnitChanged
                            )
                        }

                        Text(
                            text = "Surface Area = Length × Width",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AreaUnitDropdown(
    selectedUnit: AreaUnit,
    onUnitSelected: (AreaUnit) -> Unit,
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
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Area Unit")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            AreaUnit.values().forEach { unit ->
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

@Composable
fun LengthUnitDropdown(
    selectedUnit: LengthUnit,
    onUnitSelected: (LengthUnit) -> Unit,
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
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Length Unit")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            LengthUnit.values().forEach { unit ->
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

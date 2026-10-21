package com.example.paintcover.ui.home.components

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
import com.example.paintcover.data.model.CoverageUnit

@Composable
fun PaintCoverageSection(
    coverageInput: String,
    onCoverageChanged: (String) -> Unit,
    coverageUnit: CoverageUnit,
    onCoverageUnitChanged: (CoverageUnit) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "PAINT COVERAGE",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = coverageInput,
                    onValueChange = onCoverageChanged,
                    label = { Text("Coverage Rate") },
                    placeholder = { Text("e.g. 10") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .weight(1f)
                        .semantics { contentDescription = "Paint coverage per liter." }
                )

                CoverageUnitDropdown(
                    selectedUnit = coverageUnit,
                    onUnitSelected = onCoverageUnitChanged
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            val helperQuestion = when (coverageUnit) {
                CoverageUnit.SQUARE_METER_PER_LITER -> "How many square meters does 1 liter cover?"
                CoverageUnit.SQUARE_FOOT_PER_LITER -> "How many square feet does 1 liter cover?"
                CoverageUnit.SQUARE_METER_PER_GALLON -> "How many square meters does 1 gallon cover?"
                CoverageUnit.SQUARE_FOOT_PER_GALLON -> "How many square feet does 1 gallon cover?"
            }

            Text(
                text = helperQuestion,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Higher coverage means less paint is required.",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
            )
        }
    }
}

@Composable
fun CoverageUnitDropdown(
    selectedUnit: CoverageUnit,
    onUnitSelected: (CoverageUnit) -> Unit,
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
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Coverage Unit")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            CoverageUnit.values().forEach { unit ->
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

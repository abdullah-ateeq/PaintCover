package com.example.paintcover.ui.home

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.paintcover.data.model.PaintCoverUiState
import com.example.paintcover.domain.math.PaintCoverFormatter
import com.example.paintcover.ui.home.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: PaintCoverUiState,
    onSurfaceTypeSelected: (com.example.paintcover.data.model.SurfaceType) -> Unit,
    onInputModeChanged: (com.example.paintcover.data.model.InputMode) -> Unit,
    onSurfaceAreaChanged: (String) -> Unit,
    onSurfaceAreaUnitChanged: (com.example.paintcover.data.model.AreaUnit) -> Unit,
    onSurfaceLengthChanged: (String) -> Unit,
    onSurfaceLengthUnitChanged: (com.example.paintcover.data.model.LengthUnit) -> Unit,
    onSurfaceWidthChanged: (String) -> Unit,
    onSurfaceWidthUnitChanged: (com.example.paintcover.data.model.LengthUnit) -> Unit,
    onCoverageChanged: (String) -> Unit,
    onCoverageUnitChanged: (com.example.paintcover.data.model.CoverageUnit) -> Unit,
    onCoatsChanged: (String) -> Unit,
    onWastePercentChanged: (String) -> Unit,
    onCanSizeChanged: (String) -> Unit,
    onCanSizeUnitChanged: (com.example.paintcover.data.model.PaintVolumeUnit) -> Unit,
    onOpeningsEnabledChanged: (Boolean) -> Unit,
    onAddDoor: () -> Unit,
    onUpdateDoor: (String, String, String, com.example.paintcover.data.model.LengthUnit, com.example.paintcover.data.model.LengthUnit) -> Unit,
    onRemoveDoor: (String) -> Unit,
    onAddWindow: () -> Unit,
    onUpdateWindow: (String, String, String, com.example.paintcover.data.model.LengthUnit, com.example.paintcover.data.model.LengthUnit) -> Unit,
    onRemoveWindow: (String) -> Unit,
    onSaveToHistory: () -> Unit,
    onReset: () -> Unit,
    onValueCopied: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val shareCalculation: () -> Unit = {
        uiState.result?.let { result ->
            val summary = PaintCoverFormatter.formatSummaryForShare(
                result = result,
                decimalPlaces = uiState.settings.decimalPlaces,
                roundingMode = uiState.settings.roundingMode
            )
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, summary)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, "Share Paint Calculation")
            context.startActivity(shareIntent)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "PaintCover",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Estimate paint quantity from surface area and coverage.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onReset) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset calculation to defaults")
                    }
                    IconButton(
                        onClick = onSaveToHistory,
                        enabled = uiState.result != null
                    ) {
                        Icon(Icons.Default.BookmarkBorder, contentDescription = "Save to history")
                    }
                    IconButton(
                        onClick = shareCalculation,
                        enabled = uiState.result != null
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share calculation")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
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
            // Surface Type Selector
            SurfaceTypeSelector(
                selectedType = uiState.surfaceType,
                onTypeSelected = onSurfaceTypeSelected
            )

            // Surface Input Section (Area or Dimensions)
            SurfaceInputSection(
                inputMode = uiState.inputMode,
                onInputModeChanged = onInputModeChanged,
                surfaceAreaInput = uiState.surfaceAreaInput,
                onSurfaceAreaChanged = onSurfaceAreaChanged,
                surfaceAreaUnit = uiState.surfaceAreaUnit,
                onSurfaceAreaUnitChanged = onSurfaceAreaUnitChanged,
                surfaceLengthInput = uiState.surfaceLengthInput,
                onSurfaceLengthChanged = onSurfaceLengthChanged,
                surfaceLengthUnit = uiState.surfaceLengthUnit,
                onSurfaceLengthUnitChanged = onSurfaceLengthUnitChanged,
                surfaceWidthInput = uiState.surfaceWidthInput,
                onSurfaceWidthChanged = onSurfaceWidthChanged,
                surfaceWidthUnit = uiState.surfaceWidthUnit,
                onSurfaceWidthUnitChanged = onSurfaceWidthUnitChanged
            )

            // Openings Deduction (Doors & Windows)
            OpeningsDeductionSection(
                enabled = uiState.openingsEnabled,
                onEnabledChanged = onOpeningsEnabledChanged,
                doors = uiState.doors,
                windows = uiState.windows,
                onAddDoor = onAddDoor,
                onUpdateDoor = onUpdateDoor,
                onRemoveDoor = onRemoveDoor,
                onAddWindow = onAddWindow,
                onUpdateWindow = onUpdateWindow,
                onRemoveWindow = onRemoveWindow
            )

            // Paint Coverage
            PaintCoverageSection(
                coverageInput = uiState.coverageInput,
                onCoverageChanged = onCoverageChanged,
                coverageUnit = uiState.coverageUnit,
                onCoverageUnitChanged = onCoverageUnitChanged
            )

            // Number of Coats
            CoatsSection(
                coatsInput = uiState.coatsInput,
                onCoatsChanged = onCoatsChanged
            )

            // Waste Allowance
            WasteAllowanceSection(
                wastePercentInput = uiState.wastePercentInput,
                onWastePercentChanged = onWastePercentChanged
            )

            // Can Details (Optional)
            CanDetailsSection(
                canSizeInput = uiState.canSizeInput,
                onCanSizeChanged = onCanSizeChanged,
                canSizeUnit = uiState.canSizeUnit,
                onCanSizeUnitChanged = onCanSizeUnitChanged
            )

            // Error Message (if any)
            AnimatedVisibility(
                visible = uiState.errorMessage != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                uiState.errorMessage?.let { errorText ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = errorText,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }
            }

            // Results Section
            uiState.result?.let { result ->
                // Primary Result Card (Most prominent)
                PrimaryResultCard(
                    result = result,
                    settings = uiState.settings,
                    onShare = shareCalculation,
                    onValueCopied = onValueCopied
                )

                // Surface Preview Diagram
                if (uiState.settings.showPaintDiagram) {
                    PaintDiagramCard(result = result)
                }

                // Purchase Card (when enabled or can size provided)
                if (uiState.settings.showPurchaseDetails) {
                    PurchaseCard(
                        result = result,
                        settings = uiState.settings,
                        onValueCopied = onValueCopied
                    )
                }

                // Calculation Steps
                if (uiState.settings.showCalculationSteps) {
                    CalculationStepsCard(steps = result.calculationSteps)
                }

                // Formula Reference
                if (uiState.settings.showFormula) {
                    FormulaCard(hasOpenings = result.openingArea > java.math.BigDecimal.ZERO)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

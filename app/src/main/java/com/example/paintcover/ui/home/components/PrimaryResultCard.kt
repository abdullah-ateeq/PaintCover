package com.example.paintcover.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.paintcover.data.model.AppSettings
import com.example.paintcover.data.model.PaintCoverResult
import com.example.paintcover.domain.math.PaintCoverFormatter
import java.math.BigDecimal

@Composable
fun PrimaryResultCard(
    result: PaintCoverResult,
    settings: AppSettings,
    onShare: () -> Unit,
    onValueCopied: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    val dec = settings.decimalPlaces
    val rm = settings.roundingMode

    val formattedPaintRequired = PaintCoverFormatter.formatBigDecimal(result.paintRequired, dec, rm)
    val formattedBasePaint = PaintCoverFormatter.formatBigDecimal(result.basePaintRequired, dec, rm)
    val formattedPaintableArea = PaintCoverFormatter.formatBigDecimal(result.paintableArea, dec, rm)
    val formattedCoverage = PaintCoverFormatter.formatBigDecimal(result.coveragePerLiter, dec, rm)
    val formattedWaste = PaintCoverFormatter.formatBigDecimal(result.wastePercent, dec, rm)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = "Estimated paint required: $formattedPaintRequired liters." },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Header Row: Label + Share & Copy buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PAINT REQUIRED",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                    letterSpacing = 1.5.sp
                )

                Row {
                    IconButton(
                        onClick = {
                            val summary = PaintCoverFormatter.formatSummaryForShare(result, dec, rm)
                            clipboardManager.setText(AnnotatedString(summary))
                            onValueCopied("Summary copied to clipboard")
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "Copy paint calculation.",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onShare,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Share calculation",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Big Number: e.g. 11 L or 10.1156 L
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = formattedPaintRequired,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "L",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString("$formattedPaintRequired L"))
                        onValueCopied("Paint Required ($formattedPaintRequired L) copied")
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.ContentCopy,
                        contentDescription = "Copy paint required value",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Text(
                text = "Includes ${formattedWaste}% waste • ${result.coats} ${if (result.coats == 1) "coat" else "coats"}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Breakdown box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.75f))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (result.openingArea > BigDecimal.ZERO) {
                        BreakdownItem(
                            label = "GROSS SURFACE",
                            value = "${PaintCoverFormatter.formatBigDecimal(result.grossSurfaceArea, dec, rm)} m²"
                        )
                        BreakdownItem(
                            label = "OPENINGS",
                            value = "- ${PaintCoverFormatter.formatBigDecimal(result.openingArea, dec, rm)} m²"
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PAINTABLE AREA",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "$formattedPaintableArea m²",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString("$formattedPaintableArea m²"))
                                    onValueCopied("Paintable Area ($formattedPaintableArea m²) copied")
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    Icons.Default.ContentCopy,
                                    contentDescription = "Copy paintable area",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }

                    BreakdownItem(
                        label = "COATS",
                        value = "${result.coats}"
                    )
                    BreakdownItem(
                        label = "COVERAGE",
                        value = "$formattedCoverage m²/L"
                    )
                    BreakdownItem(
                        label = "BASE PAINT",
                        value = "$formattedBasePaint L"
                    )
                    BreakdownItem(
                        label = "WASTE",
                        value = "$formattedWaste%"
                    )
                }
            }
        }
    }
}

@Composable
fun BreakdownItem(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

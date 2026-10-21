package com.example.paintcover.ui.home.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.paintcover.data.model.PaintCoverResult
import com.example.paintcover.data.model.SurfaceType
import com.example.paintcover.domain.math.PaintCoverFormatter

@Composable
fun PaintDiagramCard(
    result: PaintCoverResult,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val outlineColor = MaterialTheme.colorScheme.outline
    val coatsAnimated by animateFloatAsState(
        targetValue = result.coats.coerceIn(1, 5).toFloat(),
        animationSpec = tween(400),
        label = "coatsProgress"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = "Paintable rectangular surface." },
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
                Text(
                    text = "SURFACE PREVIEW",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "${result.surfaceType.displayName} • ${result.coats} ${if (result.coats == 1) "coat" else "coats"}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Architectural Surface Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val padX = 24.dp.toPx()
                    val padY = 16.dp.toPx()
                    val rectW = w - (padX * 2)
                    val rectH = h - (padY * 2)

                    // Outer rectangular surface outline
                    drawRoundRect(
                        color = outlineColor.copy(alpha = 0.4f),
                        topLeft = Offset(padX, padY),
                        size = Size(rectW, rectH),
                        cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                        style = Stroke(
                            width = 2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
                        )
                    )

                    // Painted interior stripes representing coat layers
                    val layerFraction = (coatsAnimated / 5f).coerceIn(0.2f, 1f)
                    val paintedWidth = rectW * layerFraction

                    drawRoundRect(
                        color = primaryContainer.copy(alpha = 0.65f),
                        topLeft = Offset(padX + 2f, padY + 2f),
                        size = Size(paintedWidth - 4f, rectH - 4f),
                        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                    )

                    // Architectural texture lines / paint stroke bands
                    val stripeCount = (result.coats * 2).coerceIn(3, 10)
                    val stripeSpacing = (paintedWidth - 8f) / stripeCount
                    for (i in 0 until stripeCount) {
                        val startX = padX + 6f + (i * stripeSpacing)
                        drawLine(
                            color = primaryColor.copy(alpha = 0.25f),
                            start = Offset(startX, padY + 6f),
                            end = Offset(startX, padY + rectH - 6f),
                            strokeWidth = 3.dp.toPx()
                        )
                    }

                    // Roller edge marker
                    val rollerX = padX + paintedWidth - 4f
                    drawLine(
                        color = primaryColor,
                        start = Offset(rollerX, padY + 4f),
                        end = Offset(rollerX, padY + rectH - 4f),
                        strokeWidth = 4.dp.toPx()
                    )
                }

                // Center badge overlay
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.88f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "${PaintCoverFormatter.formatBigDecimal(result.paintableArea)} m²",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${PaintCoverFormatter.formatBigDecimal(result.coveragePerLiter)} m²/L coverage",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

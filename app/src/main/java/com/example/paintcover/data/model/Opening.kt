package com.example.paintcover.data.model

data class Opening(
    val id: String,
    val width: String,
    val height: String,
    val widthUnit: LengthUnit = LengthUnit.METER,
    val heightUnit: LengthUnit = LengthUnit.METER,
    val label: String = "Opening"
)

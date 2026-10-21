package com.example.paintcover.data.model

enum class CoverageUnit(val symbol: String, val displayName: String) {
    SQUARE_METER_PER_LITER("m²/L", "m² per Liter"),
    SQUARE_FOOT_PER_LITER("ft²/L", "ft² per Liter"),
    SQUARE_METER_PER_GALLON("m²/gal", "m² per US Gallon"),
    SQUARE_FOOT_PER_GALLON("ft²/gal", "ft² per US Gallon")
}

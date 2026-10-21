package com.example.paintcover.data.model

enum class SurfaceType(val displayName: String, val description: String) {
    WALL("WALL", "Estimate paint for a wall"),
    CEILING("CEILING", "Estimate paint for a ceiling"),
    FLOOR("FLOOR", "Estimate paint for a floor"),
    OTHER("OTHER", "Any paintable surface")
}

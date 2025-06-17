package com.firman.capstone.urvoice.ui.components

import androidx.compose.ui.graphics.painter.Painter

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: Painter? = null,
    val isMainFeature: Boolean = false
)
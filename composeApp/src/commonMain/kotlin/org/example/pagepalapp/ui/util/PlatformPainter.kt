package org.example.pagepalapp.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

@Composable
expect fun platformPainter(name: String): Painter


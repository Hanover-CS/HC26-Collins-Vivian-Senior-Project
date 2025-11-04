package org.example.pagepalapp.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable


@Composable
fun PagePalTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(),
        typography = AppTypography,
        content = content
    )
}

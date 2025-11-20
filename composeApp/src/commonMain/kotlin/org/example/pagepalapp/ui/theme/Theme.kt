/**

 * theme wrapper for the app. This file defines:
 *   - color scheme (currently Material3 light theme)
 *   - typography (imported from AppTypography)
 *   - shape styles (if added later)
 */

package org.example.pagepalapp.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun PagePalTheme(content: @Composable () -> Unit) {

    // apply Material3 light colors, typography, and default shapes.
    MaterialTheme(
        colorScheme = lightColorScheme(),
        typography = AppTypography,
        content = content
    )
}

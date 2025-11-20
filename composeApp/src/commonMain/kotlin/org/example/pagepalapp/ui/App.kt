/**
 * every screen in the app runs inside this
 */

package org.example.pagepalapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color

import org.example.pagepalapp.ui.navigation.AppNavigation
import org.example.pagepalapp.data.HomeViewModel

@Composable
fun App() {

    // light theme colors for PagePal
    val lightColors = lightColorScheme(
        primary = Color(0xFF6C63FF),
        onPrimary = Color.White,
        background = Color(0xFFF8F7FF),
        surface = Color.White,
        onSurface = Color(0xFF333333)
    )

    // apply Material theme & route all UI into AppNavigation()
    MaterialTheme(colorScheme = lightColors) {
        Surface {
            AppNavigation(viewModel = HomeViewModel())
        }
    }
}

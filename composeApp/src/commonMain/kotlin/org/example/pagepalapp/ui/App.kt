/**
 * every screen in the app runs inside this
 */

package org.example.pagepalapp.ui

import android.annotation.SuppressLint
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.example.pagepalapp.ui.navigation.AppNavigation
import org.example.pagepalapp.data.HomeViewModel

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun App() {
    Surface(color = MaterialTheme.colorScheme.background) {
        AppNavigation(viewModel = HomeViewModel())
    }
}

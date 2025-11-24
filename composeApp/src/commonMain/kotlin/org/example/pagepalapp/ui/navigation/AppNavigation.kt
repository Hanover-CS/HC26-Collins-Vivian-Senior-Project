/**
 * defines the entire navigation graph using Jetpack Navigation.
 *
 * routes:
 *   - "home"             → HomeScreen
 *   - "library"          → LibraryScreen
 *   - "addBook"          → AddBookScreen
 *   - "bookDetail/{id}"  → BookDetailScreen
 *   - "calendar"         → CalendarScreen (⭐ NEW)
 */

package org.example.pagepalapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable

import org.example.pagepalapp.data.HomeViewModel
import org.example.pagepalapp.ui.screens.HomeScreen
import org.example.pagepalapp.ui.screens.BookDetailScreen
import org.example.pagepalapp.ui.screens.LibraryScreen
import org.example.pagepalapp.ui.screens.AddBookScreen
import org.example.pagepalapp.ui.screens.CalendarScreen    // ⭐ ADD THIS IMPORT

@Composable
fun AppNavigation(viewModel: HomeViewModel) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {
            HomeScreen(navController = navController, viewModel = viewModel)
        }

        composable("library") {
            LibraryScreen(navController = navController, viewModel = viewModel)
        }

        composable("addBook") {
            AddBookScreen(navController = navController, viewModel = viewModel)
        }

        composable("bookDetail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")

            BookDetailScreen(
                bookId = id,
                navController = navController,
                viewModel = viewModel
            )
        }

        // ⭐ NEW CALENDAR ROUTE
        composable("calendar") {
            CalendarScreen(viewModel = viewModel)
        }
    }
}

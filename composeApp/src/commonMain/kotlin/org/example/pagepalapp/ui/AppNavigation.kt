/**
 * defines the entire navigation graph using Jetpack Navigation.
 *
 * routes:
 *   - "home"        → HomeScreen (search + results)
 *   - "library"     → LibraryScreen (saved personal books)
 *   - "addBook"     → AddBookScreen (manual personal books)
 *   - "bookDetail/{id}" → Detail page for selected book
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

@Composable
fun AppNavigation(viewModel: HomeViewModel) {

    // navigation controller that keeps track of back stack & current screen
    val navController = rememberNavController()

    // main navigation graph
    NavHost(
        navController = navController,
        startDestination = "home"     // first screen shown
    ) {

        // home (search screen)
        composable("home") {
            HomeScreen(navController = navController, viewModel = viewModel)
        }

        // library (saved books)
        composable("library") {
            LibraryScreen(navController = navController, viewModel = viewModel)
        }

        // add a personal book
        composable("addBook") {
            AddBookScreen(navController = navController, viewModel = viewModel)
        }

        // book details page
        composable("bookDetail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")

            BookDetailScreen(
                bookId = id,
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

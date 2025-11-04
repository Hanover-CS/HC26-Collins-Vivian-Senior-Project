package org.example.pagepalapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.pagepalapp.data.HomeViewModel
import org.example.pagepalapp.ui.components.BottomNavigationBar
import org.example.pagepalapp.ui.screens.*
import org.example.pagepalapp.ui.theme.PagePalTheme

class MainActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PagePalTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(navController)
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable("home") {
                            HomeScreen(navController, homeViewModel)
                        }
                        composable("library") {
                            LibraryScreen(navController, homeViewModel)
                        }
                        composable("addBook") {
                            AddBookScreen(navController)
                        }
                        composable("bookDetail/{bookId}") { backStackEntry ->
                            val bookId = backStackEntry.arguments?.getString("bookId")
                            BookDetailScreen(bookId, navController, homeViewModel)
                        }
                    }
                }
            }
        }
    }
}

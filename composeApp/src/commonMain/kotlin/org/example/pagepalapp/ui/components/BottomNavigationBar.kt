/**
 * defines the bottom navigation bar used throughout the app.
 *
 * allows users to quickly switch between:
 *  - Home screen (search books)
 *  - Library screen (saved books)
 *  - Add Book screen (manual entry)
 */

@file:OptIn(ExperimentalMaterial3Api::class)
package org.example.pagepalapp.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.graphics.Color

@Composable
fun BottomNavigationBar(navController: NavController) {

    // list of bottom bar destinations
    val items = listOf(
        BottomNavItem("Home", "home", "🏠"),
        BottomNavItem("Library", "library", "📚"),
        BottomNavItem("Add", "addBook", "➕")
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        // tracks which screen is currently active
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->

            NavigationBarItem(
                selected = currentRoute == item.route,     // highlight active item
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo("home")                // simplifies backstack
                            launchSingleTop = true         // prevents duplicates
                        }
                    }
                },
                icon = { Text(item.icon) },                // emoji icon
                label = { Text(item.title) },              // text label below icon
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}

// defines a single navigation item
data class BottomNavItem(
    val title: String,       // label under icon
    val route: String,       // navigation route
    val icon: String         // emoji icon
)

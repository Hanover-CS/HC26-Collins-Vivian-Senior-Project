/**
 * defines the bottom navigation bar used throughout the app.
 *
 * allows users to quickly switch between:
 *  - Home screen (search books)
 *  - Library screen (saved books)
 *  - Add Book screen (manual entry)
 *  - Calendar screen (reading log)
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

    val items = listOf(
        BottomNavItem("Home", "home", "🏠"),
        BottomNavItem("Library", "library", "📚"),
        BottomNavItem("Add", "addBook", "➕"),
        BottomNavItem("Calendar", "calendar", "📅")
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->

            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo("home")
                            launchSingleTop = true
                        }
                    }
                },
                icon = { Text(item.icon) },
                label = { Text(item.title) },
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
    val title: String,
    val route: String,
    val icon: String
)

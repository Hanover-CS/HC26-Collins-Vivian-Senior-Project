/**
 * LibraryScreen.kt
 *
 * displays all books the user has added to their personal library:
 *  - books added manually through AddBookScreen
 *  - books added from the BookDetailScreen using "Add to Library"

 */

package org.example.pagepalapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.example.pagepalapp.data.HomeViewModel
import org.example.pagepalapp.ui.components.BookCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {

    val libraryBooks by viewModel.libraryBooks.collectAsState()   // saved books list

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Library") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("←")     // simple back button
                    }
                }
            )
        }
    ) { padding ->

        if (libraryBooks.isEmpty()) {

            // empty-state message
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Your library is empty. Add books to get started! :)",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                items(libraryBooks) { book ->

                    BookCard(book) {
                        navController.navigate("bookDetail/${book.id}")
                    }

                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

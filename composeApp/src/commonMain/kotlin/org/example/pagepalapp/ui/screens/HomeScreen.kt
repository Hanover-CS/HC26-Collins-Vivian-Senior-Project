/**
 * this is the main screen where users:
 *  - search for books using Google Books API
 *  - browse search results
 *  - tap a book to view details
 */

@file:OptIn(ExperimentalMaterial3Api::class)
package org.example.pagepalapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import org.example.pagepalapp.ui.components.BookCard
import org.example.pagepalapp.data.HomeViewModel
import org.example.pagepalapp.ui.components.BottomNavigationBar
import org.example.pagepalapp.ui.components.QuoteCard

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {

    val query by viewModel.query.collectAsState()
    val books by viewModel.results.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("PagePal") }) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            // cute motivational card ! :)))
            QuoteCard()

            // search input
            OutlinedTextField(
                value = query,
                onValueChange = viewModel::onQueryChange,
                label = { Text("Search for books") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // search button
            Button(
                onClick = { viewModel.searchBooks() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Search")
            }

            Spacer(Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn {
                    items(books) { volume ->
                        BookCard(volume) { selectedBook ->
                            navController.navigate("bookDetail/${selectedBook.id}")
                        }
                    }
                }
            }
        }
    }
}

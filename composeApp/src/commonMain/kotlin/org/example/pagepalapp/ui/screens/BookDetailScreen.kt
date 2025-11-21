/**
 * displays the full details about a selected book, including:
 *  - cover image
 *  - title and author(s)
 *  - description text
 *  - add/remove from library button
 *  - reading progress fields (auto pageCount support!)
 */

@file:OptIn(ExperimentalMaterial3Api::class)

package org.example.pagepalapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.example.pagepalapp.data.HomeViewModel
import org.example.pagepalapp.ui.util.platformPainter

@Composable
fun BookDetailScreen(
    bookId: String?,
    navController: NavController,
    viewModel: HomeViewModel
) {
    val book = viewModel.getBookById(bookId)

    if (book == null) {
        Text("Book not found", modifier = Modifier.padding(32.dp))
        return
    }

    val info = book.volumeInfo
    val isInLibrary = viewModel.isBookInLibrary(book.id)

    // initial state uses the auto-populated values from Google Books
    var currentPage by remember { mutableStateOf(book.currentPage) }
    var totalPages by remember { mutableStateOf(book.totalPages) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(info?.title ?: "Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // big cover image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(info?.imageLinks?.thumbnail)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = platformPainter("placeholder"),
                    error = platformPainter("error")
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.55f))
                            )
                        )
                )
            }

            // text content
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {

                Text(
                    info?.title.orEmpty(),
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 24.sp)
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    info?.authors?.joinToString(", ") ?: "",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF444444))
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    info?.description ?: "No description available.",
                    style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 22.sp)
                )

                Spacer(Modifier.height(24.dp))

                // add/remove from library button
                Button(
                    onClick = {
                        if (isInLibrary) {
                            viewModel.removeFromLibrary(book.id)
                        } else {
                            viewModel.addToLibrary(book)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        if (isInLibrary) Color.Red else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(if (isInLibrary) "Remove from Library" else "Add to Library")
                }

                Spacer(Modifier.height(24.dp))

                // reading progress!
                Text("Reading Progress", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(12.dp))

                // current page
                OutlinedTextField(
                    value = currentPage.toString(),
                    onValueChange = {
                        currentPage = it.toIntOrNull() ?: 0
                        viewModel.updateBookProgress(book.id, currentPage, totalPages)
                    },
                    label = { Text("Current Page") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                // total pages (auto-filled)
                OutlinedTextField(
                    value = totalPages.toString(),
                    onValueChange = {
                        totalPages = it.toIntOrNull() ?: 0
                        viewModel.updateBookProgress(book.id, currentPage, totalPages)
                    },
                    label = { Text("Total Pages (autofilled)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                if (totalPages > 0) {
                    LinearProgressIndicator(
                        progress = (currentPage.toFloat() / totalPages.toFloat()).coerceIn(0f, 1f),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "${((currentPage.toFloat() / totalPages.toFloat()) * 100).toInt()}% read"
                    )
                }
            }
        }
    }
}

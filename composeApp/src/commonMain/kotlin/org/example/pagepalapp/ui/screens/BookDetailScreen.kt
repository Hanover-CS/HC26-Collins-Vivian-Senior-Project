/**
 * Displays the full details about a selected book, including:
 * - cover image
 * - title and author(s)
 * - description
 * - add/remove from library
 * - reading progress
 * - rating bar with full + half stars
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
import kotlinx.coroutines.launch
import org.example.pagepalapp.data.HomeViewModel
import org.example.pagepalapp.ui.components.StarRatingBar
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

    var currentPage by remember { mutableStateOf(book.currentPage) }
    var totalPages by remember { mutableStateOf(book.totalPages) }
    var rating by remember { mutableStateOf(book.rating) }

    val snackbarHostState = remember { SnackbarHostState() }
    val libraryEvent by viewModel.libraryEvent.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(libraryEvent) {
        libraryEvent?.let { msg ->
            scope.launch {
                snackbarHostState.showSnackbar(msg)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(info?.title ?: "Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("←", fontSize = 32.sp)
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

            /* --------------------
               Cover Image Section
            ---------------------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                val thumbnailUrl = info?.imageLinks?.thumbnail
                    ?.replace("http://", "https://")

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(thumbnailUrl)
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
                                listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.55f)
                                )
                            )
                        )
                )
            }

            /* --------------------
               Main Text Content
            ---------------------- */
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {

                // Title
                Text(
                    info?.title.orEmpty(),
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 24.sp)
                )

                Spacer(Modifier.height(6.dp))

                // Author(s)
                Text(
                    info?.authors?.joinToString(", ") ?: "",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF444444))
                )

                Spacer(Modifier.height(16.dp))

                // Description
                Text(
                    info?.description ?: "No description available.",
                    style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 22.sp)
                )

                Spacer(Modifier.height(24.dp))

                /* --------------------
                   Rating Bar
                ---------------------- */
                Text("Your Rating", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                StarRatingBar(
                    rating = rating,
                    onRatingChange = { new ->
                        rating = new
                        viewModel.updateBookRating(book.id, new)
                    }
                )

                Spacer(Modifier.height(24.dp))

                /* --------------------
                   Add / Remove Button
                ---------------------- */
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

                /* --------------------
                   Reading Progress
                ---------------------- */
                Text("Reading Progress", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(12.dp))

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

                OutlinedTextField(
                    value = totalPages.toString(),
                    onValueChange = {
                        totalPages = it.toIntOrNull() ?: 0
                        viewModel.updateBookProgress(book.id, currentPage, totalPages)
                    },
                    label = { Text("Total Pages") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                if (totalPages > 0) {
                    val progress = (currentPage.toFloat() / totalPages.toFloat()).coerceIn(0f, 1f)

                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    Text("${(progress * 100).toInt()}% read")
                }
            }
        }
    }
}

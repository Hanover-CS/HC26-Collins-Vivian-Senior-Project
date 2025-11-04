@file:OptIn(ExperimentalMaterial3Api::class)

package org.example.pagepalapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import org.example.pagepalapp.R
import org.example.pagepalapp.data.HomeViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@Composable
fun BookDetailScreen(
    bookId: String?,
    navController: NavController,
    viewModel: HomeViewModel
) {
    val book = viewModel.getBookById(bookId)
    val libraryBooks by viewModel.libraryBooks.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (book != null) {
        val info = book.volumeInfo
        val title = info?.title ?: "Unknown Title"
        val author = info?.authors?.joinToString(", ") ?: "Unknown Author"
        val description = info?.description ?: "No description available"
        val thumbnail = info?.imageLinks?.thumbnail?.replace("http://", "https://")
        val isInLibrary = remember(libraryBooks) { viewModel.isBookInLibrary(book.id) }

        // Track reading progress locally (per session)
        var currentPage by remember { mutableStateOf(book.currentPage) }
        var totalPages by remember { mutableStateOf(if (book.totalPages > 0) book.totalPages else 300) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(title) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- Cover Image ---
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(thumbnail)
                        .crossfade(true)
                        .build(),
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(220.dp)
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    placeholder = painterResource(R.drawable.ic_book_placeholder),
                    error = painterResource(R.drawable.ic_broken_image)
                )

                // --- Title + Author ---
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "by $author",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // --- Progress Tracker ---
                val progress = if (totalPages > 0) currentPage.toFloat() / totalPages else 0f
                Text(
                    text = "📖 Reading Progress: $currentPage / $totalPages pages",
                    style = MaterialTheme.typography.bodyMedium
                )
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = currentPage.toString(),
                        onValueChange = { new ->
                            currentPage = new.toIntOrNull() ?: 0
                        },
                        label = { Text("Current Page") },
                        modifier = Modifier.weight(1f).padding(end = 4.dp)
                    )
                    OutlinedTextField(
                        value = totalPages.toString(),
                        onValueChange = { new ->
                            totalPages = new.toIntOrNull() ?: totalPages
                        },
                        label = { Text("Total Pages") },
                        modifier = Modifier.weight(1f).padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        viewModel.updateBookProgress(book.id, currentPage, totalPages)
                        scope.launch {
                            snackbarHostState.showSnackbar("📚 Progress updated: $currentPage / $totalPages pages")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("💾 Save Progress")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- Add/Remove toggle ---
                Button(
                    onClick = {
                        if (isInLibrary) {
                            viewModel.removeFromLibrary(book.id)
                            scope.launch {
                                snackbarHostState.showSnackbar("❌ Removed from My Library")
                            }
                        } else {
                            viewModel.addToLibrary(book)
                            scope.launch {
                                snackbarHostState.showSnackbar("✅ Added to My Library")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isInLibrary)
                            MaterialTheme.colorScheme.errorContainer
                        else
                            MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isInLibrary) "🗑 Remove from My Library"
                        else "➕ Add to My Library"
                    )
                }
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Book not found", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

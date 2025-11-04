package org.example.pagepalapp.ui.screens

    import androidx.compose.foundation.Image
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.material3.*
    import androidx.compose.runtime.*
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.unit.dp
    import org.example.pagepalapp.R // if you're in Android source set
    import org.example.pagepalapp.models.Book // create this model later if you need
    import androidx.lifecycle.viewmodel.compose.viewModel
    import kotlinx.coroutines.*
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Search


@Composable
fun BookListScreen(
    books: List<Book>,
    onSearch: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) } // 👈 loading state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 🔍 Search Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.weight(4f),
                placeholder = { Text("Search books...") }
            )

            Button(
                onClick = {
                    isLoading = true
                    onSearch(query)
                    // use coroutine scope for delay
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(2000)
                        isLoading = false
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            }
        }

        // Show loading indicator while fetching
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // 📚 Show list of books when not loading
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(books) { book ->
                    BookItem(book)
                }
            }
        }
    }
}


    @Composable
    fun BookItem(book: Book) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(book.title, style = MaterialTheme.typography.titleMedium)
                Text(book.author, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
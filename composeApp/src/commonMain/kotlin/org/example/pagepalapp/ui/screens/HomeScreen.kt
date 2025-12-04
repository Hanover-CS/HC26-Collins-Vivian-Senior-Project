@file:OptIn(ExperimentalMaterial3Api::class)
package org.example.pagepalapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.pagepalapp.ui.components.BookCard
import org.example.pagepalapp.data.HomeViewModel
import org.example.pagepalapp.ui.components.BottomNavigationBar
import org.example.pagepalapp.ui.components.QuoteCard

data class ReadingMood(
    val emoji: String,
    val label: String
)

private val readingMoods = listOf(
    ReadingMood("😌", "Calm"),
    ReadingMood("😭", "Sad"),
    ReadingMood("🧠", "Educational"),
    ReadingMood("😂", "Funny")
)

// search suggestions
private val searchSuggestions = listOf(
    "Fantasy",
    "Romance",
    "Mystery",
    "Young Adult",
    "Classics",
    "Sci-Fi",
    "Non-fiction"
)

@Composable
fun ReadingMoodSelector(
    selectedMood: ReadingMood?,
    onMoodSelected: (ReadingMood) -> Unit
) {
    Column(modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)) {
        Text(
            text = "Reading mood",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            readingMoods.forEach { mood ->
                val isSelected = mood == selectedMood

                AssistChip(
                    onClick = { onMoodSelected(mood) },
                    label = { Text(mood.label) },
                    leadingIcon = { Text(mood.emoji) },
                    // optional little styling
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (isSelected)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        else
                            MaterialTheme.colorScheme.surface
                    )
                )
            }
        }
    }
}


@Composable
fun SearchSuggestionsRow(
    onSuggestionClick: (String) -> Unit
) {
    Column(modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)) {
        Text(
            text = "Quick suggestions",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            searchSuggestions.forEach { suggestion ->
                FilterChip(
                    selected = false,
                    onClick = { onSuggestionClick(suggestion) },
                    label = { Text(suggestion) }
                )
            }
        }
    }
}
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {

    val query by viewModel.query.collectAsState()
    val books by viewModel.results.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var selectedMood by remember { mutableStateOf<ReadingMood?>(null) }

    val scope = rememberCoroutineScope()

    // pink background gradient
    val backgroundBrush = Brush.verticalGradient(
        listOf(
            Color(0xFFF9EAF3),
            Color(0xFFF7F4FF)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PagePal", fontSize = 26.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = Color.Transparent
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(backgroundBrush)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(Modifier.height(6.dp))
            QuoteCard()
            Spacer(Modifier.height(24.dp))

            ReadingMoodSelector(
                selectedMood = selectedMood,
                onMoodSelected = { mood ->
                    selectedMood = mood
                    viewModel.logMoodForToday(mood.emoji)

                    // also mark today as a reading day
                    viewModel.logReading(
                        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                    )
                }
            )

            Spacer(Modifier.height(12.dp))

            // floating search box card
            ElevatedCard(
                shape = RoundedCornerShape(22.dp),
                elevation = CardDefaults.elevatedCardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF3E5F5)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(20.dp)) {

                    OutlinedTextField(
                        value = query,
                        onValueChange = viewModel::onQueryChange,
                        label = { Text("Search for books") },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF9C27B0),
                            unfocusedBorderColor = Color(0xFFCE93D8),
                            focusedLabelColor = Color(0xFF7B1FA2)
                        ),
                        singleLine = true
                    )

                    // searches by subject and not by keywords
                    SearchSuggestionsRow { suggestion ->
                        val subjectQuery = "subject:\"$suggestion\""
                        viewModel.onQueryChange(subjectQuery)
                        scope.launch {
                            viewModel.searchBooks()
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.searchBooks()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF9C27B0),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Search", fontSize = 16.sp)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            if (!isLoading && books.isNotEmpty()) {
                Text(
                    text = "Results",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF6A1B9A),
                    modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
                )
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF9C27B0))
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
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

private fun HomeViewModel.logReading(date: LocalDate) {}

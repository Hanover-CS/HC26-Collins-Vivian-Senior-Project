/**
 * displays the full details about a selected book, including:
 *  - cover image
 *  - title and author(s)
 *  - description text
 *  - add/remove from library button
 *  - reading progress fields
 *  - rating bar (full + half stars)
 */

@file:OptIn(ExperimentalMaterial3Api::class)

package org.example.pagepalapp.data

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.*

class HomeViewModel : ViewModel() {

    private val api = NetworkModule.booksApi

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _results = MutableStateFlow<List<Volume>>(emptyList())
    val results: StateFlow<List<Volume>> = _results

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _moodLogs = MutableStateFlow<Map<LocalDate, String>>(emptyMap())
    val moodLogs: StateFlow<Map<LocalDate, String>> = _moodLogs

    // personal library
    private val _libraryBooks = MutableStateFlow<List<Volume>>(emptyList())
    val libraryBooks: StateFlow<List<Volume>> = _libraryBooks

    private val _libraryEvent = MutableStateFlow<String?>(null)
    val libraryEvent: StateFlow<String?> = _libraryEvent

    fun addToLibrary(book: Volume) {
        if (_libraryBooks.value.any { it.id == book.id }) return

        // autofills total pages from Google Books
        val googlePages = book.volumeInfo?.pageCount
        if (googlePages != null && googlePages > 0 && book.totalPages == 0) {
            book.totalPages = googlePages
        }

        _libraryBooks.value = _libraryBooks.value + book

        _libraryEvent.value = "Added to library!"
    }

    fun removeFromLibrary(bookId: String) {
        _libraryBooks.value = _libraryBooks.value.filter { it.id != bookId }
    }

    fun isBookInLibrary(bookId: String): Boolean =
        _libraryBooks.value.any { it.id == bookId }


    // book lookup
    fun getBookById(id: String?): Volume? {
        if (id.isNullOrBlank()) return null

        val book =
            _results.value.firstOrNull { it.id == id }
                ?: _libraryBooks.value.firstOrNull { it.id == id }
                ?: return null

        // autofill Google pageCount
        val googlePages = book.volumeInfo?.pageCount
        if (googlePages != null && googlePages > 0 && book.totalPages == 0) {
            book.totalPages = googlePages
        }

        return book
    }

    // searchbar
    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    suspend fun searchBooks() {
        val q = _query.value.trim()
        if (q.isEmpty()) return

        _isLoading.value = true
        _error.value = null

        try {
            val resp = api.searchBooks(q)
            _results.value = resp.body()?.items ?: emptyList()
        } catch (e: Exception) {
            _error.value = e.message ?: "Network error"
        } finally {
            _isLoading.value = false
        }
    }


    // book progress
    fun updateBookProgress(bookId: String, currentPage: Int, totalPages: Int) {
        _libraryBooks.value = _libraryBooks.value.map { book ->
            if (book.id == bookId) book.copy(
                currentPage = currentPage,
                totalPages = totalPages
            )
            else book
        }
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        _readingLogs.value = _readingLogs.value + today
        updateStreak()
    }


    // book rating
    fun updateBookRating(bookId: String, rating: Double) {
        _libraryBooks.value = _libraryBooks.value.map { book ->
            if (book.id == bookId) book.copy(rating = rating) else book
        }
    }


    // reading log in calendar
    private val _readingLogs = MutableStateFlow<Set<LocalDate>>(emptySet())
    val readingLogs: StateFlow<Set<LocalDate>> = _readingLogs

    fun toggleReadingDay(date: LocalDate) {
        _readingLogs.value =
            if (date in _readingLogs.value)
                _readingLogs.value - date
            else
                _readingLogs.value + date

        updateStreak()
    }


    // streak
    private val _previousStreak = MutableStateFlow(0)
    val previousStreak: StateFlow<Int> = _previousStreak

    private val _streak = MutableStateFlow(0)
    val streak: StateFlow<Int> = _streak

    fun calculateStreak(): Int {
        val logs = _readingLogs.value.toList().sortedDescending()
        if (logs.isEmpty()) return 0

        var count = 1
        for (i in 0 until logs.size - 1) {
            if (logs[i].minus(DatePeriod(days = 1)) == logs[i + 1]) {
                count++
            } else break
        }
        return count
    }

    private fun updateStreak() {
        val newStreak = calculateStreak()
        _streak.value = newStreak

        // Used for confetti + banner animation triggers
        if (newStreak > _previousStreak.value) {
            _previousStreak.value = newStreak
        }
    }

    fun logMoodForToday(moodEmoji: String) {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        _moodLogs.value = _moodLogs.value + (today to moodEmoji)
    }
}

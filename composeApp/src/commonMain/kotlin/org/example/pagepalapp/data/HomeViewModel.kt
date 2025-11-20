/**
 * manages:
 *  - getting books from Google Books API
 *  - loading state
 *  - search query input
 *  - search results list
 *  - user's personal library (in-memory list)
 *  - progress tracking (currentPage, totalPages)
 */

package org.example.pagepalapp.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    // API instance
    private val api = NetworkModule.booksApi

    // search bar text
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    // results returned from the Google Books API
    private val _results = MutableStateFlow<List<Volume>>(emptyList())
    val results: StateFlow<List<Volume>> = _results

    // shows progress indicator
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // error messages from network failures
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // user's in-memory personal library
    private val _libraryBooks = MutableStateFlow<List<Volume>>(emptyList())
    val libraryBooks: StateFlow<List<Volume>> = _libraryBooks

    // updates text in search bar
    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    // calls Google Books API based on search input
    fun searchBooks() {
        val currentQuery = _query.value.trim()
        if (currentQuery.isEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val resp = api.searchBooks(query = currentQuery)

                if (resp.isSuccessful) {
                    // Extract book list from response
                    val books = resp.body()?.items ?: emptyList()
                    _results.value = books
                } else {
                    _error.value = "API error: ${resp.code()}"
                }

            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // retrieves a book using either search results OR saved library
    fun getBookById(id: String?): Volume? {
        if (id.isNullOrBlank()) return null
        return _results.value.firstOrNull { it.id == id }
            ?: _libraryBooks.value.firstOrNull { it.id == id }
    }

    // adds a book to the user's library (avoids duplicates)
    fun addToLibrary(book: Volume) {
        val current = _libraryBooks.value.toMutableList()
        if (current.none { it.id == book.id }) {
            current.add(book)
            _libraryBooks.value = current
        }
    }

    // removes a book from the personal library
    fun removeFromLibrary(bookId: String) {
        _libraryBooks.value = _libraryBooks.value.filterNot { it.id == bookId }
    }

    // checks if a book is saved
    fun isBookInLibrary(bookId: String) =
        _libraryBooks.value.any { it.id == bookId }

    // updates progress tracking fields inside the library list
    fun updateBookProgress(bookId: String, currentPage: Int, totalPages: Int) {
        _libraryBooks.value = _libraryBooks.value.map { book ->
            if (book.id == bookId)
                book.copy(currentPage = currentPage, totalPages = totalPages)
            else book
        }
    }
}

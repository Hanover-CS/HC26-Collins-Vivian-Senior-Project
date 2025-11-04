package org.example.pagepalapp.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    // API reference
    private val api = NetworkModule.booksApi

    // query text
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    // search results from Google Books API
    private val _results = MutableStateFlow<List<Volume>>(emptyList())
    val results: StateFlow<List<Volume>> = _results

    // loading & error state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // user’s personal library
    private val _libraryBooks = MutableStateFlow<List<Volume>>(emptyList())
    val libraryBooks: StateFlow<List<Volume>> = _libraryBooks

    // event handlers below
    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    fun searchBooks() {
        val currentQuery = _query.value.trim()
        if (currentQuery.isEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val resp = api.searchBooks(query = currentQuery)
                if (resp.isSuccessful) {
                    val books = resp.body()?.items ?: emptyList()
                    _results.value = books

                    println("Fetched ${books.size} books:")
                    books.forEach {
                        println("→ ID: ${it.id}, Title: ${it.volumeInfo?.title}")
                    }
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

// library management below

    /** retrieves a book either from search results or from user's library */
    fun getBookById(id: String?): Volume? {
        println("Looking up book ID: $id")
        if (id.isNullOrBlank()) return null
        return _results.value.firstOrNull { it.id == id }
            ?: _libraryBooks.value.firstOrNull { it.id == id }
    }

    /** adds a book to the user's personal library (if not already added) */
    fun addToLibrary(book: Volume) {
        val current = _libraryBooks.value.toMutableList()
        if (current.none { it.id == book.id }) { // avoid duplicates
            current.add(book)
            _libraryBooks.value = current
            println("✅ Added to library: ${book.volumeInfo?.title}")
        } else {
            println("⚠️ Book already in library: ${book.volumeInfo?.title}")
        }
    }

    /** Removes a book from the library */
    fun removeFromLibrary(bookId: String) {
        _libraryBooks.value = _libraryBooks.value.filterNot { it.id == bookId }
        println("🗑️ Removed book with ID: $bookId")
    }

    /** this will check if a given book is already in the user's library */
    fun isBookInLibrary(bookId: String): Boolean {
        return _libraryBooks.value.any { it.id == bookId }
    }

    fun updateBookProgress(bookId: String, currentPage: Int, totalPages: Int) {
        val updatedLibrary = _libraryBooks.value.map { book ->
            if (book.id == bookId) book.copy(currentPage = currentPage, totalPages = totalPages)
            else book
        }
        _libraryBooks.value = updatedLibrary
    }

}

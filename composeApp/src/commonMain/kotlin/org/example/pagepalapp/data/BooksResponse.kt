/**
 * Represents the top-level JSON response structure from the Google Books API.
 * The API wraps the book list inside an "items" field, so this model basically
 * just matches that.
 */

package org.example.pagepalapp.data
data class BooksResponse(
    val items: List<Volume>?
)

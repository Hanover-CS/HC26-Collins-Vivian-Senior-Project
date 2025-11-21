/**
 * This file defines the core data structures used throughout the PagePal app.
 */

package org.example.pagepalapp.data

// represents a full book, or a "volume" returned from Google Books API
data class Volume(
    val id: String,                      // unique book ID
    val volumeInfo: VolumeInfo?,         // info about the book

    // reading progress (stored locally)
    var currentPage: Int = 0,
    var totalPages: Int = 0              // auto-filled from Google Books when available
)

// holds metadata returned from the Google Books API
data class VolumeInfo(
    val title: String?,
    val authors: List<String>?,
    val description: String?,
    val imageLinks: ImageLinks?,
    val pageCount: Int? = null,

    // optional additional fields if you ever want more metadata
    val publisher: String? = null,
    val publishedDate: String? = null,
    val categories: List<String>? = null
)

// Image-only section of the API response
data class ImageLinks(
    val thumbnail: String?
)

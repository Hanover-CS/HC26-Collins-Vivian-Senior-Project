/**
 * This file defines the core data structures used throughout the PagePal app.
 */

package org.example.pagepalapp.data

// represents a full book "or a volume" returned from Google Books API
data class Volume(
    val id: String,                      // unique book ID
    val volumeInfo: VolumeInfo?,         // info about the book
    var currentPage: Int = 0,            // user's current reading progress
    var totalPages: Int = 0              // total number of pages (user will enter this manually)
)

// holds data like title, description, authors, images
data class VolumeInfo(
    val title: String?,                  // book title
    val authors: List<String>?,          // list of authors
    val description: String?,            // summary
    val imageLinks: ImageLinks?          // thumbnails from the API
)

// Image-only section of the API response
data class ImageLinks(
    val thumbnail: String?               // essentially a URL to a thumbnail image
)

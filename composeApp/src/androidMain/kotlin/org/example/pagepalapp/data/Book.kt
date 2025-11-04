package org.example.pagepalapp.data

data class Volume(
    val id: String,
    val volumeInfo: VolumeInfo?,
    var currentPage: Int = 0,         // 👈 user’s current progress
    var totalPages: Int = 0
)

data class VolumeInfo(
    val title: String?,
    val authors: List<String>?,
    val description: String?,
    val imageLinks: ImageLinks?
)

data class ImageLinks(
    val thumbnail: String?
)

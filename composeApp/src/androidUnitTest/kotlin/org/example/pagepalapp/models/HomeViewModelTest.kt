package org.example.pagepalapp

import org.junit.Test
import org.junit.Assert.*
import org.example.pagepalapp.data.*

class HomeViewModelTest {

    @Test
    fun addAndRemoveBookWorks() {
        val viewModel = HomeViewModel()

        val book = Volume(
            id = "test123",
            volumeInfo = VolumeInfo(
                title = "Test Book",
                authors = listOf("Author"),
                description = "desc",
                imageLinks = ImageLinks(null)
            )
        )

        // add book
        viewModel.addToLibrary(book)
        assertTrue(viewModel.isBookInLibrary("test123"))

        // remove book
        viewModel.removeFromLibrary("test123")
        assertFalse(viewModel.isBookInLibrary("test123"))
    }
}

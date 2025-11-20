/**
 * serves as the data access layer for local SQLDelight storage.
 */

package org.example.pagepalapp.data

import app.cash.sqldelight.db.SqlDriver
import org.example.pagepalapp.db.PagePalDatabase

class BookRepository(driver: SqlDriver) {

    private val database = PagePalDatabase(driver)
    private val queries = database.bookQueries         // auto-generated query interface

    // will return a full list of saved books from SQLDelight storage
    fun getAllBooks() = queries.getAllBooks().executeAsList()

    // inserts a new book into the database
    fun insertBook(title: String, author: String?, pages: Int?, mood: String?) {
        queries.insertBook(
            title = title,
            author = author,
            pages = pages?.toLong(),
            mood = mood
        )
    }

    // deletes all books and is used for resetting the database
    fun clearBooks() {
        queries.deleteAll()
    }
}

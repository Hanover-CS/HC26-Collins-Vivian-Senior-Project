/**
 * Retrofit interface for communicating with the Google Books API.
 */

package org.example.pagepalapp.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApiService {

    // performs a GET request to https://www.googleapis.com/books/v1/volumes
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,        // user's search term
        @Query("maxResults") maxResults: Int = 20
    ): Response<BooksResponse>
}

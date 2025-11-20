/**
 * provides Retrofit and OkHttp configuration for Google Books API requests.
 *
 * Responsibilities:
 *  - build OkHttp client
 *  - attach interceptor that adds the API key to every request
 *  - enable HTTP logging for debugging
 *  - create a Retrofit instance
 *  - expose a singleton API interface (`booksApi`)
 *
 * used only by HomeViewModel.
 */

package org.example.pagepalapp.data

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.example.pagepalapp.BuildConfig

object NetworkModule {

    private const val BASE_URL = "https://www.googleapis.com/books/v1/"

    // attaches "?key=<API_KEY>" to every API request
    private val apiKeyAppendingInterceptor = Interceptor { chain ->
        val original = chain.request()

        val newUrl = original.url.newBuilder()
            .addQueryParameter("key", BuildConfig.GOOGLE_BOOKS_API_KEY)
            .build()

        chain.proceed(original.newBuilder().url(newUrl).build())
    }

    // logs API traffic to Logcat
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // builds the OkHttp client with interceptors
    private val okHttp = OkHttpClient.Builder()
        .addInterceptor(apiKeyAppendingInterceptor)
        .addInterceptor(logging)
        .build()

    // builds the Retrofit service
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttp)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // exposes the Google Books API interface
    val booksApi: GoogleBooksApiService by lazy {
        retrofit.create(GoogleBooksApiService::class.java)
    }
}

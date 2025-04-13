package com.example.myapplication.data

import android.content.Context
import com.example.myapplication.data.Movie
import com.example.myapplication.data.MovieDatabase
import com.example.myapplication.data.TMDbService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(
    private val service: TMDbService,
    context: Context
) {
    private val dao = MovieDatabase.getInstance(context).movieDao()

    suspend fun fetchAndStoreMovies(apiKey: String) {
        val response = service.getPopularMovies(apiKey)
        response.results.map { it.toMovie() }.forEach {
            dao.insert(it)
        }
    }

    suspend fun getAllMovies(): List<Movie> = dao.getAll()
}
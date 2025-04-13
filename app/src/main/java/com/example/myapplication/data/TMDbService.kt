package com.example.myapplication.data

import retrofit2.http.GET
import retrofit2.http.Query

interface TMDbService {
    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("2b8a64199789a82f5cff8b0f159e69f8") apiKey: String): TMDbResponse
}

data class TMDbResponse(
    val results: List<TMDBMovie>
)

data class TMDBMovie(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String?
) {
    fun toMovie(): Movie = Movie(id, title, overview, poster_path)
}
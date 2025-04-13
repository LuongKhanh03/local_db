package com.example.myapplication.data

import androidx.room.*

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie): Unit

    @Query("SELECT * FROM movies")
    suspend fun getAll(): List<Movie>

}
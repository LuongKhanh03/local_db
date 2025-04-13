package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.Movie
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.data.TMDbService
import com.example.myapplication.data.MovieRepository
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: MovieRepository
    private var selectedMovieId: Int? = null

    private val pickVideo =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedMovieId?.let { id ->
                    saveFilePathToMovie(id, it.toString())
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(TMDbService::class.java)
        repository = MovieRepository(service, this)

        binding.btnFetch.setOnClickListener {
            lifecycleScope.launch {
                repository.fetchAndStoreMovies("")
                val movies = repository.getAllMovies()
                binding.textView.text = movies.joinToString("\n") { it.title }
                if (movies.isNotEmpty()) {
                    selectedMovieId = movies.first().id
                }
            }
        }

        binding.btnPickFile.setOnClickListener {
            pickVideo.launch("video/*")
        }
    }

    private fun saveFilePathToMovie(id: Int, uri: String) {
        lifecycleScope.launch {
            val movies = repository.getAllMovies()
            val updated = movies.find { it.id == id }?.copy(localFilePath = uri)
            updated?.let { repository.fetchAndStoreMovies("") } // Re-insert để update
        }
    }
}


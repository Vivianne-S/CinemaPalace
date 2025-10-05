package com.example.cinemapalace.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cinemapalace.data.model.TmdbMovie
import com.example.cinemapalace.ui.components.MovieCard
import com.example.cinemapalace.ui.home.HomeViewModel
import com.example.cinemapalace.util.Result

@Composable
fun AllMoviesScreen(
    category: String,
    viewModel: HomeViewModel,
    onMovieSelected: (TmdbMovie) -> Unit,
    onBackClick: () -> Unit
) {
    val moviesState = when (category) {
        "popular" -> viewModel.popular.collectAsState().value
        "now_playing" -> viewModel.nowPlaying.collectAsState().value
        "upcoming" -> viewModel.upcoming.collectAsState().value
        else -> Result.Error("Okänd kategori")
    }

    val title = when (category) {
        "popular" -> "Topplista"
        "now_playing" -> "På bio nu"
        "upcoming" -> "Kommande filmer"
        else -> "Filmer"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xFF0A0A0A), Color(0xFF181818)))
            )
            .padding(8.dp)
    ) {
        // 🔙 Back-knapp
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Tillbaka",
                    tint = Color.White
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFFFC107)
            )
        }

        when (moviesState) {
            is Result.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFFFC107))
            }

            is Result.Error -> Text(
                text = "Fel: ${moviesState.message}",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )

            is Result.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(160.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(moviesState.data) { movie ->
                        MovieCard(
                            movie = movie,
                            onClick = { onMovieSelected(movie) },
                            showPremiere = category == "upcoming"
                        )
                    }
                }
            }
        }
    }
}
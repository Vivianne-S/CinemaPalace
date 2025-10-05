package com.example.cinemapalace.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cinemapalace.data.model.TmdbMovie
import com.example.cinemapalace.ui.components.MovieCard
import com.example.cinemapalace.util.Result

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onMovieSelected: (TmdbMovie) -> Unit,
    onSeeAllClick: (String) -> Unit = {}
) {
    val popularState = viewModel.popular.collectAsState()
    val nowPlayingState = viewModel.nowPlaying.collectAsState()
    val upcomingState = viewModel.upcoming.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0A0A0A), Color(0xFF181818))
                )
            ),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // 🎬 Header högst upp
        item { CinemaPalaceHeader() }

        // Topplista
        item { SectionHeader("Topplista", onSeeAll = { onSeeAllClick("popular") }) }
        item { SectionRow(popularState.value, onMovieSelected) }

        // På bio nu
        item { SectionHeader("På bio nu", onSeeAll = { onSeeAllClick("now_playing") }) }
        item { SectionRow(nowPlayingState.value, onMovieSelected) }

        // Kommande filmer
        item { SectionHeader("Kommande filmer", onSeeAll = { onSeeAllClick("upcoming") }) }
        item { SectionRow(upcomingState.value, onMovieSelected, showPremiere = true) }

        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

@Composable
private fun CinemaPalaceHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 12.dp)
    ) {
        Text(
            text = "CINEMA PALACE",
            color = Color(0xFFFFC107),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Tillsammans upplever vi filmens magi",
            color = Color.LightGray,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SectionHeader(title: String, onSeeAll: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color(0xFFFFC107),
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = "Visa alla ▶",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray),
            modifier = Modifier
                .clickable { onSeeAll() }
                .padding(end = 8.dp)
        )
    }
}

@Composable
private fun SectionRow(
    result: Result<List<TmdbMovie>>,
    onMovieSelected: (TmdbMovie) -> Unit,
    showPremiere: Boolean = false
) {
    when (result) {
        is Result.Loading -> Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFFFFC107))
        }

        is Result.Error -> Text(
            text = "Fel: ${result.message}",
            color = Color.Red,
            modifier = Modifier.padding(16.dp)
        )

        is Result.Success -> {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(result.data, key = { it.id }) { movie ->
                    MovieCard(
                        movie = movie,
                        onClick = { onMovieSelected(movie) },
                        showPremiere = showPremiere
                    )
                }
            }
        }
    }
}
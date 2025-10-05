package com.example.cinemapalace.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.cinemapalace.data.model.TmdbMovie
import com.example.cinemapalace.data.remote.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun ShowtimeListScreen() {
    val scope = rememberCoroutineScope()
    var movies by remember { mutableStateOf<List<TmdbMovie>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val data = RetrofitInstance.api.getPopularMovies()
                movies = data.results
            } catch (e: Exception) {
                error = e.localizedMessage
            } finally {
                isLoading = false
            }
        }
    }

    when {
        isLoading -> Text("⏳ Laddar filmer...", Modifier.padding(16.dp))
        error != null -> Text("❌ Fel: $error", Modifier.padding(16.dp))
        movies.isEmpty() -> Text("📭 Inga filmer hittades", Modifier.padding(16.dp))
        else -> LazyColumn(Modifier.fillMaxSize()) {
            items(movies) { movie ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(movie.title, fontWeight = FontWeight.Bold)
                        Text(
                            movie.overview,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text("⭐ ${movie.rating ?: "N/A"}")
                    }
                }
            }
        }
    }
}
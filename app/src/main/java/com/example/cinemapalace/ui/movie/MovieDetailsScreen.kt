package com.example.cinemapalace.ui.movie

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberAsyncImagePainter
import com.example.cinemapalace.data.model.TmdbMovie
import com.example.cinemapalace.data.model.ShowtimeWithMovie
import com.example.cinemapalace.data.remote.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun MovieDetailsScreen(
    movie: TmdbMovie,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    // 🔹 Håller visningarna
    var showtimes by remember { mutableStateOf<List<ShowtimeWithMovie>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // 🔹 Hämta showtimes från backend (Ktor)
    LaunchedEffect(movie.id) {
        scope.launch {
            try {
                val response = RetrofitInstance.api.getShowtimesForMovie(movie.id)
                showtimes = response
            } catch (e: Exception) {
                error = e.localizedMessage
            } finally {
                isLoading = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .background(
                    Brush.verticalGradient(listOf(Color(0xFF0A0A0A), Color(0xFF181818)))
                )
                .padding(bottom = 80.dp)
        ) {
            // 🔙 Back button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Tillbaka",
                        tint = Color.White
                    )
                }
            }

            // 🎬 Poster
            Image(
                painter = rememberAsyncImagePainter(movie.posterPath),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🎞️ Titel + info
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFC107)
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Premiär: ${movie.releaseDate ?: "Okänt"}",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = movie.overview.ifEmpty { "Ingen beskrivning tillgänglig." },
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🎟️ Visningar
            Text(
                text = "Biljetter & Visningar",
                color = Color(0xFFFFC107),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            when {
                isLoading -> Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFFFC107))
                }

                error != null -> Text(
                    text = "❌ Fel: $error",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )

                showtimes.isEmpty() -> Text(
                    text = "🎬 Inga visningar tillgängliga.",
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )

                else -> Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    showtimes.groupBy { it.theaterId }.forEach { (theater, times) ->
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = theater,
                            style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
                            fontWeight = FontWeight.Bold
                        )

                        times.forEach { showtime ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1C))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = showtime.startTime,
                                            color = Color(0xFFFFC107),
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Salong ${showtime.hallId}",
                                            color = Color.LightGray,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    Button(
                                        onClick = { /* TODO: Visa platser (nästa steg) */ },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFFFC107)
                                        )
                                    ) {
                                        Text(
                                            text = "Välj plats",
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}
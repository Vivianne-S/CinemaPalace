package com.example.cinemapalace.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cinemapalace.data.model.ShowtimeWithMovie
import com.example.cinemapalace.ui.components.MovieCard
import com.example.cinemapalace.util.Result

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onMovieSelected: (ShowtimeWithMovie) -> Unit
) {
    // 🔹 Collectar StateFlow korrekt så UI uppdateras automatiskt
    val showtimesState = viewModel.showtimesState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0A0A0A), Color(0xFF181818))
                )
            )
    ) {
        when (val state = showtimesState.value) {
            is Result.Loading -> {
                CircularProgressIndicator(
                    color = Color(0xFFFFC107),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is Result.Success -> {
                val showtimes = state.data

                if (showtimes.isEmpty()) {
                    Text(
                        text = "🎥 Inga visningar tillgängliga just nu",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 60.dp)
                    ) {
                        Text(
                            text = "🎬 CinemaPalace",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = Color(0xFFFFC107),
                                fontWeight = FontWeight.ExtraBold
                            ),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        Text(
                            text = "Aktuella filmer",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(showtimes) { showtime ->
                                MovieCard(
                                    movie = showtime.movie,
                                    onClick = { onMovieSelected(showtime) }
                                )
                            }
                        }
                    }
                }
            }

            is Result.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Kunde inte ladda filmer",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.message,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.loadShowtimes() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
                    ) {
                        Text("Försök igen", color = Color.Black)
                    }
                }
            }
        }
    }
}
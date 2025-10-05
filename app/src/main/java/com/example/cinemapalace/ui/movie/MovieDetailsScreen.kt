package com.example.cinemapalace.ui.movie

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.cinemapalace.data.model.TmdbMovie

@Composable
fun MovieDetailsScreen(
    movie: TmdbMovie,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 🔹 Scrollbart innehåll
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0A0A0A), Color(0xFF181818))
                    )
                )
                .padding(bottom = 80.dp) // extra luft för fade
        ) {
            // 🔙 Back-knapp
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

            // 🎬 Filmens poster
            Image(
                painter = rememberAsyncImagePainter(
                    model = "https://image.tmdb.org/t/p/w500${movie.posterPath}"
                ),
                contentDescription = "Poster för ${movie.title}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 📄 Film-info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFC107)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                movie.releaseDate?.let {
                    Text(
                        text = "Premiär: $it",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.LightGray
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = movie.overview.ifEmpty { "Ingen beskrivning tillgänglig." },
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 🎟️ Boka-knapp
                Button(
                    onClick = { /* TODO: bokningsflöde */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFC107)
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = "Boka biljetter 🎟️",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(60.dp))
            }
        }

        // 🔥 Fade-effekt längst ner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xFF000000))
                    )
                )
        )
    }
}
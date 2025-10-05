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
import com.example.cinemapalace.data.model.MovieDto

@Composable
fun MovieDetailsScreen(
    movie: MovieDto,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0A0A0A), Color(0xFF181818))
                )
            )
            .verticalScroll(scrollState)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Enkel back-knapp utan TopAppBar
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

            // Movie poster
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

            Spacer(modifier = Modifier.height(20.dp))

            // Movie details
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Book button
                Button(
                    onClick = {
                        // TODO: lägg till navigation till bokning här
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFC107)
                    )
                ) {
                    Text(
                        text = "Boka biljetter 🎟️",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
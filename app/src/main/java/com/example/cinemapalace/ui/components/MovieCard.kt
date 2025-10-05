package com.example.cinemapalace.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import coil.compose.rememberAsyncImagePainter
import com.example.cinemapalace.data.model.TmdbMovie
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MovieCard(
    movie: TmdbMovie,
    onClick: () -> Unit,
    showPremiere: Boolean = false
) {
    Box(
        modifier = Modifier
            .width(180.dp)
            .padding(8.dp)
            .clickable { onClick() }
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF121212), Color(0xFF1E1E1E))
                )
            )
    ) {
        Column {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(movie.posterPath ?: ""),
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )

                // 🔹 Visa "Premiär 24 oktober" istället för 2025-10-24
                if (showPremiere && movie.releaseDate != null) {
                    val formattedDate = try {
                        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val outputFormat = SimpleDateFormat("d MMMM", Locale("sv"))
                        val date = inputFormat.parse(movie.releaseDate)
                        "Premiär ${outputFormat.format(date!!)}"
                    } catch (e: Exception) {
                        "Premiär ${movie.releaseDate}"
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFFC107))
                            .padding(vertical = 4.dp)
                            .align(Alignment.BottomCenter)
                    ) {
                        Text(
                            text = formattedDate,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // 🎞 Filmtitel
            Text(
                text = movie.title,
                color = Color.White,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}
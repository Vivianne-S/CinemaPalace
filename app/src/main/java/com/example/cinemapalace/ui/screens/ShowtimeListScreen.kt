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
import com.example.cinemapalace.data.model.ShowtimeWithMovie
import com.example.cinemapalace.data.remote.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun ShowtimeListScreen() {
    val scope = rememberCoroutineScope()
    var showtimes by remember { mutableStateOf<List<ShowtimeWithMovie>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val data = RetrofitInstance.api.getAllShowtimes()
                showtimes = data
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
        showtimes.isEmpty() -> Text("📭 Inga filmer hittades", Modifier.padding(16.dp))
        else -> LazyColumn(Modifier.fillMaxSize()) {
            items(showtimes) { st ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(st.movie.title, fontWeight = FontWeight.Bold)
                        Text(
                            st.movie.overview,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text("🎬 ${st.startTime}")
                    }
                }
            }
        }
    }
}
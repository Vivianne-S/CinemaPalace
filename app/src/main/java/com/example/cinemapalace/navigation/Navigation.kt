package com.example.cinemapalace.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cinemapalace.ui.home.HomeScreen
import com.example.cinemapalace.ui.home.HomeViewModel
import com.example.cinemapalace.ui.movie.MovieDetailsScreen

@Composable
fun AppNavigation(viewModel: HomeViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onMovieSelected = { showtime ->
                    navController.navigate("details/${showtime.movie.id}")
                }
            )
        }

        composable(
            "details/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.StringType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId") ?: return@composable

            val showtimeWithMovie = viewModel.getMovieById(movieId)

            if (showtimeWithMovie != null) {
                MovieDetailsScreen(
                    movie = showtimeWithMovie.movie,
                    onBackClick = { navController.popBackStack() }
                )
            } else {
                // Om filmen inte hittas, gå tillbaka
                navController.popBackStack()
            }
        }
    }
}
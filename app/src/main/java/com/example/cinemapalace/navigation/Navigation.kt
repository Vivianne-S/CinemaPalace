package com.example.cinemapalace.navigation

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.foundation.layout.WindowInsets
import com.example.cinemapalace.data.model.TmdbMovie
import com.example.cinemapalace.ui.home.HomeScreen
import com.example.cinemapalace.ui.home.HomeViewModel
import com.example.cinemapalace.ui.movie.MovieDetailsScreen
import com.example.cinemapalace.ui.screens.AllMoviesScreen

@Composable
fun AppNavigation(viewModel: HomeViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Filmer.route,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            // 🎬 Startsida
            composable(BottomNavItem.Filmer.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onMovieSelected = { movie: TmdbMovie ->
                        navController.navigate("details/${movie.id}")
                    },
                    onSeeAllClick = { category ->
                        navController.navigate("all/$category")
                    }
                )
            }

            // 🎞 Alla filmer per kategori
            composable(
                "all/{category}",
                arguments = listOf(navArgument("category") { type = NavType.StringType })
            ) { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category") ?: return@composable
                AllMoviesScreen(
                    category = category,
                    viewModel = viewModel,
                    onMovieSelected = { movie ->
                        navController.navigate("details/${movie.id}")
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 🎞 Detaljsida
            composable(
                "details/{movieId}",
                arguments = listOf(navArgument("movieId") { type = NavType.StringType })
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId") ?: return@composable
                val movie = viewModel.getMovieById(movieId)

                if (movie != null) {
                    MovieDetailsScreen(
                        movie = movie,
                        onBackClick = { navController.popBackStack() }
                    )
                } else {
                    navController.popBackStack()
                }
            }

            // Övriga menyer
            composable(BottomNavItem.Sok.route) { Text("Sök – under utveckling") }
            composable(BottomNavItem.Kop.route) { Text("Köp – under utveckling") }
            composable(BottomNavItem.Biografer.route) { Text("Biografer – under utveckling") }
            composable(BottomNavItem.LoggaIn.route) { Text("Logga in – under utveckling") }
        }
    }
}
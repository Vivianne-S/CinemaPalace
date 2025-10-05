package com.example.cinemapalace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cinemapalace.navigation.AppNavigation
import com.example.cinemapalace.ui.home.HomeViewModel
import com.example.cinemapalace.ui.theme.CinemaPalaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CinemaPalaceTheme {
                val homeViewModel: HomeViewModel = viewModel()
                AppNavigation(viewModel = homeViewModel)
            }
        }
    }
}
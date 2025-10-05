package com.example.cinemapalace.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Filmer : BottomNavItem("home", "Filmer", Icons.Filled.Movie)
    object Sok : BottomNavItem("search", "Sök", Icons.Filled.Search)
    object Kop : BottomNavItem("tickets", "Köp", Icons.Filled.ShoppingCart)
    object Biografer : BottomNavItem("theaters", "Biografer", Icons.Filled.Map)
    object LoggaIn : BottomNavItem("login", "Logga in", Icons.Filled.Person)
}
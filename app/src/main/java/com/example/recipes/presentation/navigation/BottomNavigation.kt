package com.example.recipes.presentation.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable

object BottomNavigation {

    @Composable
    fun BottomNavigationBar(
        currentScreen: String,
        onNavigationSelected: (String) -> Unit
    ){
        BottomNavigation() {
            BottomNavigationItem(
                icon = {
                    Icon(Icons.Default.Favorite, "Избранное")
                },
                label = { Text("Избранное") },
                selected = currentScreen == "favorites",
                onClick = {onNavigationSelected("favorites")}
            )

            BottomNavigationItem(
                icon = {
                    Icon(Icons.Default.Search, "Поиск")
                },
                label = {Text("Поиск")},
                selected = currentScreen == "search",
                onClick = {onNavigationSelected("search")}
            )
        }
    }
}
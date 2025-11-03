package com.example.recipes.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.recipes.presentation.navigation.BottomNavigation
import com.example.recipes.presentation.navigation.BottomNavigation.BottomNavigationBar
import com.example.recipes.presentation.screen.RecipeListScreen.RecipeListScreen
import com.example.recipes.presentation.viewmodel.RecipesViewModel

@Composable
fun MainScreen(
    viewModel: RecipesViewModel,
    navController: NavController
) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentScreen = currentScreen,
                onNavigationSelected = { screen ->
                    viewModel.navigateTo(screen)
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentScreen) {
                "recipes" -> {
                    RecipeListScreen(
                        viewModel = viewModel,
                        onMealClick = { meal ->
                            navController.navigate("recipe_detail/${meal.idMeal}")
                        }
                    )
                }
                "favorites" -> {
                    FavoriteRecipesScreen(
                        viewModel = viewModel,
                        onMealClick = { meal ->
                            navController.navigate("recipe_detail/${meal.idMeal}")
                        },

                    )
                }
                "search" -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Поиск будет реализован позже")
                    }
                }
            }
        }
    }
}
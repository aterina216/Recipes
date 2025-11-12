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
import com.example.recipes.data.remote.model.Meal
import com.example.recipes.presentation.navigation.BottomNavigation
import com.example.recipes.presentation.navigation.BottomNavigation.BottomNavigationBar
import com.example.recipes.presentation.screen.RecipeListScreen.RecipeListScreen
import com.example.recipes.presentation.viewmodel.RecipesViewModel

@Composable
fun MainScreen(
    viewModel: RecipesViewModel,
    onMealClick: (Meal) -> Unit,
    onCategoryClick: (String) -> Unit
) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavigation.BottomNavigationBar(
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
                    RecipeListScreen.RecipeListScreen(
                        viewModel = viewModel,
                        onMealClick = onMealClick
                    )
                }
                "favorites" -> {
                    FavoriteRecipesScreen(
                        viewModel = viewModel,
                        onMealClick = onMealClick
                    )
                }
                "search" -> {
                    SearchScreen(
                        viewModel = viewModel,
                        onMealClick = onMealClick
                    )
                }
                "categories" -> {
                    CategoriesScreen(
                        viewModel = viewModel,
                        onCategoryClick = onCategoryClick
                    )
                }
            }
        }
    }
}
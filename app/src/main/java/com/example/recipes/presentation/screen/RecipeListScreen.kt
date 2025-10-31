package com.example.recipes.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipes.presentation.component.RecipeItem
import com.example.recipes.presentation.component.RecipeItem.RecipeItem
import com.example.recipes.presentation.viewmodel.RecipesViewModel

object RecipeListScreen {

    @Composable
    fun RecipeListScreen(viewModel: RecipesViewModel) {
        val meals by viewModel.meals.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn {
                items(meals) { meal ->
                    RecipeItem(meal = meal)
                }
            }
            LaunchedEffect(meals) {
                if (meals.isNotEmpty()) {
                    println("DEBUG: First meal image URL: ${meals.first().strMealThumb}")
                }
            }
        }
    }
}
package com.example.recipes.presentation.screen

import android.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.util.TableInfo
import coil.compose.AsyncImage
import com.example.recipes.data.remote.model.Meal
import com.example.recipes.presentation.component.RecipeItem
import com.example.recipes.presentation.component.RecipeItem.RecipeItem
import com.example.recipes.presentation.screen.recipeCard.RecipeCard
import com.example.recipes.presentation.viewmodel.RecipesViewModel

object RecipeListScreen {

    @Composable
    fun RecipeListScreen(
        viewModel: RecipesViewModel,
        onMealClick: (Meal) -> Unit = {},
        onFavoriteClick: () -> Unit = {}
    ) {
        val meals by viewModel.meals.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Рецепты") },
                    actions = {
                        IconButton(onClick = onFavoriteClick) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Избранное"
                            )
                        }
                    }
                )
            }
        )
        { paddingValues ->
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    items(meals) { meal ->
                        RecipeCard(
                            meal = meal,
                            onMealClick = onMealClick,
                            onToggleFavorite = { mealid ->
                                viewModel.toggleFavorite(mealid)
                            }
                        )
                    }
                }
            }
        }
    }
}
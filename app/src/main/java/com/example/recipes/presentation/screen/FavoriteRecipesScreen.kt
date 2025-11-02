package com.example.recipes.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.recipes.data.remote.model.Meal
import com.example.recipes.presentation.screen.recipeCard.RecipeCard
import com.example.recipes.presentation.viewmodel.RecipesViewModel
import dagger.Component

object FavoriteRecipesScreen {

    @Composable
    fun favoriteRecipesScreen(
       viewModel: RecipesViewModel,
       onMealClick: (Meal) -> Unit = {}
    ){
        val favoriteMeals by viewModel.favorite_meals.collectAsState()

        if(favoriteMeals.isEmpty()){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                )
                {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Нет избранных",
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                    Text (
                        text = "Нет избранных рецептов",
                        style = MaterialTheme.typography.h6,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
        else {
            LazyColumn {
                items(favoriteMeals) {
                    meal ->
                    RecipeCard(
                        meal = meal,
                        onMealClick = onMealClick,
                        onToggleFavorite = { mealId ->
                            viewModel.toggleFavorite(mealId)
                        }
                    )
                }
            }
        }
    }

}
package com.example.recipes.presentation.screen


import android.R.attr.contentDescription
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.example.recipes.data.remote.model.Meal
import com.example.recipes.presentation.viewmodel.RecipesViewModel
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.recipes.presentation.screen.recipeCard.RecipeCard
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryRecipesScreen(
    category: String,
    viewModel: RecipesViewModel,
    onBackClick: () -> Unit = {},
    onMealClick: (Meal) -> Unit = {}
) {

    val meals by viewModel.categoryMeals.collectAsState()

    LaunchedEffect(category) {
        viewModel.loadMealsByCategory(category)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Рецепты: $category") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (meals.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Рецепты не найдены")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(paddingValues))
            {
                items(meals) {
                    meal ->
                    RecipeCard(
                        meal = meal,
                        onMealClick = onMealClick,
                        onToggleFavorite = {
                            mealId ->
                            viewModel.toggleFavorite(mealId)
                        }
                    )
                }
            }
        }
    }
}
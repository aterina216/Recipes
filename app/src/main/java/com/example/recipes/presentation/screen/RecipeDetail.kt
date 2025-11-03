package com.example.recipes.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.recipes.data.remote.model.Meal
import com.example.recipes.presentation.viewmodel.RecipesViewModel

object RecipeDetail {

    @Composable
    fun RecipeDetailScreen(
        meal: Meal, // Принимаем готовый Meal объект, а не mealId
        onBackClick: () -> Unit = {},
        onToggleFavorite: (String) -> Unit
    ) {
        // Создаем локальное состояние для мгновенного обновления UI
        var currentMeal by remember { mutableStateOf(meal) }

        LaunchedEffect(meal) {
            currentMeal = meal
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Рецепт") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, "Назад")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            onToggleFavorite(currentMeal.idMeal)
                            // Мгновенно обновляем локальное состояние
                            currentMeal = currentMeal.copy(isFavorite = !currentMeal.isFavorite)
                        }) {
                            Icon(
                                imageVector = if (currentMeal.isFavorite) {
                                    Icons.Filled.Favorite
                                } else {
                                    Icons.Outlined.Favorite
                                },
                                contentDescription = "Избранное",
                                tint = if (currentMeal.isFavorite) Color.Red else Color.Gray
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = currentMeal.strMealThumb,
                    contentDescription = currentMeal.strMeal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = currentMeal.strMeal,
                            style = MaterialTheme.typography.h4,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(onClick = {
                            onToggleFavorite(currentMeal.idMeal)
                            currentMeal = currentMeal.copy(isFavorite = !currentMeal.isFavorite)
                        }) {
                            Icon(
                                imageVector = if (currentMeal.isFavorite) {
                                    Icons.Filled.Favorite
                                } else {
                                    Icons.Outlined.Favorite
                                },
                                contentDescription = null,
                                tint = if (currentMeal.isFavorite) Color.Red else Color.Gray,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Text(
                        text = "Категория: ${currentMeal.strCategory}",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    Text(
                        text = "Кухня: ${currentMeal.strArea}",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Text(
                        text = "Инструкции:",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                    )

                    Text(
                        text = currentMeal.strInstructions ?: "Инструкции не указаны",
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}
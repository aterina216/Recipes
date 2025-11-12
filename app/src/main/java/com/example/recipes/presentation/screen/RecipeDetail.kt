package com.example.recipes.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.ExtendedFloatingActionButton
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
        mealId: String,
        viewModel: RecipesViewModel,
        onBackClick: () -> Unit,
        onShareClick: (Meal) -> Unit
    ) {
        // Ищем рецепт в существующих данных
        var meal by remember(mealId) {
            mutableStateOf(viewModel.getMealById(mealId))
        }

        // Функция для переключения избранного
        val toggleFavorite = {
            meal?.let { currentMeal ->
                viewModel.toggleFavorite(currentMeal.idMeal)
                // Обновляем локальное состояние
                meal = currentMeal.copy(isFavorite = !currentMeal.isFavorite)
            }
        }

        // Если не нашли - показываем заглушку
        if (meal == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Рецепт не найден")
                    Button(onClick = onBackClick) {
                        Text("Назад")
                    }
                }
            }
            return
        }

        // Отображаем детали рецепта
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Рецепт") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, "Назад")
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
                // Картинка
                AsyncImage(
                    model = meal!!.strMealThumb,
                    contentDescription = meal!!.strMeal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )

                // Информация
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = meal!!.strMeal,
                            style = MaterialTheme.typography.h4,
                            modifier = Modifier.weight(1f)
                        )

                        // Дублирующая иконка избранного в основном контенте
                        IconButton(onClick = toggleFavorite as () -> Unit) {
                            Icon(
                                imageVector = if (meal!!.isFavorite) {
                                    Icons.Filled.Favorite
                                } else {
                                    Icons.Outlined.Favorite
                                },
                                contentDescription = "Избранное",
                                tint = if (meal!!.isFavorite) Color.Red else Color.Gray,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Категория: ${meal!!.strCategory}")
                    Text("Кухня: ${meal!!.strArea}")

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = { onShareClick(meal!!) }) {
                        Text("Поделиться рецептом")
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Инструкции:",
                        style = MaterialTheme.typography.h6
                    )

                    Text(
                        text = meal!!.strInstructions ?: "Инструкции не указаны",
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}
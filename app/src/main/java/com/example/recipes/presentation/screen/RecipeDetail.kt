package com.example.recipes.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.recipes.data.remote.model.Meal

object RecipeDetail {

    @Composable
    fun RecipeDetailScreen(
        meal: Meal,
        onBackClick: () -> Unit = {}
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Рецепт") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
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
                    model = meal.strMealThumb,
                    contentDescription = meal.strMeal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = meal.strMeal,
                        style = MaterialTheme.typography.h6
                    )

                    Text(
                        text = "Категория: ${meal.strCategory}",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Text(
                        text = "Кухня: ${meal.strArea}",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Text(
                        text = "Инструкции:",
                        style = MaterialTheme.typography.h3,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )

                    Text(
                        text = meal.strInstructions ?: "Инструкции не указаны",
                        style = MaterialTheme.typography.h6
                    )
                }
            }
        }
    }
}
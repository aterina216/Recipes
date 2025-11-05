package com.example.recipes.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import com.example.recipes.data.remote.model.Meal
import com.example.recipes.presentation.viewmodel.RecipesViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.room.util.query
import com.example.recipes.presentation.screen.recipeCard.RecipeCard

@Composable
fun SearchScreen(
    viewModel: RecipesViewModel,
    onMealClick: (Meal) -> Unit = {}
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    Column {
        TextField(
            value = searchQuery,
            onValueChange = { query ->
                viewModel.onSearchQueryChanged(query)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Поиск рецептов...") },
            leadingIcon = {
                Icon(Icons.Default.Search, "Поиск")
            },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(
                        onClick = { viewModel.cleanSearch() }
                    ) {
                        Icon(Icons.Default.Close, "Очистить")
                    }
                }
            },
            singleLine = true
        )
        when {
            isSearching && searchResults.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text("Ничего не найдено")
                }
            }

            searchQuery.isBlank() -> {
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center){
                    Column (horizontalAlignment = Alignment.CenterHorizontally)
                    {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Поиск",
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )
                        Text(
                            text = "Введите название рецепта",
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }
            else -> {
                LazyColumn {
                    items(searchResults) {
                        meal -> RecipeCard(
                            meal = meal,
                            onMealClick = onMealClick,
                            onToggleFavorite = {mealId ->
                                viewModel.toggleFavorite(mealId)
                            }
                        )
                    }
                }
            }
        }
    }
}
package com.example.recipes.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipes.data.remote.model.Category
import com.example.recipes.presentation.viewmodel.RecipesViewModel

@Composable
fun CategoriesScreen (
    viewModel: RecipesViewModel,
    onCategoryClick: (String) -> Unit = {}
){

    val categories by viewModel.categories.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCategories()
    }

    LazyColumn {
        items(categories) { category ->
            CategoryItem(
                category = category.strCategory,
                onClick = {onCategoryClick(category.strCategory)}
            )
        }
    }
}

@Composable
fun CategoryItem(category: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)  // Переносим onClick в модификатор
    ) {
        Text(
            text = category,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.h6
        )
    }
}
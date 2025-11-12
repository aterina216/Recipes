package com.example.recipes

import AppNavigation
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.recipes.data.remote.model.Meal
import com.example.recipes.data.repository.MealRepository
import com.example.recipes.presentation.navigation.BottomNavigation.BottomNavigationBar
import com.example.recipes.presentation.screen.CategoriesScreen
import com.example.recipes.presentation.screen.CategoryRecipesScreen
import com.example.recipes.presentation.screen.FavoriteRecipesScreen
import com.example.recipes.presentation.screen.MainScreen
import com.example.recipes.presentation.screen.RecipeDetail.RecipeDetailScreen
import com.example.recipes.presentation.screen.RecipeListScreen
import com.example.recipes.presentation.screen.RecipeListScreen.RecipeListScreen
import com.example.recipes.presentation.screen.SearchScreen
import com.example.recipes.presentation.viewmodel.RecipesViewModel
import com.example.recipes.ui.theme.RecipesTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: RecipesViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as RecipesApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        setContent {
            RecipesTheme {
                AppNavigation(viewModel, this)
            }
        }
    }

    fun shareRecipe(meal: Meal) {
        val shareText = buildShareText(meal)
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Поделиться рецептом"))
    }

    private fun buildShareText(meal: Meal): String {
        return """
            🍽️ ${meal.strMeal}
            
            Категория: ${meal.strCategory}
            Кухня: ${meal.strArea}
            
            ${
            meal.strInstructions?.take(200)?.let {
                if (it.length == 200) "$it..." else it
            } ?: "Описание рецепта отсутствует"
        }
            
            Приложение: Рецепты от ${packageName}
        """.trimIndent()
    }
}

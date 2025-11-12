
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.recipes.MainActivity
import com.example.recipes.presentation.screen.CategoryRecipesScreen
import com.example.recipes.presentation.screen.MainScreen
import com.example.recipes.presentation.screen.RecipeDetail.RecipeDetailScreen
import com.example.recipes.presentation.viewmodel.RecipesViewModel

@Composable
fun AppNavigation(viewModel: RecipesViewModel, activity: MainActivity) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(
                viewModel = viewModel,
                onMealClick = { meal ->
                    navController.navigate("detail/${meal.idMeal}")
                },
                onCategoryClick = { category ->
                    navController.navigate("category/$category")
                }
            )
        }

        composable("detail/{mealId}") { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId") ?: ""
            RecipeDetailScreen(
                mealId = mealId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onShareClick = { meal -> activity.shareRecipe(meal) }
            )
        }

        composable("category/{categoryName}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            CategoryRecipesScreen(
                category = categoryName,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onMealClick = { meal ->
                    navController.navigate("detail/${meal.idMeal}")
                }
            )
        }
    }
}
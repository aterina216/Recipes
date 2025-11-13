package com.example.recipes.presentation.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.recipes.R
import com.example.recipes.data.remote.model.Meal
import com.example.recipes.presentation.viewmodel.RecipesViewModel
import com.example.recipes.utils.DownLoad.downloadImage
import kotlinx.coroutines.launch


object RecipeDetail {

    @Composable
    fun RecipeDetailScreen(
        mealId: String,
        viewModel: RecipesViewModel,
        onBackClick: () -> Unit,
        onShareClick: (Meal) -> Unit
    ) {

        val context = LocalContext.current
        val scope = rememberCoroutineScope()


        // Ищем рецепт в существующих данных
        var meal by remember(mealId) {
            mutableStateOf(viewModel.getMealById(mealId))
        }

        val downloadCurrentImage = {
            meal?.let { currentMeal ->
                currentMeal.strMealThumb?.let { url ->
                    scope.launch {
                        // УБРАЛ scope из параметров
                        downloadImage(context, url, "recipe_${currentMeal.idMeal}")
                    }
                } ?: Toast.makeText(context, "Нет изображения для скачивания", Toast.LENGTH_SHORT).show()
            } ?: Toast.makeText(context, "Рецепт не найден", Toast.LENGTH_SHORT).show()
        }

        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                downloadCurrentImage()
            } else {
                Toast.makeText(context, "Нужно разрешение для сохранения изображений", Toast.LENGTH_LONG).show()
            }
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

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(


                        onClick = {
                            // Здесь будет вызов функции скачивания

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                downloadCurrentImage()
                            } else {
                                // Для старых версий проверяем разрешение
                                if (ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                                ) {
                                    downloadCurrentImage()
                                } else {
                                    permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                }
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_save_24),
                            contentDescription = "Скачать изображение"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Скачать изображение")
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
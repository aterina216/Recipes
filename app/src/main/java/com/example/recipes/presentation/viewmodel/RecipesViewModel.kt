package com.example.recipes.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipes.data.local.db.AppDatabase
import com.example.recipes.data.remote.RetrofitClient
import com.example.recipes.data.remote.model.Meal
import com.example.recipes.data.repository.MealRepository
import com.example.recipes.utils.MemoryLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MealRepository
    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> = _meals

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        // Инициализируем репозиторий с БД
        val database = AppDatabase.getDatabase(application)
        val mealDao = database.mealDao()
        val mealApi = RetrofitClient.mealApi

        repository = MealRepository(mealApi, mealDao)

        loadRandomMeals()
    }

    private fun loadRandomMeals() {
        viewModelScope.launch {
            MemoryLogger.logMemory("BEFORE_LOAD")

            _isLoading.value = true
            try {
                _meals.value = repository.getRandomMeals()
            } catch (e: Exception) {
                // Обработка ошибок
                Log.e("RecipesViewModel", "Error loading meals", e)
            }
            _isLoading.value = false

            MemoryLogger.logMemory("AFTER_LOAD")
        }
    }

    fun toggleFavorite(mealId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(mealId)
            // Обновляем список после изменения избранного
            loadRandomMeals()
        }
    }

    fun checkMemory() {
        MemoryLogger.logMemory("MANUAL_CHECK")
    }
}

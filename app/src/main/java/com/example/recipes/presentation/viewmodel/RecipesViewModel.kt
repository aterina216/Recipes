package com.example.recipes.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipes.data.local.db.AppDatabase
import com.example.recipes.data.remote.RetrofitClient
import com.example.recipes.data.remote.model.Meal
import com.example.recipes.data.repository.MealRepository
import com.example.recipes.utils.MemoryLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.compose
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecipesViewModel @Inject constructor(
    private val repository: MealRepository
) : ViewModel() {

    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> = _meals

    private val _favoriteMeals = MutableStateFlow<List<Meal>>(emptyList())
    val favorite_meals : StateFlow<List<Meal>> = _favoriteMeals

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _currentScreen = MutableStateFlow("recipes")
    val currentScreen: StateFlow<String> = _currentScreen

    init {
        loadRandomMeals()
        loadFavorites()
    }

    private fun loadRandomMeals() {
        viewModelScope.launch {
            MemoryLogger.logMemory("BEFORE_LOAD")
            _isLoading.value = true
            try {
                _meals.value = repository.getRandomMeals()
            } catch (e: Exception) {
                Log.e("RecipesViewModel", "Error loading meals", e)
            }
            _isLoading.value = false
            MemoryLogger.logMemory("AFTER_LOAD")
        }
    }

    fun toggleFavorite(mealId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(mealId)
            loadRandomMeals()
            loadFavorites()
        }
    }

    fun checkMemory() {
        MemoryLogger.logMemory("MANUAL_CHECK")
    }

    private fun loadFavorites(){
        viewModelScope.launch {
            repository.getFavorites().collect {
                favoriteList ->
                _favoriteMeals.value = favoriteList
            }
        }
    }

    private fun getFavorites(): Flow<List<Meal>>{
        return repository.getFavorites()
    }

    fun navigateTo(screen: String){
        _currentScreen.value = screen
    }
}
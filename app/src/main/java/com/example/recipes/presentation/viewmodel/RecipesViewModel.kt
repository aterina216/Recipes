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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.compose
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecipesViewModel @Inject constructor(
    private val repository: MealRepository
) : ViewModel() {

    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> = _meals

    private val _favoriteMeals = MutableStateFlow<List<Meal>>(emptyList())
    val favoriteMeals: StateFlow<List<Meal>> = _favoriteMeals

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _currentScreen = MutableStateFlow("recipes")
    val currentScreen: StateFlow<String> = _currentScreen

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Meal>>(emptyList())
    val searchResults: StateFlow<List<Meal>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    init {
        loadRandomMeals()
        loadFavorites()
    }

    private fun loadRandomMeals() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _meals.value = repository.getRandomMeals()
            } catch (e: Exception) {
                Log.e("RecipesViewModel", "Error loading meals", e)
            }
            _isLoading.value = false
        }
    }

    fun toggleFavorite(mealId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(mealId)
            // Просто перезагружаем оба списка
            loadRandomMeals()
            loadFavorites()
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            repository.getFavorites().collect { favorites ->
                _favoriteMeals.value = favorites
            }
        }
    }

    fun navigateTo(screen: String) {
        _currentScreen.value = screen
    }

    fun onSearchQueryChanged(query: String){
        _searchQuery.value = query
        _isSearching.value = query.isNotBlank()

        viewModelScope.launch {
           if(query.isNotBlank() && query.length > 2){
               _isSearching.value = true

               try {
                   val apiResults = repository.searchMealsFromApi(query)
                   _searchResults.value = apiResults
               }
               catch (e: Exception){
                   Log.e("RecipesViewModel", "API search error", e)
                   _searchResults.value = emptyList()
               }
               _isSearching.value = false
           }
            else {
                repository.searchMeals(query).collect {
                    localResults ->
                    _searchResults.value = localResults
                }
           }
        }
    }

    fun cleanSearch(){
        _searchQuery.value = ""
        _isSearching.value = false
        _searchResults.value = emptyList()
    }
}
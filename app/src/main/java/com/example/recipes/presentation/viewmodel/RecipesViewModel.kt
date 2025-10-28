package com.example.recipes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipes.data.remote.model.Meal
import com.example.recipes.data.repository.MealRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipesViewModel: ViewModel() {
    private val repository = MealRepository()

    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> = _meals

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            val isApiWorking = repository.testApi()
            println("API WORKING: $isApiWorking")
            if (isApiWorking) {
                loadRandomMeals()
            }
        }
    }

    private fun loadRandomMeals(){
        viewModelScope.launch {
            _isLoading.value = true
            _meals.value = repository.getRandomMeals()
            _isLoading.value = false
        }
    }
}
package com.example.recipes.data.repository

import com.example.recipes.data.remote.RetrofitClient
import com.example.recipes.data.remote.model.Meal

class MealRepository {

    private val mealApi = RetrofitClient.mealApi

    suspend fun getRandomMeals(): List<Meal>{
        val meals = mutableListOf<Meal>()

        repeat(10){
            try {
                val response = mealApi.getRandomMeal()
                response.meals?.firstOrNull()?.let {
                    meal ->
                    meals.add(meal)
                }
            }
            catch (e: Exception){

            }
        }
        return meals.distinctBy { it.idMeal }
    }

    suspend fun testApi(): Boolean {
        return try {
            val response = mealApi.getRandomMeal()
            println("TEST API: ${response.meals?.size} meals received")
            response.meals?.isNotEmpty() ?: false
        } catch (e: Exception) {
            println("TEST API ERROR: ${e.message}")
            false
        }
    }
}
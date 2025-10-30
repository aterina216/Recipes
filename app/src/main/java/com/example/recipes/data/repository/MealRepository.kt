package com.example.recipes.data.repository

import android.util.Log
import com.example.recipes.data.local.dao.Mealdao
import com.example.recipes.data.remote.RetrofitClient
import com.example.recipes.data.remote.api.MealApi
import com.example.recipes.data.remote.model.Meal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class MealRepository(
    private val mealApi: MealApi,
    private val mealDao: Mealdao
) {
    // Получаем рецепты
    suspend fun getRandomMeals(): List<Meal> {
        // Сначала из БД
        val localMeals = mealDao.getAllMeals().first()
        if (localMeals.isNotEmpty()) {
            return localMeals
        }

        // Если БД пуста - грузим из сети
        val networkMeals = loadMealsFromNetwork()
        mealDao.insertMeals(networkMeals)
        return networkMeals
    }

    private suspend fun loadMealsFromNetwork(): List<Meal> {
        val meals = mutableListOf<Meal>()

        repeat(10) {
            try {
                val response = mealApi.getRandomMeal()
                response.meals?.firstOrNull()?.let { meal ->
                    meals.add(meal)
                }
            } catch (e: Exception) {
                Log.e("MealRepository", "Error loading meal", e)
            }
        }

        return meals.distinctBy { it.idMeal }
    }

    // Избранное
    fun getFavorites(): Flow<List<Meal>> = mealDao.getFavoriteMeals()

    suspend fun toggleFavorite(mealId: String) {
        val currentMeal = mealDao.getAllMeals().first().find { it.idMeal == mealId }
        currentMeal?.let {
            mealDao.setFavorite(mealId, !it.isFavorite)
        }
    }

    // Поиск
    fun searchMeals(query: String): Flow<List<Meal>> {
        return if (query.isBlank()) {
            mealDao.getAllMeals()
        } else {
            mealDao.searchMeals(query)
        }
    }

    // Обновляем доступ
    suspend fun markAsAccessed(mealId: String) {
        mealDao.updateLastedAccessed(mealId)
    }
}
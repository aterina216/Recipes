package com.example.recipes.data.repository

import android.util.Log
import com.example.recipes.data.local.dao.Mealdao
import com.example.recipes.data.remote.RetrofitClient
import com.example.recipes.data.remote.api.MealApi
import com.example.recipes.data.remote.model.Category
import com.example.recipes.data.remote.model.Meal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlin.collections.emptyList

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
            flowOf(emptyList())
        } else {
            // Сначала ищем в локальной БД
            mealDao.searchMeals(query)
        }
    }

    // Обновляем доступ
    suspend fun markAsAccessed(mealId: String) {
        mealDao.updateLastedAccessed(mealId)
    }

    suspend fun getMealById(id: String): Meal? {
        return mealDao.getMealById(id)
    }

    suspend fun searchMealsFromApi(query: String): List<Meal> {
        return try {
            val response = mealApi.searchMeals(query)
            val meals = response.meals ?: emptyList()

            // Преобразуем nullable список в non-nullable
            val nonNullableMeals = meals.filterNotNull()

            mealDao.insertMeals(nonNullableMeals)
            nonNullableMeals
        } catch (e: Exception) {
            Log.e("MealRepository", "Error searching meals from API", e)
            emptyList()
        }
    }

    suspend fun getCategories(): List<Category> {
        return try {
            val response = mealApi.getCategories()
            response.meals
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getMealsByCategory(category: String): List<Meal> {
        return try {
            // 1. Получаем список ID рецептов категории
            val categoryResponse = mealApi.getMealsByCategory(category)

            // 2. Для каждого ID загружаем полный рецепт
            val fullMeals = mutableListOf<Meal>()
            categoryResponse.meals?.forEach { meal ->
                if (meal != null) {
                    val fullMeal = mealApi.getMealById(meal.idMeal)
                    fullMeal.meals?.firstOrNull()?.let { fullMeals.add(it) }
                }
            }

            fullMeals
        } catch (e: Exception) {
            emptyList()
        }
    }

}
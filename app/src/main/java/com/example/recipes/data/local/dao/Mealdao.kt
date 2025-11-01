package com.example.recipes.data.local.dao

import androidx.compose.ui.text.style.LineBreak
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipes.data.remote.model.Meal
import kotlinx.coroutines.flow.Flow

@Dao
interface Mealdao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: Meal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<Meal>)

    @Query("SELECT * FROM recipes ORDER BY last_accessed DESC")
    fun getAllMeals(): Flow<List<Meal>>

    @Query("SELECT * FROM recipes WHERE is_favorite = 1")
    fun getFavoriteMeals(): Flow<List<Meal>>

    @Query("SELECT * FROM recipes WHERE title LIKE '%' || :query || '%'")
    fun searchMeals(query: String): Flow<List<Meal>>

    @Query("UPDATE recipes SET is_favorite = :isFavorite WHERE idMeal = :id")
    suspend fun setFavorite(id: String, isFavorite: Boolean)

    @Query("UPDATE recipes SET last_accessed = :timestamp WHERE idMeal = :id")
    suspend fun updateLastedAccessed(id: String, timestamp: Long = System.currentTimeMillis())

    @Query("DELETE FROM recipes WHERE last_accessed < :timestamp AND is_favorite = 0")
    suspend fun cleanOldMeals(timestamp: Long = System.currentTimeMillis())

    @Query("SELECT * FROM recipes WHERE idMeal = :id LIMIT 1")
    suspend fun getMealById(id: String): Meal?

}
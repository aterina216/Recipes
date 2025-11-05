package com.example.recipes.data.remote.api

import retrofit2.http.Query
import androidx.room.RawQuery
import com.example.recipes.data.remote.model.MealResponse
import retrofit2.http.GET

interface MealApi {

    @GET("random.php")
    suspend fun getRandomMeal(): MealResponse

    @GET("search.php")
    suspend fun searchMeals(@Query("s") query: String): MealResponse
}
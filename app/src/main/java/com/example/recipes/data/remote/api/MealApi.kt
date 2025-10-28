package com.example.recipes.data.remote.api

import com.example.recipes.data.remote.model.MealResponse
import retrofit2.http.GET

interface MealApi {

    @GET("random.php")
    suspend fun getRandomMeal(): MealResponse
}
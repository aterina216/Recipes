package com.example.recipes.data.remote.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Meal (
    @PrimaryKey
    val idMeal: String,

    @ColumnInfo(name = "title")
    val strMeal: String,

    @ColumnInfo(name = "category")
    val strCategory: String,

    @ColumnInfo(name = "cuisine")
    val strArea: String,

    @ColumnInfo(name = "image_url")
    val strMealThumb: String?,

    @ColumnInfo(name = "instructions")
    val strInstructions: String?,

    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean = false,

    @ColumnInfo(name = "last_accessed")
    val lastAccessed: Long = System.currentTimeMillis()


)
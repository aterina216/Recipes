package com.example.recipes.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.recipes.data.local.dao.Mealdao
import com.example.recipes.data.local.db.AppDatabase
import com.example.recipes.data.remote.api.MealApi
import com.example.recipes.data.repository.MealRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "recipes_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMealDao(database: AppDatabase): Mealdao {
        return database.mealDao()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMealApi(retrofit: Retrofit): MealApi {
        return retrofit.create(MealApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMealRepository(
        mealApi: MealApi,
        mealDao: Mealdao
    ): MealRepository {
        return MealRepository(mealApi, mealDao)
    }
}
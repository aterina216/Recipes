package com.example.recipes.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.recipes.data.local.dao.Mealdao
import com.example.recipes.data.remote.model.Meal


@Database(
    entities = [Meal::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun mealDao(): Mealdao

    companion object{

        @Volatile
        private var INSTANSE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            return INSTANSE ?: synchronized(this){
                val instanse = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "recipes_db"
                )
                    .build()
                INSTANSE = instanse
                instanse
            }
        }
    }
}
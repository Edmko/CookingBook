package com.example.cookingbook.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cookingbook.db.dao.RecipeDao

@Database(entities = [DatabaseRecipe::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val recipeDao: RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "recipeDB"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }
}
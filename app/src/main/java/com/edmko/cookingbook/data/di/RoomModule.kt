package com.edmko.cookingbook.data.di

import android.content.Context
import com.edmko.cookingbook.data.AppDatabase
import com.edmko.cookingbook.data.dao.RecipeDao
import com.edmko.cookingbook.repository.RecipesRepository
import com.edmko.cookingbook.repository.RecipesRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule(val context: Context) {

    @Singleton
    @Provides
    fun provideDatabase(): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideRecipeDao(database: AppDatabase): RecipeDao {
        return database.recipeDao
    }

    @Singleton
    @Provides
    fun provideRecipeRepository(recipeDao: RecipeDao): RecipesRepository {
        return RecipesRepositoryImpl(recipeDao)
    }
}
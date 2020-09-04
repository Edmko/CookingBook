package com.edmko.cookingbook.repository

import androidx.lifecycle.LiveData
import com.edmko.cookingbook.data.Result
import com.edmko.cookingbook.models.Recipe

interface RecipesRepository {
    suspend fun saveRecipe(recipe: Recipe)
    suspend fun deleteRecipe(recipe: Recipe)
    suspend fun getRecipeById(recipeId: String): Result<Recipe>
    fun observeRecipeById(recipeId: String): LiveData<Recipe>
}
package com.edmko.cookingbook.repository

import androidx.lifecycle.LiveData
import com.edmko.cookingbook.data.Result
import com.edmko.cookingbook.models.Recipe

interface RecipesRepository {
    val recipes: LiveData<List<Recipe>>
    suspend fun saveRecipe(recipe: Recipe)
    suspend fun deleteRecipe(recipe: Recipe)
    suspend fun getRecipeById(recipeId: String): Recipe?
    suspend fun updateRecipe(recipe: Recipe)
    fun observeRecipeById(recipeId: String): LiveData<Recipe>
    suspend fun deleteRecipeById(recipeId: String)
}
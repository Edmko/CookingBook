package com.example.cookingbook.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.cookingbook.CookingApplication
import com.example.cookingbook.Result
import com.example.cookingbook.db.DatabaseRecipe
import com.example.cookingbook.db.asDomainRecipe
import com.example.cookingbook.models.Recipe
import com.example.cookingbook.models.asDatabaseRecipe
import kotlinx.coroutines.*
import java.lang.Error
import java.lang.Exception

class AppRepository {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val dataBase = CookingApplication.appDatabase.recipeDao

    val recipes: LiveData<List<Recipe>> = Transformations.map(dataBase.getAllRecipes()) {
        it.asDomainRecipe()
    }

    fun saveRecipe(recipe: Recipe) {
        GlobalScope.launch {
            dataBase.insertRecipe(recipe.asDatabaseRecipe())
        }
    }

    fun deleteRecipe(recipe: Recipe) {
        GlobalScope.launch {
            dataBase.deleteRecipe(recipe.asDatabaseRecipe())
        }
    }

    suspend fun getRecipeById(recipeId: String): Result<Recipe> =
        withContext(ioDispatcher) {
            try {
                val recipe = dataBase.getRecipeById(recipeId)
                if (recipe != null) {
                    return@withContext Result.Success(recipe.asDomainRecipe())
                } else {
                    return@withContext Result.Error(Exception("Recipe not found"))
                }
            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }
    fun observeRecipeById(recipeId: String) : LiveData<Recipe> = Transformations.map(dataBase.observeRecipeById(recipeId)){
        it.asDomainRecipe()
    }
}
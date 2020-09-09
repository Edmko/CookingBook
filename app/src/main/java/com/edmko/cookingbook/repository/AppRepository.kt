package com.edmko.cookingbook.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.edmko.cookingbook.CookingApplication
import com.edmko.cookingbook.data.Result
import com.edmko.cookingbook.data.asDomainRecipe
import com.edmko.cookingbook.models.Recipe
import com.edmko.cookingbook.models.asDatabaseRecipe
import kotlinx.coroutines.*
import java.lang.Exception

class AppRepository : RecipesRepository{

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val localDataSource = CookingApplication.appDatabase.recipeDao

    val recipes: LiveData<List<Recipe>> = Transformations.map(localDataSource.observeAllRecipes()) {
        it.asDomainRecipe()
    }

    override suspend fun saveRecipe(recipe: Recipe) = withContext(ioDispatcher) {
        localDataSource.insertRecipe(recipe.asDatabaseRecipe())
        }


    override suspend fun deleteRecipe(recipe: Recipe) =
        withContext(ioDispatcher) {
          localDataSource.deleteRecipe(recipe.asDatabaseRecipe())
            }

    override suspend fun updateRecipe(recipe: Recipe) = withContext(ioDispatcher){
        localDataSource.updateRecipe(recipe.asDatabaseRecipe())
    }

    override suspend fun getRecipeById(recipeId: String): Result<Recipe> =
        withContext(ioDispatcher) {
            try {
                val recipe = localDataSource.getRecipeById(recipeId)
                if (recipe != null) {
                    return@withContext Result.Success(recipe.asDomainRecipe())
                } else {
                    return@withContext Result.Error(Exception("Recipe not found"))
                }
            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }

    override fun observeRecipeById(recipeId: String): LiveData<Recipe> =
        Transformations.map(localDataSource.observeRecipeById(recipeId)) {
            it.asDomainRecipe()
        }
}
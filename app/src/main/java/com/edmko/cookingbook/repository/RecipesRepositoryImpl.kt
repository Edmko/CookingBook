package com.edmko.cookingbook.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.edmko.cookingbook.data.Result
import com.edmko.cookingbook.data.asDomainRecipe
import com.edmko.cookingbook.data.dao.RecipeDao
import com.edmko.cookingbook.models.Recipe
import com.edmko.cookingbook.models.asDatabaseRecipe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipesRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao
) : RecipesRepository {

    override val recipes: LiveData<List<Recipe>> = Transformations.map(recipeDao.observeAllRecipes()) {
        it.asDomainRecipe()
    }

    override suspend fun saveRecipe(recipe: Recipe) {
        recipeDao.insertRecipe(recipe.asDatabaseRecipe())
    }


    override suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.deleteRecipe(recipe.asDatabaseRecipe())
    }

    override suspend fun updateRecipe(recipe: Recipe) {
        recipeDao.updateRecipe(recipe.asDatabaseRecipe())
    }

    override suspend fun getRecipeById(recipeId: String): Recipe? = recipeDao.getRecipeById(recipeId)?.asDomainRecipe()

    override fun observeRecipeById(recipeId: String): LiveData<Recipe> =
        Transformations.map(recipeDao.observeRecipeById(recipeId)) {
            it.asDomainRecipe()
        }
}
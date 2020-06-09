package com.example.cookingbook.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.cookingbook.CookingApplication
import com.example.cookingbook.db.DatabaseRecipe
import com.example.cookingbook.db.asDomainRecipe
import com.example.cookingbook.models.Recipe
import com.example.cookingbook.models.asDatabaseRecipe
import kotlinx.coroutines.*

class AppRepository {

    private val dataBase = CookingApplication.appDatabase.recipeDao

    val recipes: LiveData<List<Recipe>> = Transformations.map(dataBase.getAllRecipes()) {
        it.asDomainRecipe()
    }

    fun saveRecipe(recipe: DatabaseRecipe) {
        GlobalScope.launch {
            dataBase.insertRecipe(recipe)
        }
    }

    fun deleteRecipe(recipe: Recipe) {
        GlobalScope.launch {
            dataBase.deleteRecipe(recipe.asDatabaseRecipe())
        }
    }

    fun getRecipeById(id: String): LiveData<Recipe> = Transformations.map(dataBase.getRecipeById(id)) {
        it.asDomainRecipe()
    }
    fun getRecipesByTag(tag: String) : LiveData<List<Recipe>> = Transformations.map(dataBase.getRecipesByTag(tag)){
        it.asDomainRecipe()
    }
}
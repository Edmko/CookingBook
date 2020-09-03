package com.edmko.cookingbook.ui.recipeFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.edmko.cookingbook.models.Recipe
import com.edmko.cookingbook.repository.AppRepository

class RecipeViewModel : ViewModel() {
    var repository: AppRepository = AppRepository()
    lateinit var recipe: LiveData<Recipe>
    var currentSecondYouTube = 0F

    fun getRecipe(idArgs: String) {
       recipe = repository.observeRecipeById(idArgs)
    }
}

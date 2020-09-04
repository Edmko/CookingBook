package com.edmko.cookingbook.ui.recipeFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edmko.cookingbook.models.Recipe
import com.edmko.cookingbook.repository.AppRepository
import kotlinx.coroutines.launch

class RecipeViewModel(private val appRepository: AppRepository) : ViewModel() {
    lateinit var recipe: LiveData<Recipe>

    var currentSecondYouTube = 0F

    fun getRecipe(recipeId: String) {
       recipe = appRepository.observeRecipeById(recipeId)
    }

    fun deleteRecipe(recipe: Recipe){
        viewModelScope.launch { appRepository.deleteRecipe(recipe) }
    }
}

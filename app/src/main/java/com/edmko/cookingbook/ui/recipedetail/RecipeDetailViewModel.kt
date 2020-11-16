package com.edmko.cookingbook.ui.recipedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.edmko.cookingbook.base.BaseViewModel
import com.edmko.cookingbook.models.Recipe
import com.edmko.cookingbook.repository.RecipesRepositoryImpl
import com.edmko.cookingbook.ui.recipedetail.domain.RecipeDetailModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecipeDetailViewModel @Inject constructor(val model: RecipeDetailModel) : BaseViewModel() {
    lateinit var recipe: LiveData<Recipe>

    var currentSecondYouTube = 0F

    fun getRecipe(recipeId: String) {
       recipe = model.observeRecipeById(recipeId)
    }

    fun deleteRecipe(recipe: Recipe){
        viewModelScope.launch { model.deleteRecipe(recipe) }
    }
}

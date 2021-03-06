package com.edmko.cookingbook.ui.recipes

import androidx.lifecycle.MutableLiveData
import com.edmko.cookingbook.base.BaseViewModel
import com.edmko.cookingbook.models.Recipe
import com.edmko.cookingbook.ui.recipes.domain.RecipesModel
import javax.inject.Inject

class RecipesViewModel @Inject constructor(
    private val model: RecipesModel
) : BaseViewModel(){
    var recipeList = model.recipes
}

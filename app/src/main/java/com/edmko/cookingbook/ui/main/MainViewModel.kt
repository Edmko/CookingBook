package com.edmko.cookingbook.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.edmko.cookingbook.models.Recipe
import com.edmko.cookingbook.repository.AppRepository

class MainViewModel : ViewModel() {
    var repository: AppRepository = AppRepository()
    var recipeList: LiveData<List<Recipe>> = repository.recipes
    var recipeId = ""
    var check_ScrollingUp = false

}

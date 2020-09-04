package com.edmko.cookingbook.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.edmko.cookingbook.models.Recipe
import com.edmko.cookingbook.repository.AppRepository

class MainViewModel (private val appRepository: AppRepository) : ViewModel(){
    var recipeList: LiveData<List<Recipe>> = appRepository.recipes
    var recipeId = ""

}

package com.example.cookingbook.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.cookingbook.models.Recipe
import com.example.cookingbook.repository.AppRepository

class MainViewModel : ViewModel() {
    var repository: AppRepository = AppRepository()
    var recipeList: LiveData<List<Recipe>> = repository.recipes
    var recipeId = ""

}

package com.example.cookingbook.ui.recipeFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookingbook.Result
import com.example.cookingbook.db.DatabaseRecipe
import com.example.cookingbook.db.TypeConverter
import com.example.cookingbook.models.Recipe
import com.example.cookingbook.repository.AppRepository
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {
    var repository: AppRepository = AppRepository()
    lateinit var recipe: LiveData<Recipe>
    var currentSecondYouTube = 0F

    fun getRecipe(idArgs: String) {
       recipe = repository.observeRecipeById(idArgs)
    }
}

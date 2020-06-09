package com.example.cookingbook.ui.addRecipe

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.cookingbook.models.Recipe
import com.example.cookingbook.models.asDatabaseRecipe
import com.example.cookingbook.repository.AppRepository
import java.util.*

class AddRecipeViewModel() : ViewModel() {
    var repository: AppRepository = AppRepository()
    private var _recipe = MutableLiveData<Recipe>()

    val recipe: LiveData<Recipe>
        get() = _recipe
    private var tempRecipe = Recipe(
        "",
        "", "", "", "", mutableListOf(), mutableListOf()
    )

    fun getRecipe(idArgs: String, owner: LifecycleOwner) {
        if (idArgs.isNotBlank()) {
            repository.getRecipeById(idArgs).observe(owner, Observer {
                _recipe.postValue(it)
                tempRecipe = it
            })
        }
    }

    fun addIngredientToList(ingred: Pair<String, String>) {
        tempRecipe.ingredients.add(ingred)
        updateRecipe()
    }

    fun addTagToList(tag: String) {
        tempRecipe.tags.add(tag)
        updateRecipe()
    }

    fun removeTagFromList(index: Int) {
        tempRecipe.tags.removeAt(index)
        updateRecipe()
    }

    fun removeIngredientFromList(index: Int) {
        tempRecipe.ingredients.removeAt(index)
        updateRecipe()
    }

    fun updateImage(url: String) {
        tempRecipe.image = url
        updateRecipe()
    }

    private fun updateRecipe() {
        _recipe.postValue(tempRecipe)
    }

    fun saveRecipe(name: String, author: String, notes: String) {
        tempRecipe.name = name
        tempRecipe.author = author
        tempRecipe.notes = notes
        if (tempRecipe.id.isEmpty())tempRecipe.id = UUID.randomUUID().toString()
        repository.saveRecipe(tempRecipe.asDatabaseRecipe())
    }

    fun checkTemp(): Boolean {
        return if (
            tempRecipe.id.isEmpty() &&
            tempRecipe.author.isEmpty() &&
            tempRecipe.image.isEmpty() &&
            tempRecipe.name.isEmpty() &&
            tempRecipe.ingredients.isEmpty() &&
            tempRecipe.notes.isEmpty() &&
            tempRecipe.tags.isEmpty()
        ) true else {
            _recipe.postValue(tempRecipe)
            false
        }
    }

}

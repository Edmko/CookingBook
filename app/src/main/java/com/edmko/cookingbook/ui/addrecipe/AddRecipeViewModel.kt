package com.edmko.cookingbook.ui.addrecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.edmko.cookingbook.base.BaseViewModel
import com.edmko.cookingbook.models.Recipe
import com.edmko.cookingbook.repository.RecipesRepositoryImpl
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class AddRecipeViewModel @Inject constructor(val model: RecipesRepositoryImpl) : BaseViewModel() {
    private var _recipe = MutableLiveData<Recipe>()
    val recipe: LiveData<Recipe> = _recipe

    val name = MutableLiveData<String>()

    val author = MutableLiveData<String>()

    val notes = MutableLiveData<String>()

    val link = MutableLiveData<String>()

    private val _image = MutableLiveData<String>()
    val image: LiveData<String> = _image

    private val _tags = MutableLiveData<MutableList<String>>()
    val tags: LiveData<MutableList<String>> = _tags

    private val _ingredients = MutableLiveData<MutableList<Pair<String, String>>>()
    val ingredients: LiveData<MutableList<Pair<String, String>>> = _ingredients


    private val _dataLoading = MutableLiveData<Boolean>()

    private var tempIngredientsList = mutableListOf<Pair<String, String>>()
    private var tempTagList = mutableListOf<String>()

    private var recipeId: String? = null

    private var isNewRecipe: Boolean = false

    private var isDataLoaded = false

    fun getRecipe(recipeId: String?) {
        if (_dataLoading.value == true) {
            return
        }
        this.recipeId = recipeId
        if (recipeId == null) {
            isNewRecipe = true
            return
        }
        if (isDataLoaded) {
            return
        }

        isNewRecipe = false
        _dataLoading.value = true

//        viewModelScope.launch {
//            recipesRepository.getRecipeById(recipeId).let { result ->
//                if (result is Result.Success) {
//                    onRecipeLoaded(result.data)
//                } else {
//                    onDataNotAvailable()
//                }
//
//
//            }
//        }
    }

    private fun onDataNotAvailable() {
        _dataLoading.value = false
    }

    private fun onRecipeLoaded(recipe: Recipe) {
        name.value = recipe.name
        author.value = recipe.author
        link.value = recipe.link
        _tags.value = recipe.tags
        _image.value = recipe.image

        tempTagList = recipe.tags
        notes.value = recipe.notes

        _ingredients.value = recipe.ingredients
        tempIngredientsList = recipe.ingredients

        _dataLoading.value = false
        isDataLoaded = true
    }

    fun addIngredientToList(ingred: Pair<String, String>) {
            tempIngredientsList.add(ingred)
        _ingredients.postValue(tempIngredientsList)
    }

    fun removeIngredientFromList(index: Int) {
        tempIngredientsList.removeAt(index)
        _ingredients.postValue(tempIngredientsList)
    }

    fun addTagToList(tag: String) {
        if (tag.isBlank()) {
            return
        } else
            tempTagList.add(tag)
        _tags.postValue(tempTagList)
    }

    fun removeTagFromList(index: Int) {
        tempTagList.removeAt(index)
        _tags.postValue(tempTagList)
    }


    fun updateImage(url: String) {
        _image.postValue(url)
    }

    fun createRecipe(): String {

        val currentName = name.value ?: ""
        val currentAuthor = author.value ?: ""
        val currentNotes = notes.value ?: ""
        val currentTags = tags.value ?: mutableListOf()
        val currentIngredients = ingredients.value ?: mutableListOf()
        val currentLink = link.value ?: ""
        val currentImage = _image.value ?: ""

        var currentRecipeId = recipeId
        if (recipeId == null) {

            currentRecipeId = UUID.randomUUID().toString()
        }
        val recipe = Recipe(
            currentRecipeId!!,
            author = currentAuthor,
            name = currentName,
            image = currentImage,
            tags = currentTags,
            ingredients = currentIngredients,
            link = currentLink.removePrefix("https://youtu.be/"),
            notes = currentNotes
        )
        saveRecipe(recipe)
        return currentRecipeId
    }

    private fun saveRecipe(recipe: Recipe) = viewModelScope.launch {
        if (isNewRecipe)  {model.saveRecipe(recipe)}
        else model.updateRecipe(recipe)
    }


}

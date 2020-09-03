package com.edmko.cookingbook.ui.addRecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edmko.cookingbook.Result
import com.edmko.cookingbook.models.Recipe
import com.edmko.cookingbook.repository.AppRepository
import kotlinx.coroutines.launch
import java.util.*

class AddRecipeViewModel() : ViewModel() {
    var repository: AppRepository = AppRepository()
    private var _recipe = MutableLiveData<Recipe>()
    val recipe: LiveData<Recipe> = _recipe



    val name = MutableLiveData<String>()

    val author = MutableLiveData<String>()

    val notes = MutableLiveData<String>()

    val link = MutableLiveData<String>()

    private val _image = MutableLiveData<String>()
    val image : LiveData<String> = _image

    private val _tags = MutableLiveData<MutableList<String>>()
    val tags: LiveData<MutableList<String>> = _tags

    private val _ingredients = MutableLiveData<MutableList<Pair<String, String>>>()
    val ingredients: LiveData<MutableList<Pair<String, String>>> = _ingredients



    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private var tempIngredientsList = mutableListOf<Pair<String, String>>()
    private var tempTagList = mutableListOf<String>()

    private var recipeId: String? = null

    private var isNewRecipe: Boolean = false

    private var isDataLoaded = false

    private val TAG = "myLogs"
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

                viewModelScope.launch {
                    repository.getRecipeById(recipeId).let { result ->
                        if (result is Result.Success) {
                            onRecipeLoaded(result.data)
                        } else {
                            onDataNotAvailable()
                        }



            }
        }
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

    fun saveRecipe(): String {

        val currentName = name.value?: ""
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
            link = currentLink,
            notes = currentNotes
        )
        createRecipe(recipe)
        return currentRecipeId
    }

    private fun createRecipe(newRecipe: Recipe) {
        repository.saveRecipe(newRecipe)

    }

}

package com.edmko.cookingbook.ui.addrecipe.domain

import com.edmko.cookingbook.repository.RecipesRepository
import com.edmko.cookingbook.ui.recipes.domain.RecipesModel
import javax.inject.Inject

class AddRecipeModelImpl @Inject constructor(
    private val getRepository: RecipesRepository
) : AddRecipeModel,
    RecipesRepository by getRepository
package com.edmko.cookingbook.ui.recipedetail.domain

import com.edmko.cookingbook.repository.RecipesRepository
import javax.inject.Inject

class RecipeDetailModelImpl @Inject constructor(
    private val getRepository: RecipesRepository
) : RecipeDetailModel,
    RecipesRepository by getRepository
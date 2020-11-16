package com.edmko.cookingbook.ui.recipes.domain

import com.edmko.cookingbook.repository.RecipesRepository
import javax.inject.Inject

class RecipesModelImpl @Inject constructor(
    private val getRepository: RecipesRepository
) : RecipesModel,
        RecipesRepository by getRepository
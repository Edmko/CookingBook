package com.edmko.cookingbook.ui.recipedetail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.edmko.cookingbook.base.ViewModelFactory
import com.edmko.cookingbook.base.di.ViewModelKey
import com.edmko.cookingbook.ui.recipedetail.RecipeDetailViewModel
import com.edmko.cookingbook.ui.recipedetail.domain.RecipeDetailModel
import com.edmko.cookingbook.ui.recipedetail.domain.RecipeDetailModelImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface RecipeDetailModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RecipeDetailViewModel::class)
    fun bindViewModel(model: RecipeDetailViewModel): ViewModel

    @Binds
    fun bindModel(model: RecipeDetailModelImpl): RecipeDetailModel
}
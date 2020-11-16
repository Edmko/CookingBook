package com.edmko.cookingbook.ui.recipes.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.edmko.cookingbook.base.ViewModelFactory
import com.edmko.cookingbook.base.di.ViewModelKey
import com.edmko.cookingbook.ui.recipes.RecipesViewModel
import com.edmko.cookingbook.ui.recipes.domain.RecipesModel
import com.edmko.cookingbook.ui.recipes.domain.RecipesModelImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface RecipesModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RecipesViewModel::class)
    fun bindViewModel(viewModel: RecipesViewModel): ViewModel

    @Binds
    fun bindModel(model: RecipesModelImpl): RecipesModel
}
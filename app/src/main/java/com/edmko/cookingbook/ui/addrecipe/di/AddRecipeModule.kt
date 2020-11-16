package com.edmko.cookingbook.ui.addrecipe.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.edmko.cookingbook.base.ViewModelFactory
import com.edmko.cookingbook.base.di.ViewModelKey
import com.edmko.cookingbook.ui.addrecipe.AddRecipeViewModel
import com.edmko.cookingbook.ui.addrecipe.domain.AddRecipeModel
import com.edmko.cookingbook.ui.addrecipe.domain.AddRecipeModelImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AddRecipeModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AddRecipeViewModel::class)
    fun bindViewModel(model: AddRecipeViewModel): ViewModel

    @Binds
    fun bindModel(model: AddRecipeModelImpl): AddRecipeModel
}
package com.edmko.cookingbook.ui.addrecipe.di

import com.edmko.cookingbook.coredi.ApplicationProvider
import com.edmko.cookingbook.ui.addrecipe.AddRecipeFragment
import dagger.Component

@Component(
    dependencies = [ApplicationProvider::class],
    modules = [AddRecipeModule::class]
)
interface AddRecipeComponent {
    fun inject(fragment: AddRecipeFragment)

    companion object {
        fun build(applicationProvider: ApplicationProvider): AddRecipeComponent{
            return DaggerAddRecipeComponent.builder()
                .applicationProvider(applicationProvider)
                .build()
        }
    }
}
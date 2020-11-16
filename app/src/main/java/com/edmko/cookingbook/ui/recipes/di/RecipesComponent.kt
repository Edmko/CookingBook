package com.edmko.cookingbook.ui.recipes.di

import com.edmko.cookingbook.coredi.ApplicationProvider
import com.edmko.cookingbook.ui.recipes.RecipesFragment
import dagger.Component
import javax.inject.Singleton

@Component(
    dependencies = [ApplicationProvider::class],
    modules = [RecipesModule::class]
)
@Singleton
interface RecipesComponent {
    fun inject(fragment: RecipesFragment)

    companion object {
        fun build(applicationProvider: ApplicationProvider): RecipesComponent {
            return DaggerRecipesComponent.builder()
                .applicationProvider(applicationProvider)
                .build()
        }
    }
}
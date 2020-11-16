package com.edmko.cookingbook.ui.recipedetail.di

import com.edmko.cookingbook.coredi.ApplicationProvider
import com.edmko.cookingbook.ui.recipedetail.RecipeDetailFragment
import dagger.Component

@Component(
    dependencies = [ApplicationProvider::class],
    modules = [RecipeDetailModule::class]
)
interface RecipeDetailComponent {
    fun inject(fragment: RecipeDetailFragment)

    companion object{
        fun build(applicationProvider: ApplicationProvider): RecipeDetailComponent{
            return DaggerRecipeDetailComponent.builder()
                .applicationProvider(applicationProvider)
                .build()
        }
    }
}
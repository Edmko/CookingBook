package com.edmko.cookingbook.repository.di

import com.edmko.cookingbook.repository.RecipesRepository
import com.edmko.cookingbook.repository.RecipesRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryModule {

    @Singleton
    @Binds
    fun provideRecipesRepository(impl: RecipesRepositoryImpl): RecipesRepository
}
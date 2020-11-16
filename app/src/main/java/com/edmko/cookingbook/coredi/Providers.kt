package com.edmko.cookingbook.coredi

import android.content.Context
import com.edmko.cookingbook.data.dao.RecipeDao
import com.edmko.cookingbook.repository.RecipesRepository
import kotlinx.coroutines.CoroutineDispatcher

interface ApplicationProvider:
        RoomProvider,
        AndroidProvider,
        UseCaseProvider,
        DispatchersProvider,
        RepositoryProvider

interface AndroidProvider {
    fun provideContext(): Context
}

interface RoomProvider{
    fun provideRecipeDao(): RecipeDao
}

interface UseCaseProvider{

}

interface RepositoryProvider{
    fun provideRecipesRepository(): RecipesRepository
}
interface DispatchersProvider {
    fun provideCoroutineDispatchersProvider(): CoroutineDispatchersProvider
}

interface CoroutineDispatchersProvider {

    val uiDispatcher: CoroutineDispatcher
    val calculationsDispatcher: CoroutineDispatcher
    val ioDispatcher: CoroutineDispatcher
}
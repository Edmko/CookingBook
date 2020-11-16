package com.edmko.cookingbook.application.di

import com.edmko.cookingbook.coredi.CoroutineDispatchersProvider
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class CoroutineModule {

    @Singleton
    @Provides
    fun provideCoroutineDispatchersProvider(): CoroutineDispatchersProvider {
        val provider = object : CoroutineDispatchersProvider {
            override val uiDispatcher: CoroutineDispatcher
                get() = Dispatchers.Main
            override val calculationsDispatcher: CoroutineDispatcher
                get() = Dispatchers.Default
            override val ioDispatcher: CoroutineDispatcher
                get() = Dispatchers.IO
        }
        return provider
    }
}
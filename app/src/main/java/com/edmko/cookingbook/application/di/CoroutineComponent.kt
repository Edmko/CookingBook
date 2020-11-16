package com.edmko.cookingbook.application.di

import com.edmko.cookingbook.coredi.DispatchersProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CoroutineModule::class])
interface CoroutineComponent : DispatchersProvider {

    companion object {

        fun build(): DispatchersProvider =
            DaggerCoroutineComponent.builder()
                .build()
    }
}
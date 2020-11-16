package com.edmko.cookingbook.repository.di

import com.edmko.cookingbook.coredi.RepositoryProvider
import com.edmko.cookingbook.coredi.RoomProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [RoomProvider::class],
    modules = [RepositoryModule::class]
)
interface RepositoryComponent : RepositoryProvider {

    companion object {
        fun build(roomProvider: RoomProvider): RepositoryComponent {
            return DaggerRepositoryComponent.builder()
                .roomProvider(roomProvider)
                .build()
        }
    }
}
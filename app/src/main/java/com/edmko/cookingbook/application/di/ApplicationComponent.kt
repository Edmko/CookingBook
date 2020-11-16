package com.edmko.cookingbook.application.di

import android.content.Context
import com.edmko.cookingbook.coredi.*
import com.edmko.cookingbook.data.di.RoomComponent
import com.edmko.cookingbook.repository.di.RepositoryComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [AndroidProvider::class,
        RoomProvider::class,
        DispatchersProvider::class,
        RepositoryProvider::class]
)
interface ApplicationComponent : ApplicationProvider {

    companion object {
        fun build(context: Context): ApplicationComponent {
            val androidProvider = AndroidComponent.build(context)
            val roomProvider = RoomComponent.build(context)
            val repositoryProvider = RepositoryComponent.build(roomProvider)
            val dispatchersProvider = CoroutineComponent.build()
            return DaggerApplicationComponent.builder()
                .androidProvider(androidProvider)
                .repositoryProvider(repositoryProvider)
                .roomProvider(roomProvider)
                .dispatchersProvider(dispatchersProvider)
                .build()
        }
    }
}
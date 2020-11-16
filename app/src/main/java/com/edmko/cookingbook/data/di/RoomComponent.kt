package com.edmko.cookingbook.data.di

import android.content.Context
import com.edmko.cookingbook.coredi.RoomProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [RoomModule::class]
)
interface RoomComponent : RoomProvider {

    companion object {
        fun build(context: Context): RoomComponent{
            return DaggerRoomComponent.builder()
                .roomModule(RoomModule(context))
                .build()
        }
    }
}
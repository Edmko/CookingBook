package com.edmko.cookingbook.application.di

import android.content.Context
import com.edmko.cookingbook.coredi.AndroidProvider
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface AndroidComponent : AndroidProvider {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AndroidComponent
    }

    companion object {

        fun build(context: Context): AndroidProvider {
            return DaggerAndroidComponent.builder()
                .context(context)
                .build()
        }
    }
}
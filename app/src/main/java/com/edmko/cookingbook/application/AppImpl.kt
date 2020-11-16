package com.edmko.cookingbook.application

import android.app.Application
import com.edmko.cookingbook.application.di.ApplicationComponent
import com.edmko.cookingbook.coredi.App
import com.edmko.cookingbook.coredi.ApplicationProvider

class AppImpl: Application(), App {

    private val applicationProvider by lazy { ApplicationComponent.build(this) }
    override fun getApplicationProvider(): ApplicationProvider = applicationProvider
}
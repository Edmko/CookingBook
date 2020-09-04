package com.edmko.cookingbook

import android.app.Application
import android.content.Context
import com.edmko.cookingbook.data.AppDatabase
import com.edmko.cookingbook.repository.AppRepository
import java.util.*

open class CookingApplication : Application() {


    companion object {
        lateinit var appDatabase: AppDatabase
        lateinit var appContext : Context
    }
    override fun onCreate() {
        super.onCreate()
        appDatabase = AppDatabase.getInstance(this)
        appContext = this
    }

}
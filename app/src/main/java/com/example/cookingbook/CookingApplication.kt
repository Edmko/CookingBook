package com.example.cookingbook

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import com.example.cookingbook.db.AppDatabase

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
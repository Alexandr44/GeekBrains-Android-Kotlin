package com.alex44.kotlincourse.view.ui

import android.app.Application
import com.alex44.kotlincourse.di.appModule
import com.alex44.kotlincourse.di.mainModule
import com.alex44.kotlincourse.di.noteModule
import com.alex44.kotlincourse.di.splashModule
import com.github.ajalt.timberkt.Timber
import org.koin.android.ext.android.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(timber.log.Timber.DebugTree())
        startKoin(this, listOf(appModule, splashModule, mainModule, noteModule))
    }
}
package com.example.musicplayer

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import com.example.musicplayer.di.AppModule
import com.example.musicplayer.di.DataModule
import com.example.musicplayer.di.RepoModule

/**
 * @author Tomislav Curis
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(AppModule, DataModule, RepoModule))
        }
    }
}
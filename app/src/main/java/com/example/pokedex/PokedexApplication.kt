package com.example.pokedex

import android.app.Application
import com.example.pokedex.di.databaseModules
import com.example.pokedex.di.networkModules
import com.example.pokedex.di.repositoryModules
import com.example.pokedex.di.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class PokedexApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@PokedexApplication)
            loadKoinModules(
                listOf(
                    networkModules,
                    repositoryModules,
                    viewModelModules,
                    databaseModules
                )
            )
        }
    }
}
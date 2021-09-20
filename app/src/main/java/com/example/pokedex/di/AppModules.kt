package com.example.pokedex.di

import androidx.room.Room
import com.example.api.api.PokemonApi
import com.example.pokedex.BuildConfig
import com.example.pokedex.database.PokemonDatabase
import com.example.pokedex.repo.PokeRepository
import com.example.pokedex.viewmodel.PokemonDetailsViewModel
import com.example.pokedex.viewmodel.PokemonViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModules = module {
    single {
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
    factory {
        val retrofit: Retrofit = get()
        retrofit.create(PokemonApi::class.java)
    }
}
val databaseModules = module {
    single {
        Room.databaseBuilder(
            androidApplication().applicationContext,
            PokemonDatabase::class.java,
            "pokemon.db"
        ).build()
    }
}
val repositoryModules = module {
    single {
        val pokemonApi = get<PokemonApi>()
        val pokemonDatabase = get<PokemonDatabase>()
        PokeRepository(pokemonApi, pokemonDatabase)
    }
}

val viewModelModules = module {
    viewModel {
        val pokeRepository: PokeRepository = get()
        PokemonViewModel(pokeRepository)
    }
    viewModel{
        val pokeRepository: PokeRepository = get()
        PokemonDetailsViewModel(get(),pokeRepository)
    }
}
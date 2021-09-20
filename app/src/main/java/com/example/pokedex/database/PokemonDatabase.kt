package com.example.pokedex.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokedex.database.typeconverters.PokeTypeConverters
import com.example.pokedex.pojo.Pokemon
import com.example.pokedex.pojo.PokemonStat
import com.example.pokedex.pojo.PokemonSubDetails
import com.example.pokedex.pojo.PokemonType

@Database(
    entities = [
        Pokemon::class,
        RemoteKey::class,
        PokemonSubDetails::class,
    ], version = 1
)
@TypeConverters(value = [PokeTypeConverters::class])
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}
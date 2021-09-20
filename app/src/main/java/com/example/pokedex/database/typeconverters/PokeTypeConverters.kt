package com.example.pokedex.database.typeconverters

import androidx.room.TypeConverter
import com.example.pokedex.pojo.PokemonStat
import com.example.pokedex.pojo.PokemonType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PokeTypeConverters {

    @TypeConverter
    fun pokemonTypeListToString(pokeTypes: List<PokemonType>?) = Gson().toJson(pokeTypes)

    @TypeConverter
    fun pokemonStringTypeToList(pokeTypes: String?): List<PokemonType>? {
        return Gson().fromJson(pokeTypes, object : TypeToken<List<PokemonType>>() {}.type)
    }

    @TypeConverter
    fun pokemonStatListToString(pokeStats: List<PokemonStat>?) = Gson().toJson(pokeStats)

    @TypeConverter
    fun pokemonStringStatsToList(pokeStats: String?): List<PokemonStat>? {
        return Gson().fromJson(pokeStats, object : TypeToken<List<PokemonStat>>() {}.type)
    }
}
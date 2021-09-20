package com.example.pokedex.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.pokedex.pojo.PokemonPalette

@Dao
interface PokemonPaletteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPalette(pokemonPalette: PokemonPalette)
}
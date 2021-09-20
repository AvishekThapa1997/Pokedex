package com.example.pokedex.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "palette")
data class PokemonPalette(
    @PrimaryKey
    val pokemonId: Long,
    val paletteCode: Int
)

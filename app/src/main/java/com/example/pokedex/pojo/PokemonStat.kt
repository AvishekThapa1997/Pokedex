package com.example.pokedex.pojo

import androidx.room.Embedded


data class PokemonStat(
    val base_status: Int,
    @Embedded
    val pokemonStatDetails: PokemonStatDetails
)

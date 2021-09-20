package com.example.pokedex.pojo

import androidx.room.Embedded
import androidx.room.Relation

data class PokemonDetails(
    @Embedded val pokemon: Pokemon,
    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "subDetailsId"
    )
    val pokemonSubDetails: PokemonSubDetails?
)

fun PokemonDetails.isFullDetailNotAvailable() = pokemonSubDetails == null

package com.example.pokedex.pojo

import androidx.room.Embedded
import androidx.room.Relation

data class PokemonDetailsAndType(
    @Embedded val pokemonSubDetails: PokemonSubDetails,
    @Relation(
        parentColumn = "subDetailsId",
        entityColumn = "pokemonId"
    )
    val pokemonType: List<PokemonType>
)

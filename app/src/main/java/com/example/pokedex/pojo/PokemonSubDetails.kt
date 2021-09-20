package com.example.pokedex.pojo

import androidx.room.*

@Entity(
    tableName = "pokemon_sub_details",
    foreignKeys = [
        ForeignKey(
            entity = Pokemon::class,
            parentColumns = ["pokemonId"],
            childColumns = ["subDetailsId"]
        )
    ]
)
data class PokemonSubDetails(
    @PrimaryKey
    val subDetailsId: Long,
    val height: Double,
    val weight: Double,
    val pokemonTypes: List<PokemonType>,
    val pokemonStats: List<PokemonStat>
)

package com.example.pokedex.pojo

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

//@Entity(
//    tableName = "pokemon_type",
//    foreignKeys = [
//        ForeignKey(
//            entity = PokemonSubDetails::class,
//            parentColumns = arrayOf("subDetailsId"),
//            childColumns = arrayOf("pokemonId")
//        )
//    ]
//)
//data class PokemonType(
//    @PrimaryKey(autoGenerate = true)
//    val typeId: Long = 0,
//    val pokemonId: Long,
//    @Embedded
//    val type: PokemonTypeDetail
//)

data class PokemonType(
    @Embedded
    val type: PokemonTypeDetail
)


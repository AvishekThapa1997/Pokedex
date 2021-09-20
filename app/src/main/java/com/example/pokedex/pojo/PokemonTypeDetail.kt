package com.example.pokedex.pojo


import com.example.pokedex.R
import java.util.*

data class PokemonTypeDetail(
    val name: String,
)

fun PokemonTypeDetail.parseTypeToColor(): Int {
    return when (name.lowercase(Locale.ROOT)) {
        "normal" -> R.color.type_normal
        "fire" -> R.color.typeFire
        "water" -> R.color.typeWater
        "electric" -> R.color.typeElectric
        "grass" -> R.color.typeGrass
        "ice" -> R.color.typeIce
        "fighting" -> R.color.typeFighting
        "poison" -> R.color.typePoison
        "ground" -> R.color.typeGround
        "flying" -> R.color.typeFlying
        "psychic" -> R.color.typePsychic
        "bug" -> R.color.typeBug
        "rock" -> R.color.typeRock
        "ghost" -> R.color.typeGhost
        "dragon" -> R.color.typeDragon
        "dark" -> R.color.typeDark
        "steel" -> R.color.typeSteel
        "fairy" -> R.color.typeFairy
        else -> R.color.black
    }
}
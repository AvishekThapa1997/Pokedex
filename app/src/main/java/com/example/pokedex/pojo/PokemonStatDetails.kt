package com.example.pokedex.pojo


import android.graphics.Color
import com.example.pokedex.R

data class PokemonStatDetails(
    val name: String
)

fun PokemonStatDetails.parseStatToColor(): Int {
    return when (name.lowercase()) {
        "hp" -> R.color.hp
        "attack" -> R.color.attack
        "defense" -> R.color.defence
        "special-attack" -> R.color.special_attack
        "special-defense" -> R.color.special_defence
        "speed" -> R.color.speed
        else -> R.color.white
    }
}

fun PokemonStatDetails.parseStatToAbbr(): String {
    return when (name.lowercase()) {
        "hp" -> "HP"
        "attack" -> "ATTACK"
        "defense" -> "DEFENCE"
        "special-attack" -> "SP-ATTACK"
        "special-defense" -> "SP-DEFENCE"
        "speed" -> "SPEED"
        else -> ""
    }
}

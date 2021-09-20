package com.example.api.model

data class PokemonDetailsDto(
    val height: Double,
    val weight: Double,
    val types: List<PokemonTypeDto>,
    val stats: List<PokemonStatDto>
)
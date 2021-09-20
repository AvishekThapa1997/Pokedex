package com.example.api.response

import com.example.api.model.PokemonDto

data class PokemonResponse(
    val previous: String?,
    val next: String?,
    val results: List<PokemonDto>
)

package com.example.api.api

import com.example.api.model.PokemonDetailsDto
import com.example.api.response.PokemonResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {
    @GET("pokemon?limit=20")
    suspend fun getPokemon(
        @Query("offset") offset: Int
    ): PokemonResponse

    @GET("pokemon/{id}")
    suspend fun getPokemonDetails(@Path("id") pokemonId: Long): PokemonDetailsDto
}
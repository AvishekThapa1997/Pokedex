package com.example.pokedex.database

import androidx.paging.PagingSource
import androidx.room.*
import com.example.pokedex.pojo.*

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPokemon(pokemon: List<Pokemon>)

    @Query("SELECT * FROM pokemon WHERE pokemonName  LIKE '%' || :query || '%'")
    fun getPokemonEntries(query: String): PagingSource<Int, Pokemon>

    @Query("DELETE FROM pokemon")
    suspend fun deleteAllPokemon()

    @Query("SELECT MAX(createdAt) FROM pokemon")
    suspend fun getLastRefreshedTime(): Long?

    @Query("SELECT * FROM pokemon WHERE pokemonId = :id")
    fun getPokemonById(id: Long): Pokemon

    @Transaction
    @Query("SELECT * FROM pokemon WHERE pokemonId = :pokemonId")
    suspend fun getPokemonDetailsById(pokemonId: Long): PokemonDetails

    @Insert
    suspend fun insertPokemonDetails(pokemonSubDetails: PokemonSubDetails)
}
package com.example.pokedex

import com.example.api.api.PokemonApi
import com.example.pokedex.di.networkModules
import com.example.pokedex.di.repositoryModules
import com.example.pokedex.repo.PokeRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

class PokemonApiTest : KoinTest {
    private val pokeRepository by inject<PokeRepository>()
    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(networkModules, repositoryModules)
    }
    @Test
    fun Check_PokeApi() = runBlocking {
            val response = pokeRepository.getPokeMon()
        }
    }
package com.example.pokedex.repo


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.api.api.PokemonApi
import com.example.pokedex.database.PokemonDatabase
import com.example.pokedex.database.PokemonDao
import com.example.pokedex.pojo.*
import com.example.pokedex.util.networkBoundResource
import kotlinx.coroutines.flow.Flow


class PokeRepository(
    private val pokemonApi: PokemonApi,
    private val pokemonDatabase: PokemonDatabase
) {
    private val pokemonDao: PokemonDao by lazy {
        pokemonDatabase.pokemonDao()
    }
//    private val pokemonPaletteDao: PokemonPaletteDao by lazy {
//        pokemonDatabase.paletteDao()
//    }

    companion object {
        const val BASE_IMAGE_URL =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"
        const val NETWORK_PAGE_SIZE = 20
    }


    fun getAllPokemon(
        query: String
    ): Flow<PagingData<Pokemon>> {
        return Pager(
            PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = 5
            ),
            remoteMediator = PokemonRemoteMediator(
                pokemonDatabase, pokemonApi
            ),
        ) {
            pokemonDao.getPokemonEntries(query)
        }.flow
    }

    fun getPokemonById(pokemonId: Long, onFetchFailed: (Throwable) -> Unit) = networkBoundResource(
        query = {
            pokemonDao.getPokemonDetailsById(pokemonId)
        },
        shouldFetch = { pokemonDetails ->
            pokemonDetails.isFullDetailNotAvailable()
        },
        fetch = {
            val pokemonDetailsDto = pokemonApi.getPokemonDetails(pokemonId)
            pokemonDetailsDto
        },
        saveFetchResult = { pokemonDetailsDto ->
            val pokemonTypes = pokemonDetailsDto.types.map { pokemonTypeDto ->
                PokemonType(
                    type = PokemonTypeDetail(
                        pokemonTypeDto.type.name,
                    )
                )
            }
            val pokemonStats = pokemonDetailsDto.stats.map { pokemonStatDto ->
                PokemonStat(
                    base_status = pokemonStatDto.base_stat,
                    pokemonStatDetails = PokemonStatDetails(
                        pokemonStatDto.stat.name
                    )
                )
            }
            val pokemonSubDetails = PokemonSubDetails(
                subDetailsId = pokemonId,
                height = pokemonDetailsDto.height,
                weight = pokemonDetailsDto.weight,
                pokemonTypes = pokemonTypes,
                pokemonStats = pokemonStats
            )
            pokemonDao.insertPokemonDetails(pokemonSubDetails)
        },
        onFetchFailed = {
            onFetchFailed(it)
        }
    )

//    suspend fun insert(pokemonPalette: PokemonPalette) {
//        pokemonPaletteDao.insertPalette(pokemonPalette)
//    }
//    suspend fun updatePokemon(pokemonId : Long,paletteCode : Int){
//        pokemonDao.updatePokemonPalette(
//            pokemonId, paletteCode
//        )
//    }
}

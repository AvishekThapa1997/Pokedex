package com.example.pokedex.repo


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.api.api.PokemonApi
import com.example.pokedex.pojo.Pokemon

class PokemonPagingSource(
    private val pokemonApi: PokemonApi
) : PagingSource<Int, Pokemon>() {

    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(20) ?: anchorPage?.nextKey?.minus(20)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        return try {
            val offset = params.key ?: 0
            val response = pokemonApi.getPokemon(offset)
            val pokeMons = response.results
            val pokemonEntries = pokeMons.map { pokemon ->
                val index = if (pokemon.url.endsWith("/")) {
                    pokemon.url.dropLast(1).takeLastWhile { currChar ->
                        currChar.isDigit()
                    }
                } else {
                    pokemon.url.takeLastWhile { currentChar ->
                        currentChar.isDigit()
                    }
                }
                Pokemon(
                    pokemonId = index.toLong(),
                    pokemonName = pokemon.name,
                    imageUrl = "${PokeRepository.BASE_IMAGE_URL}/${index}.png",
                )
            }
            LoadResult.Page(
                data = pokemonEntries,
                prevKey = if (response.previous != null) {
                    offset
                } else {
                    null
                },
                nextKey = if (response.next != null) {
                    offset + 20
                } else {
                    null
                }
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override val keyReuseSupported: Boolean
        get() = true
}
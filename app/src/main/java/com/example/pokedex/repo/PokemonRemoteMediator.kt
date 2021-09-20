package com.example.pokedex.repo



import androidx.paging.*
import androidx.room.withTransaction
import com.example.api.api.PokemonApi
import com.example.pokedex.database.PokemonDao
import com.example.pokedex.database.PokemonDatabase
import com.example.pokedex.database.RemoteKey
import com.example.pokedex.database.RemoteKeyDao
import com.example.pokedex.pojo.Pokemon
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@ExperimentalPagingApi
class PokemonRemoteMediator(
    private val pokemonDatabase: PokemonDatabase,
    private val pokemonApi: PokemonApi
) : RemoteMediator<Int, Pokemon>() {
    private val pokemonDao: PokemonDao by lazy {
        pokemonDatabase.pokemonDao()
    }

    private val remoteKeyDao: RemoteKeyDao by lazy {
        pokemonDatabase.remoteKeyDao()
    }

    override suspend fun initialize(): InitializeAction {
        val lastRefreshedTime = pokemonDao.getLastRefreshedTime()
        return if (TimeUnit.MILLISECONDS.toDays(lastRefreshedTime ?: 0) < 1) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Pokemon>
    ): MediatorResult {
        return try {
            val offset = when (loadType) {
                LoadType.REFRESH -> {
                    0
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val remoteKey = pokemonDatabase.remoteKeyDao().getLastKey()
                    val nextKey = remoteKey.nextOffset ?: return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                    nextKey
                }
            }
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
            pokemonDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    pokemonDao.deleteAllPokemon()
                    remoteKeyDao.deleteAllKeys()
                }
                pokemonDao.insertAllPokemon(pokemonEntries)
                remoteKeyDao.insertKey(
                    RemoteKey(
                        nextOffset = offset + 20
                    )
                )

            }
            MediatorResult.Success(endOfPaginationReached = response.next == null)
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }
//    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Pokemon>): RemoteKey? {
//        return state.pages.lastOrNull() {
//            it.data.isNotEmpty()
//        }?.data?.lastOrNull()
//            ?.let { repo ->
//
//            }
//    }
}
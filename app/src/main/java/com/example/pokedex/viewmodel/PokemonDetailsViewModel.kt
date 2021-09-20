package com.example.pokedex.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.repo.PokeRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PokemonDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val pokeRepository: PokeRepository
) : ViewModel() {
    private var _pokemonId = savedStateHandle.get<Long>("pokemonId") ?: 0
    private val _error = Channel<Throwable>()
    val error get() = _error.receiveAsFlow()

    //    val pokemonId: Long
//        get() = _pokemonId
    private val pokemonId = MutableStateFlow(0L)
    val pokemonData = pokemonId.flatMapLatest { id ->
        if (id > 0) {
            pokeRepository.getPokemonById(id, onFetchFailed = {
                viewModelScope.launch {
                    Log.i("TAG", it?.localizedMessage)
                    _error.send(it)
                }
            })
        } else {
            emptyFlow()
        }
    }.shareIn(
        viewModelScope,
        SharingStarted.Lazily
    )

    fun setPokemonId(pokemonId: Long) {
        this.pokemonId.value = pokemonId
    }
}
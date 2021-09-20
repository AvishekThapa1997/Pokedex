package com.example.pokedex.viewmodel


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.pokedex.pojo.PokemonPalette
import com.example.pokedex.repo.PokeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class PokemonViewModel(
    private val pokeRepository: PokeRepository,
) : ViewModel() {
    private val searchQuery = MutableStateFlow("");
    private val _paletteRGB = mutableMapOf<Long, Int>()
    val paletteRGB: Map<Long, Int> = _paletteRGB
    var scrollToTop = false


    private var _previousSearchQuery = ""
    fun searchPokemon(query: String) {
        if (_previousSearchQuery != query) {
            _previousSearchQuery = query
            searchQuery.value = _previousSearchQuery
            scrollToTop = true
        }
    }

    val response = searchQuery.flatMapLatest { query ->
        pokeRepository.getAllPokemon(query)
    }.cachedIn(viewModelScope)

    fun cacheGeneratePalette(pokemonId: Long, paletteRGB: Int) {
        _paletteRGB[pokemonId] = paletteRGB
    }

    //fun cachedPaletteRGB(pokemonName: String) = _paletteRGB[pokemonName]
}
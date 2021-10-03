package com.example.pokedex.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.ItemLayoutBinding
import com.example.pokedex.pojo.Pokemon
import kotlinx.coroutines.*

class PokemonAdapter(
    private val memoryCachePalette: Map<Long, Int>,
    private val paletteGenerator: (Int, Long) -> Unit,
    private val toDetailsFragment: (Long) -> Unit
) : PagingDataAdapter<Pokemon, PokemonAdapter.PokemonViewHolder>(PokemonUtil) {
    companion object {
        private val paletteJobScope = CoroutineScope(Dispatchers.IO)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val itemLayoutBinding = ItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PokemonViewHolder(itemLayoutBinding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        getItem(position)?.let { pokemon ->
            holder.bindView(pokemon)
        }
    }

    fun cancel() = paletteJobScope.cancel()
    inner class PokemonViewHolder(private val itemLayoutBinding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(itemLayoutBinding.root) {
        fun bindView(pokemon: Pokemon) {
            pokemon.run { ->
                itemLayoutBinding.run {
                    this.pokemon = pokemon
                    detailCallBack = toDetailsFragment
                }
            }

//                itemLayoutBinding.root.context.run {
//                    loadImage(
//                        imageUrl = imageUrl,
//                        containerView = itemLayoutBinding.ivPokemonImage,
//                        requiredPalette = true,
//                        checkPaletteExistence = {
//                            val paletteCode = when {
//                                memoryCachePalette.containsKey(pokemonId) -> {
//                                    memoryCachePalette[pokemonId]
//                                }
//                                else -> {
//                                    null
//                                }
//                            }
//                            if (paletteCode != null) {
//                                setCardColor(paletteCode, pokemonId)
//                                true
//                            } else {
//                                false
//                            }
//                        },
//                        coroutineScope = paletteJobScope,
//                        dominantColorCode = { colorCode ->
//                            setCardColor(colorCode, pokemonId)
//                        }
//                    )
//                }
        }
    }

//        private fun setCardColor(rgb: Int?, pokemonId: Long? = null) {
//            rgb?.let {
//                itemLayoutBinding.run {
//                    root
//                    pokemonId?.let {
//                        paletteGenerator(rgb, pokemonId)
//                    }
//                }
//            }
//        }
}

object PokemonUtil : DiffUtil.ItemCallback<Pokemon>() {
    override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon) =
        oldItem.pokemonId == newItem.pokemonId

    override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon) =
        oldItem == newItem

}

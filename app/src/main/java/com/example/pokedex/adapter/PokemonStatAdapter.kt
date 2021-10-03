package com.example.pokedex.adapter

import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.databinding.ItemStatBinding
import com.example.pokedex.pojo.PokemonStat
import com.example.pokedex.pojo.parseStatToAbbr
import com.example.pokedex.pojo.parseStatToColor
import com.example.pokedex.util.getColorCode

class PokemonStatAdapter :
    ListAdapter<PokemonStat, PokemonStatAdapter.PokemonStatViewHolder>(PokemonStatComparator) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonStatViewHolder {
        val itemStatBinding = ItemStatBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PokemonStatViewHolder(itemStatBinding)

    }

    override fun onBindViewHolder(holder: PokemonStatViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class PokemonStatViewHolder(private val itemStatBinding: ItemStatBinding) :
        RecyclerView.ViewHolder(itemStatBinding.root) {
        private val _context: Context
            get() = itemStatBinding.root.context

        fun bind(pokemonStat: PokemonStat) {
            itemStatBinding.run {
                statName= pokemonStat.pokemonStatDetails.parseStatToAbbr()
                stat = pokemonStat.base_status.toString()
                progress = pokemonStat.base_status
                statProgress.setIndicatorColor(_context.getColorCode(pokemonStat.pokemonStatDetails.parseStatToColor()))
//                ObjectAnimator.ofInt(statProgress, "progress", 0, pokemonStat.base_status)
//                    .setDuration(500L)
//                    .start()
            }
        }
    }


    object PokemonStatComparator : DiffUtil.ItemCallback<PokemonStat>() {
        override fun areContentsTheSame(oldItem: PokemonStat, newItem: PokemonStat) =
            oldItem == newItem

        override fun areItemsTheSame(oldItem: PokemonStat, newItem: PokemonStat) =
            oldItem === newItem
    }
}
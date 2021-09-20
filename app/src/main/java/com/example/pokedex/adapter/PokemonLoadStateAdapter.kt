package com.example.pokedex.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.databinding.LoadStateItemBinding
import com.example.pokedex.util.stringResource

class PokemonLoadStateAdapter(private val retry : () -> Unit) : LoadStateAdapter<PokemonLoadStateAdapter.PokemonLoadViewHolder>() {
    class PokemonLoadViewHolder(
        private val retry : () -> Unit,
        private val loadStateItemBinding: LoadStateItemBinding
    ) : RecyclerView.ViewHolder(loadStateItemBinding.root) {
        private val context = loadStateItemBinding.root.context
        init {
            loadStateItemBinding.btnRetry.setOnClickListener {
                retry()
            }
        }
        fun bind(loadState: LoadState) {
            loadStateItemBinding.run {
               loadingProgress.isVisible = loadState is LoadState.Loading
                tvLoadStatusMessage.isVisible = (loadState is LoadState.Error).or(loadState is LoadState.Loading)
                btnRetry.isVisible = loadState is LoadState.Error
                when (loadState) {
                    is LoadState.Loading -> {
                        tvLoadStatusMessage.text = context.stringResource(R.string.loading)
                    }
                    is LoadState.Error -> {
                        tvLoadStatusMessage.text = context.stringResource(R.string.something_went_wrong)
                    }
                    else -> {

                    }
                }
            }

//                if (){
//                    loadStateItemBinding.run {
//                        tvState.text = context.stringResource(R.string.loading)
//                        loadingProgress.isVisible = true
//                    }
//                }
//                if(loadState is LoadState.Error) {
//                    loadStateItemBinding.run {
//                        tvState.text = context.stringResource(R.string.something_went_wrong)
//                        btnRetry.isVisible = true
//                    }
//                }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PokemonLoadViewHolder {
        val loadStateItemBinding = LoadStateItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PokemonLoadViewHolder(retry,loadStateItemBinding)
    }

    override fun onBindViewHolder(holder: PokemonLoadViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}
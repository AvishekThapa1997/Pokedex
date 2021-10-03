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

class PokemonLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<PokemonLoadStateAdapter.PokemonLoadViewHolder>() {
    class PokemonLoadViewHolder(
        retry: () -> Unit,
        private val loadStateItemBinding: LoadStateItemBinding
    ) : RecyclerView.ViewHolder(loadStateItemBinding.root) {
        private val context = loadStateItemBinding.root.context

        init {
            loadStateItemBinding.retry = retry
        }

        fun bind(loadState: LoadState) {
            Log.i("TAG", "bind: $loadState")
            loadStateItemBinding.run {
                loadingProgress.isVisible = loadState is LoadState.Loading
                tvLoadStatusMessage.isVisible =
                    (loadState is LoadState.Error).or(loadState is LoadState.Loading)
                btnRetry.isVisible = loadState is LoadState.Error
                when (loadState) {
                    is LoadState.Loading -> {
                       message = context.stringResource(R.string.loading)
                    }
                    is LoadState.Error -> {
                        message =
                            context.stringResource(R.string.something_went_wrong)
                    }
                    else -> {

                    }
                }
            }
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
        return PokemonLoadViewHolder(retry, loadStateItemBinding)
    }

    override fun onBindViewHolder(holder: PokemonLoadViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}
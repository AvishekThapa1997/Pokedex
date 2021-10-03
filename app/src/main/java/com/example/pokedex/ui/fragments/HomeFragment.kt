package com.example.pokedex.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.R
import com.example.pokedex.adapter.PokemonAdapter
import com.example.pokedex.adapter.PokemonLoadStateAdapter
import com.example.pokedex.databinding.HomeFragmentBinding
import com.example.pokedex.util.RecyclerViewDecoration
import com.example.pokedex.util.stringResource
import com.example.pokedex.viewmodel.PokemonViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(R.layout.home_fragment) {
    private var _homeFragmentBinding: HomeFragmentBinding? = null
    private val homeFragmentBinding get() = _homeFragmentBinding!!
    private val pokemonViewModel by viewModel<PokemonViewModel>()
    private val pokemonAdapter: PokemonAdapter by lazy {
        PokemonAdapter(
            memoryCachePalette = pokemonViewModel.paletteRGB,
            paletteGenerator = { paletteRGB, pokemonId ->
                pokemonViewModel.cacheGeneratePalette(
                    pokemonId = pokemonId,
                    paletteRGB = paletteRGB
                )
            },
            toDetailsFragment = { pokemonId ->
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                    pokemonId,
                    pokemonViewModel.paletteRGB[pokemonId] ?: 0
                )
                findNavController().navigate(action)
            })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _homeFragmentBinding = HomeFragmentBinding.bind(view)
        observeData()
        setUpRecyclerView()
        homeFragmentBinding.etSearchText.addTextChangedListener { query ->
            val searchQuery = query?.toString() ?: ""
            pokemonViewModel.searchPokemon(searchQuery)
        }
        homeFragmentBinding.btnRetry.setOnClickListener {
            pokemonAdapter.retry()
        }
        observeLoadState()
    }

    private fun observeLoadState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            pokemonAdapter.loadStateFlow.collectLatest { loadState ->
                homeFragmentBinding.run {
                    val refresh = loadState.mediator?.refresh
//                    loadStatusContainer.isVisible =
//                        refresh is LoadState.Error
                    //swipe.isRefreshing = refresh is LoadState.Loading
                     //loadingProgress.isVisible = refresh is LoadState.Loading && pokemonAdapter.itemCount <= 0
//                    btnRetry.isVisible = refresh is LoadState.Error
//                    tvLoadStatusMessage.isVisible = refresh is LoadState.Error
                    if (refresh is LoadState.Error) {
                        tvLoadStatusMessage.text = if (refresh is LoadState.Loading) {
                            requireContext().stringResource(R.string.loading)
                        } else {
                            refresh.error.localizedMessage
                                ?: requireContext().stringResource(R.string.something_went_wrong)
                        }
                    }
                }

            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            pokemonAdapter.loadStateFlow.distinctUntilChangedBy {
                it.source.refresh
            }.filter {
                it.source.refresh is LoadState.NotLoading
            }.collect {
                if (pokemonViewModel.scrollToTop) {
                    homeFragmentBinding.pokemonList.scrollToPosition(0)
                    pokemonViewModel.scrollToTop = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _homeFragmentBinding = null
        pokemonAdapter.cancel()
    }


    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            pokemonViewModel.response.collectLatest { pagingData ->
                pokemonAdapter.submitData(pagingData)
            }
        }
    }

    private fun setUpRecyclerView() {
        homeFragmentBinding.pokemonList.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                RecyclerViewDecoration(
                    resources.getDimension(R.dimen.top)
                )
            )
            itemAnimator?.changeDuration = 0

            adapter = pokemonAdapter.withLoadStateFooter(
                footer = PokemonLoadStateAdapter {
                    pokemonAdapter.retry()
                }
            )
            //  adapter = pokemonAdapter
        }
    }
}
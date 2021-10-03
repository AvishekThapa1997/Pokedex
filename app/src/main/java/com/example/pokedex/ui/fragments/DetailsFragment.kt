package com.example.pokedex.ui.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.pokedex.databinding.DetailsFragmentBinding

import com.example.pokedex.viewmodel.PokemonDetailsViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.R
import com.example.pokedex.adapter.PokemonStatAdapter
import com.example.pokedex.pojo.parseTypeToColor
import com.example.pokedex.util.*
import java.util.*


class DetailsFragment : Fragment(R.layout.details_fragment) {
    private var _detailsFragmentBinding: DetailsFragmentBinding? = null
    private val detailsFragmentBinding get() = _detailsFragmentBinding!!
    private val pokemonDetailsViewModel: PokemonDetailsViewModel by stateViewModel()
    private val detailsFragmentArgs: DetailsFragmentArgs by navArgs()
    private val pokemonStatAdapter: PokemonStatAdapter by lazy {
        PokemonStatAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _detailsFragmentBinding = DetailsFragmentBinding.bind(view)
        pokemonDetailsViewModel.setPokemonId(detailsFragmentArgs.pokemonId)
        detailsFragmentBinding.root.setBackgroundColor(detailsFragmentArgs.paletteCode)
        detailsFragmentBinding.pokemonImage.post {
            detailsFragmentBinding.cardRootContainer.setContentPadding(
                0,
                (detailsFragmentBinding.pokemonImage.height * 0.50).toInt(),
                0,
                detailsFragmentBinding.cardRootContainer.contentPaddingBottom
            )
        }
        setRecyclerView()
        observePokemonDetails()
        observeError()
    }

    private fun observeError() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            pokemonDetailsViewModel.error.collectLatest {
                Toast.makeText(
                    requireContext(),
                    requireContext().stringResource(R.string.something_went_wrong),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setRecyclerView() {
        detailsFragmentBinding.pokemonStats.run {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = pokemonStatAdapter
        }
    }

    private fun observePokemonDetails() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            pokemonDetailsViewModel.pokemonData.collectLatest { resource ->
                val pokemonDetails = resource.data
                detailsFragmentBinding.run {
                    detailsLoadingProgress.isVisible = resource is Resource.Loading
                    tvPokemonName.isVisible =
                        resource is Resource.Loading || resource is Resource.Success
                    physicalDetailsGroup.isVisible =
                        resource is Resource.Success
                    flowContainer.isVisible =
                        resource is Resource.Success && !pokemonDetails?.pokemonSubDetails?.pokemonTypes.isNullOrEmpty()
                    pokemonStats.isVisible =
                        resource is Resource.Success && !pokemonDetails?.pokemonSubDetails?.pokemonStats.isNullOrEmpty()
                    pokemonDetails?.let { details ->
                        requireContext().loadImage(
                            imageUrl = details.pokemon.imageUrl,
                            containerView = pokemonImage
                        )
                        if (tvPokemonName.isVisible) {
                            pokemonName = getString(
                                R.string.pokemon_name,
                                details.pokemon.pokemonId,
                                details.pokemon.pokemonName
                            )
                        }
                        if (physicalDetailsGroup.isVisible) {
                            height = details.pokemonSubDetails?.weight.toString()
                            weight = details.pokemonSubDetails?.height.toString()
                        }
                        if (flowContainer.isVisible) {
                            flowContainer.setMaxElementsWrap(details.pokemonSubDetails?.pokemonTypes!!.size)
                            details.pokemonSubDetails.pokemonTypes.forEachIndexed { index, pokemonType ->
                                val typeTextView = LayoutInflater.from(requireContext())
                                    .inflate(
                                        R.layout.slot_textview,
                                        detailsContainer,
                                        false
                                    ) as TextView
                                with(typeTextView) {
                                    id = View.generateViewId()
                                    text = pokemonType.type.name.capitalizeFirstLetter()
                                    backgroundTintList =
                                        ColorStateList.valueOf(
                                            requireContext().getColorCode(pokemonType.type.parseTypeToColor())
                                        )
                                }
                                detailsContainer.addView(typeTextView, 0)
                                flowContainer.addView(typeTextView)
                            }
                        }
                        if (pokemonStats.isVisible) {
                            pokemonStatAdapter.submitList(details.pokemonSubDetails?.pokemonStats)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _detailsFragmentBinding = null
    }
}

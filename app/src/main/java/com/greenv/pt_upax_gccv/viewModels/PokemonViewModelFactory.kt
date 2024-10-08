package com.greenv.pt_upax_gccv.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.greenv.pt_upax_gccv.repositories.PokemonRepositoryContract

class PokemonViewModelFactory(private val pokemonRepository: PokemonRepositoryContract) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonViewModel::class.java)) {
            return PokemonViewModel(pokemonRepository) as T
        }
        throw IllegalArgumentException("Error in ViewModelFactory")
    }
}
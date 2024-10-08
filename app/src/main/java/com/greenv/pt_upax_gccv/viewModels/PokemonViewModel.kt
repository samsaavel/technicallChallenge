package com.greenv.pt_upax_gccv.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenv.pt_upax_gccv.network.NetworkResponse
import com.greenv.pt_upax_gccv.network.models.DetailsModel
import com.greenv.pt_upax_gccv.network.models.PokemonResponse
import com.greenv.pt_upax_gccv.repositories.PokemonRepositoryContract
import kotlinx.coroutines.launch

class PokemonViewModel(private val pokemonRepository: PokemonRepositoryContract) :
    ViewModel() {

    private var offset = 0
    private val paginationSize = 25

    private val _pokemonResponse = MutableLiveData<DataState<PokemonResponse>>()
    val pokemonResponse: LiveData<DataState<PokemonResponse>> = _pokemonResponse

    private val _detailsState = MutableLiveData<DataState<DetailsModel>>()
    val detailsState: LiveData<DataState<DetailsModel>> = _detailsState

    fun fetchNextPokemonPage() {
        viewModelScope.launch {
            _pokemonResponse.postValue(DataState.Loading)
            Log.d("*****PokeViewMode", "Offset: $offset")
            val response = pokemonRepository.fetchListPokemon(paginationSize, offset)
            when (response) {
                NetworkResponse.Failure -> {
                    _pokemonResponse.postValue(
                        DataState.Error("*****PokeViewModel error fetching pokemonList")
                    )
                }

                is NetworkResponse.Success -> {
                    val newPokemonList = response.data.result
                    newPokemonList.forEach { pokemon ->
                        val pokemonId = pokemon.id
                        if (pokemonId != null) {
                            val detailsResponse = pokemonRepository.getPokemonDetails(pokemonId)
                            if (detailsResponse is NetworkResponse.Success) {
                                pokemon.spriteUrl = detailsResponse.data.sprites.front_default
                            }
                        }
                    }
                    Log.d("*****PokeViewMode", "success fetching pokemonList")
                    _pokemonResponse.postValue(DataState.Success(response.data))
                    offset += paginationSize
                }
            }
        }
    }

    fun getDetailsPokemon(id: Int) {
        viewModelScope.launch {
            _detailsState.postValue(DataState.Loading)
            when (val response = pokemonRepository.getPokemonDetails(id)) {
                NetworkResponse.Failure -> {
                    _detailsState.postValue(
                        DataState.Error("*****PokeViewModel error gettingDetails")
                    )
                }

                is NetworkResponse.Success -> {
                    DataState.Error("*****PokeViewModel success gettingDetails")
                    _detailsState.postValue(
                        DataState.Success(response.data)
                    )
                }
            }
        }
    }

    fun markAsFavorite(pokemonId: Int) {
        viewModelScope.launch {
            pokemonRepository.updateFavoriteStatus(pokemonId, true)
        }
    }

    fun unmarkAsFavorite(pokemonId: Int) {

        viewModelScope.launch {
            pokemonRepository.updateFavoriteStatus(pokemonId, false)
        }
    }
}
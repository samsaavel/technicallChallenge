package com.greenv.pt_upax_gccv.repositories

import android.util.Log
import com.greenv.pt_upax_gccv.data.entities.PokemonMapper
import com.greenv.pt_upax_gccv.network.NetworkResponse
import com.greenv.pt_upax_gccv.network.models.DetailsModel
import com.greenv.pt_upax_gccv.network.models.PokemonResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface PokemonRepositoryContract {
    suspend fun fetchListPokemon(
        paginationSize: Int,
        pagingStart: Int,
    ): NetworkResponse<PokemonResponse>

    suspend fun getPokemonDetails(id: Int): NetworkResponse<DetailsModel>
}

class PokemonRepository(
//    val pokemonApi: PokemonListApi,
//    val detailsApi: PokemonDetailsApi,
//    val generationApi: GenerationApi,
//    val pokemonDao: PokemonDao,
    //private val context: Context,
    private val dataSync: DataSyncManager,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : PokemonRepositoryContract {

    override suspend fun fetchListPokemon(
        paginationSize: Int,
        pagingStart: Int,
    ): NetworkResponse<PokemonResponse> =
        withContext(dispatcher) {
            try {
                val localData = dataSync.fetchPokemon(paginationSize, pagingStart)
                if (localData.isNullOrEmpty()) {
                    return@withContext NetworkResponse.Failure
                }
                val nextUrl = null
                val totalPokemonCount = localData.size
                val previousUrl = null
                val pokemonModels = localData.map { PokemonMapper.entityToPokemonModel(it) }
                val response = PokemonResponse(
                    count = totalPokemonCount,
                    next = nextUrl,
                    previous = previousUrl,
                    result = ArrayList(pokemonModels)
                )
                Log.d("*****PokeRepo", "fetchListPokemon success")
                NetworkResponse.Success(response)
            } catch (e: Throwable) {
                Log.d("*****PokeRepo", "${e.printStackTrace()}")
                Log.d("*****PokeRepo", "fetchListPokemon failed")
                NetworkResponse.Failure
            }
        }

    override suspend fun getPokemonDetails(id: Int): NetworkResponse<DetailsModel> =
        withContext(dispatcher) {
            try {
                val details = dataSync.fetchPokemonDetails(id)
                if (details == null) {
                    Log.d("*****PokeRepo", "Pokemon details not found")
                    return@withContext NetworkResponse.Failure
                }
                //val response = detailsApi.getDetails(id)
                Log.d("*****PokeRepo", "getDetailsPokemon success")
                NetworkResponse.Success(details)
            } catch (e: Throwable) {
                Log.d("*****PokeRepo", "${e.printStackTrace()}")
                Log.d("*****PokeRepo", "getPokemonDetails failed")
                NetworkResponse.Failure
            }
        }
}
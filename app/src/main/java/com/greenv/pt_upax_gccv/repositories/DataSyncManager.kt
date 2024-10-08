package com.greenv.pt_upax_gccv.repositories

import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.greenv.pt_upax_gccv.data.dao.PokemonDao
import com.greenv.pt_upax_gccv.data.entities.PokemonEntity
import com.greenv.pt_upax_gccv.data.entities.PokemonMapper
import com.greenv.pt_upax_gccv.network.models.DetailsModel
import com.greenv.pt_upax_gccv.network.models.PokemonModel
import com.greenv.pt_upax_gccv.network.services.PokemonDetailsApi
import com.greenv.pt_upax_gccv.network.services.PokemonListApi

class DataSyncManager(
    private val pokemonApi: PokemonListApi,
    private val detailsApi: PokemonDetailsApi,
    private val pokemonDao: PokemonDao,
    private val connectivityManager: ConnectivityManager,
) {
    fun isOnline(): Boolean {
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }

    suspend fun fetchPokemon(paginationSize: Int, pagingStart: Int): List<PokemonEntity>? {
        return if (isOnline()) {
            fetchPokemonFromNetwork(paginationSize, pagingStart)
        } else {
            fetchPokemonFromDatabase()
        }
    }

    suspend fun fetchPokemonFromNetwork(
        paginationSize: Int,
        pagingStart: Int,
    ): List<PokemonEntity>? {
        return try {
            val response = pokemonApi.fetchPokemon(paginationSize, pagingStart)
            val pokemonEntities = response.result.mapNotNull { pokemon ->
                val pokemonId = pokemon.id ?: return@mapNotNull null
                val details = detailsApi.getDetails(pokemonId)
                PokemonMapper.modelToEntity(pokemon, details)
            }
            syncPokemonToDatabase(pokemonEntities)
            pokemonEntities
        } catch (e: Exception) {
            Log.d("*****DataSync", "error: ${e.printStackTrace()}")
            null
        }
    }

    suspend fun fetchPokemonDetails(id: Int): DetailsModel? {
        return if (isOnline()) {
            fetchDetailsFromNetwork(id)
        } else {
            fetchDetailsFromDatabase(id)
        }
    }

    private suspend fun fetchDetailsFromNetwork(id: Int): DetailsModel? {
        return try {
            val details = detailsApi.getDetails(id)
            val entity = pokemonDao.getPokemonById(id) ?: return null
            val pokemonModel = PokemonModel(
                name = entity.name,
                url = entity.url,
                spriteUrl = entity.frontDefault
            )
            val updatedEntity = PokemonMapper.modelToEntity(pokemonModel, details)
            pokemonDao.insert(updatedEntity) // Sync details to database
            details
        } catch (e: Exception) {
            Log.d("*****DataSync", "Network error: ${e.printStackTrace()}")
            null
        }
    }

    private suspend fun fetchDetailsFromDatabase(id: Int): DetailsModel? {
        return try {
            val entity = pokemonDao.getPokemonById(id)
            entity?.let { PokemonMapper.entityToDetailsModel(it) }
        } catch (e: Exception) {
            Log.d("*****DataSync", "Database error: ${e.printStackTrace()}")
            null
        }
    }

    suspend fun fetchPokemonFromDatabase(): List<PokemonEntity>? {
        return try {
            pokemonDao.getAllPokemon()
        } catch (e: Exception) {
            Log.d("*****DataSync", "error: ${e.printStackTrace()}")
            null
        }
    }


    suspend fun syncPokemonToDatabase(pokemonEntities: List<PokemonEntity>) {
        try {
            val newEntities = mutableListOf<PokemonEntity>()
            for (entity in pokemonEntities) {
                val existingPokemon = pokemonDao.getPokemonById(entity.pokedexId)
                if (existingPokemon == null || existingPokemon.name != entity.name) {
                    newEntities.add(entity)
                }
            }
            if (newEntities.isNotEmpty()) {
                pokemonDao.insertAll(newEntities)
            }
            //pokemonDao.insertAll(pokemonEntities)
        } catch (e: Exception) {
            Log.d("*****DataSync", "error: ${e.printStackTrace()}")
            null
        }
    }

}
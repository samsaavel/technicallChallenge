package com.greenv.pt_upax_gccv.network.services

import com.greenv.pt_upax_gccv.network.models.DetailsModel
import com.greenv.pt_upax_gccv.network.models.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonListApi {
    @GET("pokemon")
    suspend fun fetchPokemon(
        @Query("limit") paginationSize: Int,
        @Query("offset") value: Int,
    ): PokemonResponse
}

interface PokemonDetailsApi {
    @GET("pokemon/{id}")
    suspend fun getDetails(@Path("id") id: Int): DetailsModel
}
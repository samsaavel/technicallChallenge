package com.greenv.pt_upax_gccv.network

import com.greenv.pt_upax_gccv.network.services.PokemonDetailsApi
import com.greenv.pt_upax_gccv.network.services.PokemonListApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkModule {

    private val pokeBaseUrl = "https://pokeapi.co/api/v2/"

    private val loggingInterceptor: Interceptor by lazy {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val pokeRetrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(pokeBaseUrl).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val pokemonApi: PokemonListApi by lazy { pokeRetrofit.create(PokemonListApi::class.java) }

    val detailsApi: PokemonDetailsApi by lazy { pokeRetrofit.create(PokemonDetailsApi::class.java) }
}
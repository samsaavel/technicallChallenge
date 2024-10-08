package com.greenv.pt_upax_gccv.network.services

import retrofit2.http.GET

interface ImageService {

    @GET()
    suspend fun fetchImageByUrl()
}
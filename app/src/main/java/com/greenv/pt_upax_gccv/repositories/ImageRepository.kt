package com.greenv.pt_upax_gccv.repositories

import com.greenv.pt_upax_gccv.network.NetworkResponse

interface ImageRepositoryContract {
    suspend fun fetchImage(): NetworkResponse<String>
}
class ImageRepository {
}
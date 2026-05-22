package com.example.rootup.model

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ImgBBApi {
    @Multipart
    @POST("1/upload")
    suspend fun uploadImage(
        @Query("key") apiKey: String,
        @Part image: MultipartBody.Part,
        @Part("name") name: RequestBody? = null,
        @Part("expiration") expiration: RequestBody? = null
    ): ImgBBResponse
}
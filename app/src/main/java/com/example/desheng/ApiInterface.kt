package com.example.desheng

import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("/getUrl")
    suspend fun getUrl(): Response<String>
}
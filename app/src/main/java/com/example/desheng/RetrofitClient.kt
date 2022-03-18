package com.example.desheng

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object  RetrofitClient {
    fun getInstance(): Retrofit {
        var mHttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        var mOkHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(mHttpLoggingInterceptor)
            .build()

        var gson: Gson = GsonBuilder().setLenient().create()

        var retrofit: Retrofit = retrofit2.Retrofit.Builder()
            .baseUrl("http://8.210.133.185")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(mOkHttpClient)
            .build()

        return retrofit
    }
}
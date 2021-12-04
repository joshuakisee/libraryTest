package com.paylater.paylater.utils

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface Api {

    @Headers("Content-Type: application/json")
    @GET("mobile/available-products")
    fun getProducts(
        @Header("Authorization") authorization:String,
    ): Call<Model.GetProduct>


    companion object {
        fun create(): Api {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://lipalater.dukalite.com/api/v1/")
                .build()

            return retrofit.create(Api::class.java)
        }
    }
}
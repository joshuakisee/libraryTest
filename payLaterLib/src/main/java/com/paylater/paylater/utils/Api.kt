package com.paylater.paylater.utils

import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface Api {

    @Headers("Content-Type: application/json")
    @POST("mobile/create-customer")
    fun createProfile(
        @Header("Authorization") authorization:String,
        @Body requestBody: RequestBody
    ): Call<Model.CreateProfile>

    @Headers("Content-Type: application/json")
    @GET("mobile/available-products")
    fun getProducts(
        @Header("Authorization") authorization:String,
        @Query("score") score:String,
        @Query("maxAmount") maxAmount:String,
        @Query("minAmount") minAmount:String,
        @Query("productTypeId") productTypeId:String,
        @Query("keyword") keyword:String,
    ): Call<Model.GetProduct>

    @Headers("Content-Type: application/json")
    @GET("mobile/product-groups")
    fun getBrands(
        @Header("Authorization") authorization:String,
    ): Call<Model.GetBrand>

    @Headers("Content-Type: application/json")
    @GET("mobile/filter-products")
    fun getBrandFilter(
        @Header("Authorization") authorization:String,
        @Query("categoryId") categoryId:String,
        @Query("productTypeId") productId:String,
        @Query("score") score:String,
    ): Call<Model.GetBrandFilter>

    @Headers("Content-Type: application/json")
    @POST("mobile/order/make")
    fun placeOrder(
        @Header("Authorization") authorization:String,
        @Body requestBody: RequestBody
    ): Call<Model.PlaceOrder>

    @Headers("Content-Type: application/json")
    @POST("mobile/order/pay-deposit")
    fun payNow(
        @Header("Authorization") authorization:String,
        @Body requestBody: RequestBody
    ): Call<Model.PayNow>

    @Headers("Content-Type: application/json")
    @GET("mobile/order/my-orders")
    fun getMyOrders(
        @Header("Authorization") authorization:String,
        @Query("phoneNumber") phoneNumber:String
    ): Call<Model.MyOrders>

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
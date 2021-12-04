package com.paylater.paylater.utils

import com.google.gson.annotations.SerializedName

object Model {

    data class GetProduct(
        @SerializedName("status") val status : String,
        @SerializedName("message") val message : String,
        @SerializedName("data") val data: Data,
    )

    data class Data(
        @SerializedName("availableProducts") val availableProducts: List<AvailableProducts>,
    );

    data class AvailableProducts(
        @SerializedName("id") val id : String,
        @SerializedName("categoryId") val categoryId : Int,
        @SerializedName("productTypeId") val productTypeId : Int,
        @SerializedName("name") val name : String,
        @SerializedName("ram") val ram : String,
        @SerializedName("rom") val rom : String,
        @SerializedName("screenSize") val screenSize : String,
        @SerializedName("description") val description : String,
        @SerializedName("status") val status : String,
        @SerializedName("image") val image : String,
        @SerializedName("price") val price : Double,
    )

}
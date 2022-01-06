package com.paylater.paylater.utils

import com.google.gson.annotations.SerializedName

object Model {

    data class CreateProfile(
        @SerializedName("status") val status : String,
        @SerializedName("message") val message : String,
    )

    data class PlaceOrder(
        @SerializedName("status") val status : String,
        @SerializedName("message") val message : String,
        @SerializedName("data") val data : PlaceOrderData,
    )

    data class PlaceOrderData(
        @SerializedName("order") val order : PlaceOrderDataOrder
    )

    data class PlaceOrderDataOrder(
        @SerializedName("id") val id : String,
        @SerializedName("productId") val productId : String,
        @SerializedName("customerId") val customerId : String,
        @SerializedName("price") val price : String,
        @SerializedName("depositPaid") val depositPaid : String,
        @SerializedName("monthlyInstallmentAmount") val monthlyInstallmentAmount : String,
        @SerializedName("duration") val duration : String,
        @SerializedName("status") val status : String,
        @SerializedName("orderStatus") val orderStatus : String,
        @SerializedName("paymentStatus") val paymentStatus : String,
        @SerializedName("fullyPaid") val fullyPaid : String,
        @SerializedName("nextInstallmentDate") val nextInstallmentDate : String,
        @SerializedName("paymentMethod") val paymentMethod : String,
        @SerializedName("paymentReference") val paymentReference : String,
        @SerializedName("createdOn") val createdOn : String,
        @SerializedName("updatedOn") val updatedOn : String,
    )

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
        @SerializedName("deposit") val deposit : Double,
        @SerializedName("moreImages") val moreImages : List<String>,
        @SerializedName("orderInstallments") val orderInstallments : List<AvailableProductsInstallments>,
        @SerializedName("productType") val productType : productType,
    )

    data class AvailableProductsInstallments(
        @SerializedName("id") val id : String,
        @SerializedName("orderId") val orderId : String,
        @SerializedName("status") val status : String,
        @SerializedName("installmentAmount") val installmentAmount : String,
        @SerializedName("installmentBalance") val installmentBalance : String,
        @SerializedName("installmentDueOn") val installmentDueOn : String,
        @SerializedName("paymentStatus") val paymentStatus : String,
        @SerializedName("installmentName") val installmentName : String,
        @SerializedName("installmentPercent") val installmentPercent : String,
    )

    data class productType(
        @SerializedName("id") val id : String,
        @SerializedName("type") val type : String,
        @SerializedName("status") val status : String,
    )

    data class GetBrand(
        @SerializedName("status") val status : String,
        @SerializedName("message") val message : String,
        @SerializedName("data") val data: BrandData,
    )

    data class BrandData(
        @SerializedName("productCategories") val productCategories: List<BrandDataCategories>,
        @SerializedName("productTypes") val productTypes: List<BrandDataProductTypes>,
    )

    data class BrandDataCategories(
        @SerializedName("id") val id : String,
        @SerializedName("category") val category : String,
        @SerializedName("status") val status : String,
    )

    data class BrandDataProductTypes(
        @SerializedName("id") val id : String,
        @SerializedName("type") val type : String,
        @SerializedName("status") val status : String,
    )

    data class GetBrandFilter(
        @SerializedName("status") val status : String,
        @SerializedName("message") val message : String,
        @SerializedName("data") val data: GetBrandFilterAvailableProduct,
    )

    data class GetBrandFilterAvailableProduct(
        @SerializedName("availableProducts") val availableProducts : List<GetBrandFilterAvailableProducts>,
    )

    data class GetBrandFilterAvailableProducts(
        @SerializedName("id") val id : String,
        @SerializedName("categoryId") val categoryId : String,
        @SerializedName("productTypeId") val productTypeId : String,
        @SerializedName("name") val name : String,
        @SerializedName("ram") val ram : String,
        @SerializedName("rom") val rom : String,
        @SerializedName("screenSize") val screenSize : String,
        @SerializedName("description") val description : String,
        @SerializedName("status") val status : String,
        @SerializedName("image") val image : String,
        @SerializedName("price") val price : String,
        @SerializedName("deposit") val deposit : String,
        @SerializedName("moreImages") val moreImages : List<String>,
        @SerializedName("orderInstallments") val orderInstallments : List<AvailableProductsInstallments>,
    )

    data class PayNow(
        @SerializedName("status") val status : String,
        @SerializedName("message") val message : String,
        @SerializedName("data") val data : PayNowData,
    )

    data class PayNowData(
        @SerializedName("reference") val reference : String,
    )

    data class MyOrders(
        @SerializedName("status") val status : String,
        @SerializedName("message") val message : String,
        @SerializedName("data") val data: MyOrdersData,
    )

    data class MyOrdersData(
        @SerializedName("orders") val orders : List<MyOrdersDataOrders>
    )

    data class MyOrdersDataOrders(
        @SerializedName("id") val id : String,
        @SerializedName("productId") val productId : String,
        @SerializedName("customerId") val customerId : String,
        @SerializedName("price") val price : String,
        @SerializedName("depositPaid") val depositPaid : String,
        @SerializedName("monthlyInstallmentAmount") val monthlyInstallmentAmount : String,
        @SerializedName("duration") val duration : String,
        @SerializedName("status") val status : String,
        @SerializedName("orderStatus") val orderStatus : String,
        @SerializedName("paymentStatus") val paymentStatus : String,
        @SerializedName("fullyPaid") val fullyPaid : Boolean,
        @SerializedName("nextInstallmentDate") val nextInstallmentDate : String,
        @SerializedName("paymentMethod") val paymentMethod : String,
        @SerializedName("paymentReference") val paymentReference : String,
        @SerializedName("createdOn") val createdOn : String,
        @SerializedName("updatedOn") val updatedOn : String,
        @SerializedName("productName") val productName : String,
        @SerializedName("image") val image : String,
        @SerializedName("balance") val balance : String,
        @SerializedName("depositPaidState") val depositPaidState : Boolean,
        @SerializedName("deliveryStatus") val deliveryStatus : String,
        @SerializedName("deliveryDescription") val deliveryDescription : String,
        @SerializedName("orderInstallments") val orderInstallments : List<MyOrdersDataOrdersOrderInstallments>,
        @SerializedName("product") val product : MyOrdersDataOrdersProduct,
    )

    data class MyOrdersDataOrdersOrderInstallments(
        @SerializedName("id") val id : String,
        @SerializedName("orderId") val orderId : String,
        @SerializedName("status") val status : String,
        @SerializedName("installmentAmount") val installmentAmount : String,
        @SerializedName("installmentBalance") val installmentBalance : String,
        @SerializedName("installmentDueOn") val installmentDueOn : String,
        @SerializedName("paymentStatus") val paymentStatus : String,
        @SerializedName("installmentName") val installmentName : String,
        @SerializedName("installmentPercent") val installmentPercent : String,
    )

    data class MyOrdersDataOrdersProduct(
        @SerializedName("id") val id : String,
        @SerializedName("productType") val productType : MyOrdersDataOrdersProductType
    )

    data class MyOrdersDataOrdersProductType(
        @SerializedName("id") val id : String,
        @SerializedName("type") val type : String,
        @SerializedName("status") val status : String,
    )


    data class Notifications(
        @SerializedName("notification") val notification : List<NotificationData>,
    )

    data class NotificationData(
        @SerializedName("id") val id : String,
        @SerializedName("title") val title : String,
        @SerializedName("status") val status : String,
        @SerializedName("body") val body : String,
    )

}
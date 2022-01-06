package com.paylater.paylater.activities.ui.category

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.paylater.paylater.R
import com.paylater.paylater.adaptors.BrandFilterAdaptor
import com.paylater.paylater.adaptors.BrandsAdaptor
import com.paylater.paylater.utils.Api
import com.paylater.paylater.utils.Auth
import com.paylater.paylater.utils.Model
import com.paylater.paylater.utils.uncodedErrors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Query

class CategoryViewModel : ViewModel() {

    private val apiService by lazy {
        Api.create()
    }

    lateinit var brandAdaptor: BrandsAdaptor
    lateinit var brandFilterAdaptor: BrandFilterAdaptor

    lateinit var progressDialog: ProgressDialog


    //pull all brand
    fun brands(context: Context){

        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("loading please wait...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val call : Call<Model.GetBrand> = apiService.getBrands("Basic ${Auth().auth(context)}")
        call.enqueue(object : Callback<Model.GetBrand> {
            override fun onFailure(call: Call<Model.GetBrand>?, t: Throwable?) {
                progressDialog.dismiss()
                uncodedErrors(t as Throwable, context)
            }

            override fun onResponse(call: Call<Model.GetBrand>?, response: Response<Model.GetBrand>?) {

                progressDialog.dismiss()

                if (response!!.isSuccessful){
                    Log.d("data_returned", "${response.body()}")
                    successBrandsResponse(response.body()!!, context)
                }else{

                    val json = response.errorBody()!!.string()

                    if (response.code()>500){
                        val toast = Toast.makeText(context, "System error please try again later.", Toast.LENGTH_LONG)
                        toast.setGravity(Gravity.CENTER, 0, 0)
                        toast.show()
                        return
                    }

                    Log.d("data_error", "$json")

                    val toast = Toast.makeText(context, "Error occurred! try again later.", Toast.LENGTH_LONG)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()

                }
            }
        })
    }

    fun filterBrands(categoryId:String?, productId:String?, productTitle:String?, context: Context){

        //update the cat title
        val intent = Intent("load_brand_items")
        intent.putExtra("categoryId", categoryId)
        intent.putExtra("productId", productId)
        intent.putExtra("title", "$productTitle")
        intent.putExtra("updateTitleOnly", true)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)

        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("loading please wait...")
        progressDialog.setCancelable(false)
        progressDialog.show()


        val call : Call<Model.GetBrandFilter> = apiService.getBrandFilter("Basic ${Auth().auth(context)}","$categoryId", "$productId", "200")
        call.enqueue(object : Callback<Model.GetBrandFilter> {
            override fun onFailure(call: Call<Model.GetBrandFilter>?, t: Throwable?) {
                progressDialog.dismiss()
                uncodedErrors(t as Throwable, context)
            }

            override fun onResponse(call: Call<Model.GetBrandFilter>?, response: Response<Model.GetBrandFilter>?) {

                progressDialog.dismiss()

                if (response!!.isSuccessful){
                    Log.d("data_returned", "${response.body()}")
                    successBrandFilterResponse(response.body()!!, context)
                }else{

                    val json = response.errorBody()!!.string()

                    if (response.code()>500){
                        val toast = Toast.makeText(context, "System error please try again later.", Toast.LENGTH_LONG)
                        toast.setGravity(Gravity.CENTER, 0, 0)
                        toast.show()
                        return
                    }

                    Log.d("data_error", "$json")

                    val toast = Toast.makeText(context, "Error occurred! try again later.", Toast.LENGTH_LONG)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()

                }
            }
        })

    }


    fun successBrandsResponse(results: Model.GetBrand, context: Context) {

        if (results.data != null){
            filterBrands(results.data.productCategories[0].id, results.data.productTypes[0].id, results.data.productTypes[0].type, context)
            return brandAdaptor.setEvent(results.data.productTypes, results.data.productCategories)
        }

        val builder = AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
        builder.setMessage("Oops, something happened try again")
        builder.setPositiveButton(
            "Ok"
        ) {
                dialog, which -> dialog.cancel()
        }
        builder.setCancelable(false)
        builder.show()

    }


    fun successBrandFilterResponse(results: Model.GetBrandFilter, context: Context) {

        if (results.data != null){
            return brandFilterAdaptor.setEvent(results.data.availableProducts)
        }

        val builder = AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
        builder.setMessage("Oops, something happened try again")
        builder.setPositiveButton(
            "Ok"
        ) {
                dialog, which -> dialog.cancel()
        }
        builder.setCancelable(false)
        builder.show()

    }


}
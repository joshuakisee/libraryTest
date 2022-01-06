package com.paylater.paylater.activities.ui.home

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.paylater.paylater.R
import com.paylater.paylater.activities.CallBrands
import com.paylater.paylater.adaptors.ProductsAdaptor
import com.paylater.paylater.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeViewModel : ViewModel() {

    private val apiService by lazy {
        Api.create()
    }

    lateinit var productsAdaptor: ProductsAdaptor

    lateinit var progressDialog: ProgressDialog

    var callBrands: CallBrands? = null

    fun getBrands(context: Context, callBack:CallBrands){

        this.callBrands = callBack;

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
                    //successBrandFilterResponse(response.body()!!, context)
                    callBrands?.onActionSuccess(response.body()!!)
                }else{

                    callBrands?.onActionFailure("error occurred")

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

    //pull all products
    fun products(context: Context, maxAmount:String, minAmount:String, productId:String, keyword:String){

        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("loading please wait...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val call : Call<Model.GetProduct> = apiService.getProducts("Basic ${Auth().auth(context)}","${LibSession(context).retrieveLibSession("score")}", maxAmount, minAmount, productId, keyword)
        call.enqueue(object : Callback<Model.GetProduct> {
            override fun onFailure(call: Call<Model.GetProduct>?, t: Throwable?) {
                progressDialog.dismiss()
                uncodedErrors(t as Throwable, context)
            }

            override fun onResponse(call: Call<Model.GetProduct>?, response: Response<Model.GetProduct>?) {

                progressDialog.dismiss()

                if (response!!.isSuccessful){
                    Log.d("data_returned", "${response.body()}")
                    successProductsResponse(response.body()!!, context)
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

    fun successProductsResponse(results: Model.GetProduct, context: Context) {

        if (results.data != null){
            return productsAdaptor.setEvent(results.data.availableProducts)
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
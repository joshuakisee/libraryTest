package com.paylater.paylater.activities.ui.orders

import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import com.paylater.paylater.R
import com.paylater.paylater.adaptors.MyOdersAdaptor
import com.paylater.paylater.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersViewModel : ViewModel() {

    lateinit var myOdersAdaptor: MyOdersAdaptor

    private val apiService by lazy {
        Api.create()
    }

    lateinit var progressDialog: ProgressDialog

    fun myOrders(context: Context, phoneNumber:String){

        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("loading please wait...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val call : Call<Model.MyOrders> = apiService.getMyOrders("Basic ${Auth().auth(context)}","$phoneNumber")
        call.enqueue(object : Callback<Model.MyOrders> {
            override fun onFailure(call: Call<Model.MyOrders>?, t: Throwable?) {
                progressDialog.dismiss()
                uncodedErrors(t as Throwable, context)
            }

            override fun onResponse(call: Call<Model.MyOrders>?, response: Response<Model.MyOrders>?) {

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

    fun successProductsResponse(results: Model.MyOrders, context: Context) {

        if (results.data != null){
            if(results.data.orders.isEmpty()){
                val builder = AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
                builder.setMessage("No orders found. Place your order today.")
                builder.setPositiveButton(
                    "Ok"
                ) {
                        dialog, which -> dialog.cancel()

                }
                builder.setCancelable(false)
                builder.show()
                return
            }
            return myOdersAdaptor.setEvent(results.data.orders)
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
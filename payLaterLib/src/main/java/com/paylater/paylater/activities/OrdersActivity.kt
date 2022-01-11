package com.paylater.paylater.activities

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.paylater.paylater.R
import com.paylater.paylater.adaptors.MyOdersAdaptor
import com.paylater.paylater.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersActivity : AppCompatActivity() {

    lateinit var myOdersAdaptor: MyOdersAdaptor

    private val apiService by lazy {
        Api.create()
    }

    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        supportActionBar?.title = Html.fromHtml("<font color='#702473'>My Orders</font>")


        val odersRecycler: RecyclerView = findViewById(R.id.my_orders_list)

        odersRecycler.apply {
            layoutManager = GridLayoutManager(this@OrdersActivity, 1)
            myOdersAdaptor = MyOdersAdaptor(this@OrdersActivity)
            adapter = myOdersAdaptor
        }

        myOrders(this, "${LibSession(this).retrieveLibSession("phone_number")}")

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

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
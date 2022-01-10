package com.paylater.paylater.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.FirebaseApp
import com.paylater.paylater.R

class OrdersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        var fTokenInit = FirebaseApp.initializeApp(this)

        Log.d("fTokenInitOders", "$fTokenInit")
        Log.d("fAppNameOders", "${FirebaseApp.DEFAULT_APP_NAME}")
    }
}
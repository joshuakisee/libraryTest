package com.paylater.paylater.activities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.material.button.MaterialButton
import com.paylater.paylater.R
import com.paylater.paylater.databinding.ActivityPurchaseBinding
import com.paylater.paylater.fragment.PlaceOrder
import com.paylater.paylater.utils.LibSession
import com.squareup.picasso.Picasso

class PurchaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPurchaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        supportActionBar?.title = Html.fromHtml("<font color='#702473'>Purchase</font>")

        binding = ActivityPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get extras
        var data = intent.getSerializableExtra("data") as (HashMap<*, *>)

        Log.d("data_passwed", "${data["price"]}")

        var device_filter_image = binding.root.findViewById<ImageView>(R.id.device_purchase_image)
        var device_purchase_description = binding.root.findViewById<TextView>(R.id.device_purchase_description)
        var device_purchase_deposit = binding.root.findViewById<TextView>(R.id.device_purchase_deposit)
        var device_purchase_cost = binding.root.findViewById<TextView>(R.id.device_purchase_cost)
        var key_payment_plan = binding.root.findViewById<TextView>(R.id.key_payment_plan)
        var key_features = binding.root.findViewById<TextView>(R.id.key_features)
        var device_purchase_button = binding.root.findViewById<MaterialButton>(R.id.device_purchase_button)


        //for (i in 0 until ])

        Picasso.get()
            .load("${data["image"]}")
            .error( R.drawable.ic_baseline_image_24)
            .placeholder( R.drawable.loading)
            .into(device_filter_image)

        device_purchase_description.text = "${data["description"]}"
        device_purchase_deposit.text = "KES. ${data["deposit"]}"
        device_purchase_cost.text = "KES. ${data["price"]}"
        key_features.text = "${data["keyFeatures"]}"
        key_payment_plan.text = "${data["orderInstallments"]}"

        device_purchase_button.setOnClickListener{
            var bundle = Bundle()
            bundle.putString("deposit", "${data["deposit"]}")
            bundle.putString("device_id", "${data["id"]}")
            val ft = (this).supportFragmentManager
            val bottomSheetFragment = PlaceOrder()
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.show(ft, bottomSheetFragment.tag)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
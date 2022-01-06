package com.paylater.paylater.activities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import com.paylater.paylater.R

class TrackerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracker)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        supportActionBar?.title = Html.fromHtml("<font color='#702473'>Track Item</font>")

        var status = intent.getStringExtra("status")

        var fulfilledSuccess = findViewById<View>(R.id.fulfilled_success)
        var fulfilledFailed = findViewById<View>(R.id.fulfilled_failed)
        var shippedSuccessTick = findViewById<ImageView>(R.id.shipped_success_tick)
        var shippedFailedTick = findViewById<ImageView>(R.id.shipped_failed_tick)
        var shippedSuccess = findViewById<View>(R.id.shipped_success)
        var shippedFailed = findViewById<View>(R.id.shipped_failed)
        var transitSuccessTick = findViewById<ImageView>(R.id.transit_success_tick)
        var transitFailedTick = findViewById<ImageView>(R.id.transit_failed_tick)
        var transitSuccess = findViewById<View>(R.id.transit_success)
        var transitFailed = findViewById<View>(R.id.transit_failed)
        var deliveredSuccessTick = findViewById<ImageView>(R.id.delivered_success_tick)
        var deliveredFailedTick = findViewById<ImageView>(R.id.delivered_failed_tick)
        var deliveredSuccess = findViewById<View>(R.id.delivered_success)
        var deliveredFailed = findViewById<View>(R.id.delivered_failed)
        var successTick = findViewById<ImageView>(R.id.success_tick)
        var failedTick = findViewById<ImageView>(R.id.failed_tick)

        if(status == "awaiting_fullfillment"){
            fulfilledSuccess.visibility = View.VISIBLE
            fulfilledFailed.visibility = View.GONE
            shippedSuccessTick.visibility = View.VISIBLE
            shippedFailedTick.visibility = View.GONE
        }

        if(status == "shipped"){
            fulfilledSuccess.visibility = View.VISIBLE
            fulfilledFailed.visibility = View.GONE
            shippedSuccessTick.visibility = View.VISIBLE
            shippedFailedTick.visibility = View.GONE


            shippedSuccess.visibility = View.VISIBLE
            shippedFailed.visibility = View.GONE
            transitSuccessTick.visibility = View.VISIBLE
            transitFailedTick.visibility = View.GONE
        }

        if(status == "in-transit"){
            fulfilledSuccess.visibility = View.VISIBLE
            fulfilledFailed.visibility = View.GONE
            shippedSuccessTick.visibility = View.VISIBLE
            shippedFailedTick.visibility = View.GONE


            shippedSuccess.visibility = View.VISIBLE
            shippedFailed.visibility = View.GONE
            transitSuccessTick.visibility = View.VISIBLE
            transitFailedTick.visibility = View.GONE

            transitSuccess.visibility = View.VISIBLE
            transitFailed.visibility = View.GONE
            deliveredSuccessTick.visibility = View.VISIBLE
            deliveredFailedTick.visibility = View.GONE
        }

        if(status == "delivered"){
            fulfilledSuccess.visibility = View.VISIBLE
            fulfilledFailed.visibility = View.GONE
            shippedSuccessTick.visibility = View.VISIBLE
            shippedFailedTick.visibility = View.GONE


            shippedSuccess.visibility = View.VISIBLE
            shippedFailed.visibility = View.GONE
            transitSuccessTick.visibility = View.VISIBLE
            transitFailedTick.visibility = View.GONE

            transitSuccess.visibility = View.VISIBLE
            transitFailed.visibility = View.GONE
            deliveredSuccessTick.visibility = View.VISIBLE
            deliveredFailedTick.visibility = View.GONE


            deliveredSuccess.visibility = View.VISIBLE
            deliveredFailed.visibility = View.GONE
            successTick.visibility = View.VISIBLE
            failedTick.visibility = View.GONE
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
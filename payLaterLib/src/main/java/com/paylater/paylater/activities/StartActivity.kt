package com.paylater.paylater.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.Toast
import com.paylater.paylater.R
import com.paylater.paylater.utils.LibSession

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        supportActionBar?.title = Html.fromHtml("<font color='#702473'>Pay Later</font>")

        var clientName = intent.getStringExtra("fullName")
        var clientPhone = intent.getStringExtra("phoneNumber")
        var clientScore = intent.getStringExtra("score")
        var publicKey = intent.getStringExtra("publicKey")
        var privateKey = intent.getStringExtra("privateKey")

        if (clientName != null) {
            if (clientName.isEmpty()){
                Toast.makeText(this, "Please provide name", Toast.LENGTH_LONG).show()
                finish()
                return
            }
        }else{
            Toast.makeText(this, "Please provide name", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if (clientPhone != null) {
            if (clientPhone.isEmpty()){
                Toast.makeText(this, "Please provide phone number", Toast.LENGTH_LONG).show()
                finish()
                return
            }
        }else{
            Toast.makeText(this, "Please provide phone number", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if (clientScore != null) {
            if (clientScore.isEmpty()){
                Toast.makeText(this, "Please provide credit score", Toast.LENGTH_LONG).show()
                finish()
                return
            }
        }else{
            Toast.makeText(this, "Please provide credit score", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if (publicKey != null) {
            if (publicKey.isEmpty()){
                Toast.makeText(this, "Please provide public key", Toast.LENGTH_LONG).show()
                finish()
                return
            }
        }else{
            Toast.makeText(this, "Please provide public key", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if (privateKey != null) {
            if (privateKey.isEmpty()){
                Toast.makeText(this, "Please provide private key", Toast.LENGTH_LONG).show()
                finish()
                return
            }
        }else{
            Toast.makeText(this, "Please provide private key", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        LibSession(this).profileFullName("$clientName")
        LibSession(this).profilePhone("$clientPhone")
        LibSession(this).profileScore("$clientScore")
        LibSession(this).publicKey("$publicKey")
        LibSession(this).privateKey("$privateKey")


        findViewById<Button>(R.id.start).setOnClickListener{
            var intent = Intent(this, LibMainActivity::class.java)
            startActivity(intent)
        }

    }
}
package com.paylater.paylater.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.paylater.paylater.databinding.ActivityMainBinding
import android.graphics.drawable.ColorDrawable
import android.text.Html
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.paylater.paylater.R
import com.paylater.paylater.utils.*
import com.paylater.paylater.utils.DB.DBHelper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.firebase.messaging.FirebaseMessaging


class LibMainActivity : AppCompatActivity() {

    private val apiService by lazy {
        Api.create()
    }

private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        supportActionBar?.title = Html.fromHtml("<font color='#702473'>Pay Later</font>")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_category, R.id.navigation_orders
        ))
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        FirebaseApp.initializeApp(this)

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<*> ->
            if (task.isSuccessful) {
                val token = task.result
                createCustomerProfile(token.toString(), "${LibSession(this).retrieveLibSession("full_name")}", "${LibSession(this).retrieveLibSession("phone_number")}", "${LibSession(this).retrieveLibSession("score")}")
            }else{
                createCustomerProfile("", "${LibSession(this).retrieveLibSession("full_name")}", "${LibSession(this).retrieveLibSession("phone_number")}", "${LibSession(this).retrieveLibSession("score")}")
            }
        }

        getAllNotifications()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun getAllNotifications(){
        var notification = DBHelper(this, null).getAllNotifications() as ArrayList<*>


        Log.d("notifications", "${notification.size}")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_notification -> {
                val intent = Intent(this, NotificationActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_help -> {
                val intent = Intent(this, HelpActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun createCustomerProfile(fireBase:String, name:String, phone:String, score:String){

        var json = JSONObject()
        json.put("firebaseToken", "$fireBase")
        json.put("fullNames", "$name")
        json.put("phoneNumber", "$phone")
        json.put("score", "$score")

        val requestBody : RequestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json.toString())

        val call : Call<Model.CreateProfile> = apiService.createProfile("Basic ${Auth().auth(this)}", requestBody)
        call.enqueue(object : Callback<Model.CreateProfile> {
            override fun onFailure(call: Call<Model.CreateProfile>?, t: Throwable?) {
                LibSession(this@LibMainActivity).profileIsCreated(false)
            }

            override fun onResponse(call: Call<Model.CreateProfile>?, response: Response<Model.CreateProfile>?) {

                if (response!!.isSuccessful){
                    if(response.body()!!.status == "00"){
                        Log.d("data_returned", "${response.body()}")
                        LibSession(this@LibMainActivity).profileIsCreated(true)
                    }else{
                        Log.d("data_returned", "${response.body()}")
                        LibSession(this@LibMainActivity).profileIsCreated(false)
                    }
                }else{
                    LibSession(this@LibMainActivity).profileIsCreated(false)
                }
            }
        })
    }

    private fun validateData(
        clientName: String?,
        clientPhone: String?,
        clientScore: String?,
        publicKey: String?,
        privateKey: String?
    ) {
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

    }
}
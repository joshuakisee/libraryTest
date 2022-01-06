package com.paylater.paylater.utils.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.paylater.paylater.R
import com.paylater.paylater.activities.MainActivity
import com.paylater.paylater.utils.Api
import com.paylater.paylater.utils.Auth
import com.paylater.paylater.utils.DB.DBHelper
import com.paylater.paylater.utils.LibSession
import com.paylater.paylater.utils.Model
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFirebaseMessagingService : FirebaseMessagingService() {

    var title =  "Crystobol-PayLater"
    var msg =  ""
    var status_code =  ""

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d("message_via_token", "$remoteMessage")

        var CHANNEL_ID = getString(R.string.CHANNEL_ID)

        createNotificationChannel(CHANNEL_ID)

        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {
            Log.d(ContentValues.TAG, "Message data payload: " + remoteMessage.data)

            msg = "" + remoteMessage.data["body"]
            title = "" + remoteMessage.data["title"]

        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(ContentValues.TAG, "Message Notification Body: " + remoteMessage.notification!!.body!!)
            msg = remoteMessage.notification!!.body.toString()
        }

        //save the notifications
        if(msg.isNotEmpty()){
            DBHelper(this, null).insertNotifications(title, msg)
        }

        notification(CHANNEL_ID, msg, "Crystobol-PayLater")
    }

    override fun onNewToken(token: String) {
        updateCustomerProfile(token)
    }

    private fun notification(CHANNEL_ID:String, msg:String, title:String){

        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent,  PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.followmetalk)
            .setContentTitle(title)
            .setContentText("$msg")
            .setTicker(title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(this)

        notificationManager.notify(0 /* ID of notification */, builder.build())

    }

    private fun createNotificationChannel(CHANNEL_ID:String) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private val apiService by lazy {
        Api.create()
    }

    private fun updateCustomerProfile(fireBase:String){

        var json = JSONObject()
        json.put("firebaseToken", "$fireBase")
        json.put("fullNames", "${LibSession(this).retrieveLibSession("full_name")}")
        json.put("phoneNumber", "${LibSession(this).retrieveLibSession("phone_number")}")
        json.put("score", "${LibSession(this).retrieveLibSession("score")}")

        val requestBody : RequestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json.toString())

        val call : Call<Model.CreateProfile> = apiService.createProfile("Basic ${Auth().auth(this)}", requestBody)
        call.enqueue(object : Callback<Model.CreateProfile> {
            override fun onFailure(call: Call<Model.CreateProfile>?, t: Throwable?) {
                //do nothing
            }

            override fun onResponse(call: Call<Model.CreateProfile>?, response: Response<Model.CreateProfile>?) {
                //do nothing
            }
        })
    }

}
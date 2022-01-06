package com.paylater.paylater.activities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.paylater.paylater.R
import com.paylater.paylater.adaptors.NotificationAdaptor
import com.paylater.paylater.utils.DB.DBHelper
import com.paylater.paylater.utils.Model

class NotificationActivity : AppCompatActivity() {

    lateinit var notificationAdaptor: NotificationAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        supportActionBar?.title = Html.fromHtml("<font color='#702473'>Notifications</font>")

        setContentView(R.layout.activity_notification)


        var notification = DBHelper(this, null).getAllNotifications() as ArrayList<*>


        val notificationList: RecyclerView = findViewById(R.id.notification_list)

        notificationList.apply {
            layoutManager = GridLayoutManager(this@NotificationActivity, 1)
            notificationAdaptor = NotificationAdaptor(this@NotificationActivity)
            adapter = notificationAdaptor
        }


        var gson = Gson()

        if(notification.size>0){
            var notify = HashMap<String, Any>()
            notify["notification"] = notification
            var testModel = gson.fromJson("$notify", Model.Notifications::class.java)

            notificationAdaptor.setEvent(testModel.notification)
        }

    }

    fun makeAsReadNotification(id:String){
        DBHelper(this, null).makeNotificationRead(id)
        finish()
        startActivity(intent);
    }

    fun deleteNotification(id:String){
        DBHelper(this, null).deleteNotification(id)
        finish()
        startActivity(intent);
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
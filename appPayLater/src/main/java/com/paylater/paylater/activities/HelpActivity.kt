package com.paylater.paylater.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.LinearLayout
import com.paylater.paylater.R
import com.paylater.paylater.utils.LibSession

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        supportActionBar?.title = Html.fromHtml("<font color='#702473'>Help</font>")


        findViewById<LinearLayout>(R.id.help_call).setOnClickListener{
            if (findViewById<LinearLayout>(R.id.call_list).isShown)
                findViewById<LinearLayout>(R.id.call_list).visibility = View.GONE
            else
                findViewById<LinearLayout>(R.id.call_list).visibility = View.VISIBLE
        }

        findViewById<LinearLayout>(R.id.call_list_num_one).setOnClickListener{

            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel: "+getString(R.string.customer_support_phone_one))
            startActivity(intent)

        }

        findViewById<LinearLayout>(R.id.call_list_num_two).setOnClickListener{

            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel: "+getString(R.string.customer_support_phone_two))
            startActivity(intent)

        }

        findViewById<LinearLayout>(R.id.help_email).setOnClickListener{

            //get the username
            val userName: String? = if (null ==  LibSession(this).retrieveLibSession("full_name") || LibSession(this).retrieveLibSession("full_name") == "")""
            else LibSession(this).retrieveLibSession("full_name") as String

            val phone: String? = if (null == LibSession(this).retrieveLibSession("phone_number") || LibSession(this).retrieveLibSession("phone_number") == "")""
            else LibSession(this).retrieveLibSession("phone_number") as String


            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", getString(R.string.support_email), null))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "PayLaterOnCrystobol App Support ($userName $phone)")
            startActivity(Intent.createChooser(emailIntent, getString(R.string.chooser_email_title)))
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
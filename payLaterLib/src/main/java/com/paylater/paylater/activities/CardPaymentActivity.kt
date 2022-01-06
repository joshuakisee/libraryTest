package com.paylater.paylater.activities

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.flutterwave.raveandroid.RavePayActivity
import com.flutterwave.raveandroid.RaveUiManager
import com.flutterwave.raveandroid.rave_java_commons.Meta
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.paylater.paylater.R
import org.json.JSONObject

class CardPaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_payment)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        supportActionBar?.title = Html.fromHtml("<font color='#702473'>Pay Later</font>")

        var orderId = intent.getStringExtra("orderId")
        var channelSelect = intent.getStringExtra("channelSelect")
        var payPhoneNumber = intent.getStringExtra("payPhoneNumber")
        var amountToPay = intent.getStringExtra("amount")

        var fName = findViewById<TextInputEditText>(R.id.f_name)
        var lName = findViewById<TextInputEditText>(R.id.l_name)
        var phone = findViewById<TextInputEditText>(R.id.card_phone_number)
        var email = findViewById<TextInputEditText>(R.id.pay_email)
        var payButton = findViewById<MaterialButton>(R.id.card_pay_button)

        val ai: ApplicationInfo = applicationContext.packageManager
            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
        val publicKey = ai.metaData["public_key"]
        val encryptionKey = ai.metaData["encryption_key"]

        phone.setText("$payPhoneNumber")

        payButton.setOnClickListener{
            if(fName.text.toString().trim().isEmpty()){
                fName.error = "required"
                fName.requestFocus()
                return@setOnClickListener
            }
            if(lName.text.toString().trim().isEmpty()){
                lName.error = "required"
                lName.requestFocus()
                return@setOnClickListener
            }
            if(phone.text.toString().trim().isEmpty()){
                phone.error = "required"
                phone.requestFocus()
                return@setOnClickListener
            }
            if(email.text.toString().trim().isEmpty()){
                email.error = "required"
                email.requestFocus()
                return@setOnClickListener
            }

            cardInit(
                orderId,
                channelSelect,
                payPhoneNumber,
                fName.text.toString().trim(),
                lName.text.toString().trim(),
                email.text.toString().trim(),
                amountToPay,
                publicKey.toString(),
                encryptionKey.toString(),
            )
        }
    }

    fun cardInit(
        orderId: String?,
        channelSelect: String?,
        payPhoneNumber: String?,
        fName: String,
        lName: String,
        email: String,
        amount:String?,
        publicKey:String,
        encryptionKey:String
    ){
        var data = JSONObject()
        data.put("orderId", "$orderId")
        data.put("paymentMethod", "$channelSelect")
        data.put("phoneNumber", "$payPhoneNumber")

        var list = ArrayList<JSONObject>()
        list.add(data)

        Log.d("meta_data", "${list.toList() as MutableList<Meta>?}")

        RaveUiManager(this)
            .setAmount(amount!!.toDouble())
            .setCurrency("KES")
            .setEmail("$email")
            .setfName("$fName")
            .setlName("$lName")
            .setNarration("")
            .setPublicKey("$publicKey".trim())
            .setEncryptionKey("$encryptionKey".trim())
            .setTxRef("$orderId")
            .setPhoneNumber("$payPhoneNumber", false)
            .acceptAccountPayments(true)
            .acceptCardPayments(true)
            .acceptMpesaPayments(false)
            .acceptAchPayments(false)
            .acceptGHMobileMoneyPayments(false)
            .acceptUgMobileMoneyPayments(false)
            .acceptZmMobileMoneyPayments(false)
            .acceptRwfMobileMoneyPayments(false)
            .acceptSaBankPayments(false)
            .acceptUkPayments(false)
            .acceptBankTransferPayments(true)
            .acceptUssdPayments(false)
            .acceptBarterPayments(false)
            .acceptFrancMobileMoneyPayments(false, "")
            .allowSaveCardFeature(false)
            .onStagingEnv(true)
            .setMeta(list.toList() as MutableList<Meta>?)
            .isPreAuth(false)
            .shouldDisplayFee(false)
            .showStagingLabel(true)
            .withTheme(R.style.MyCustomTheme)
            .initialize()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            val message = data.getStringExtra("response")
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                val builder = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                builder.setMessage("Success\nPayment received for processing.")
                builder.setPositiveButton(
                    "Ok"
                ) {
                        dialog, which -> dialog.cancel()
                    finish()
                }
                builder.setCancelable(false)
                builder.show()
            } else if (resultCode == RavePayActivity.RESULT_ERROR) {
                val builder = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                builder.setMessage("Error\nUnable to complete the transaction. Please try again later.")
                builder.setPositiveButton(
                    "Ok"
                ) {
                        dialog, which -> dialog.cancel()
                }
                builder.setCancelable(false)
                builder.show()
            } else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(this, "request canceled", Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
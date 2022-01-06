package com.paylater.paylater.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.paylater.paylater.R
import com.paylater.paylater.utils.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.content.Intent
import com.paylater.paylater.activities.CardPaymentActivity


class MakePayment : BottomSheetDialogFragment() {

    lateinit var progressDialog: ProgressDialog

    private val apiService by lazy {
        Api.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view = inflater.inflate(R.layout.layout_pay_order, container, false)

        var channelSelect = ""

        val amountToPay = arguments?.getString("amount")
        val orderId = arguments?.getString("order_id")

        if(orderId.toString().trim().isEmpty() || amountToPay.toString().trim().isEmpty()){
            Toast.makeText(activity, "System error! please try again", Toast.LENGTH_LONG).show()
            dismiss()
            return view
        }

        var phone = LibSession(requireActivity()).retrieveLibSession("phone_number")

        var mpesa = view.findViewById<CardView>(R.id.mpesa)
        var mpesaSelected = view.findViewById<ImageView>(R.id.mpesa_selected)
        var visa = view.findViewById<CardView>(R.id.visa)
        var cardSelected = view.findViewById<ImageView>(R.id.card_selected)
        var orderAmount = view.findViewById<TextView>(R.id.order_amount)
        var closeConfirmOrderPopup = view.findViewById<ImageView>(R.id.close_confirm_order_popup)
        var phoneNumberToPay = view.findViewById<TextView>(R.id.phone_number_to_pay)
        var phoneNumberEdit = view.findViewById<LinearLayout>(R.id.phone_number_edit)
        var editPhoneNumber = view.findViewById<ImageView>(R.id.edit_phone_number)
        var payPhoneHouse = view.findViewById<TextInputLayout>(R.id.pay_phone_house)
        var payPhoneNumber = view.findViewById<TextInputEditText>(R.id.pay_phone_number)
        var payButton = view.findViewById<MaterialButton>(R.id.pay_button)

        closeConfirmOrderPopup.setOnClickListener{
            dismiss()
        }

        editPhoneNumber.setOnClickListener{
            payPhoneHouse.visibility = View.VISIBLE
            phoneNumberEdit.visibility = View.GONE
        }

        if(phone.toString().trim().isEmpty()){
            editPhoneNumber.performClick()
        }

        orderAmount.text = "Order Amount : KES. $amountToPay"
        phoneNumberToPay.text = "$phone"
        payPhoneNumber.setText("$phone")

        mpesa.setOnClickListener{
            mpesaSelected.visibility = View.VISIBLE
            cardSelected.visibility = View.INVISIBLE
            channelSelect = "MPESA"
            payButton.text = "Mpesa Payment"
        }

        visa.setOnClickListener{
            mpesaSelected.visibility = View.INVISIBLE
            cardSelected.visibility = View.VISIBLE
            channelSelect = "CARD"
            payButton.text = "Card Payment"
        }

        payButton.setOnClickListener{
            if(channelSelect == ""){
                Toast.makeText(activity, "select payment channel", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(payPhoneNumber.text.toString().trim().isEmpty()){
                editPhoneNumber.performClick()
                payPhoneNumber.error = "required"
                payPhoneNumber.requestFocus()
                return@setOnClickListener
            }

            if(channelSelect == "CARD"){
                makePayment(orderId, "CARD", payPhoneNumber.text.toString().trim(),
                    amountToPay.toString()
                )
                return@setOnClickListener
            }

            makePayment(orderId, channelSelect, payPhoneNumber.text.toString().trim(),
                amountToPay.toString()
            )
        }

        return view
    }

    fun makePayment(orderId: String?, paymentMethod:String, phoneNumber:String, amount:String){

        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("loading please wait...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        var json = JSONObject()
        json.put("orderId", "$orderId")
        json.put("paymentMethod", "$paymentMethod")
        json.put("phoneNumber", "$phoneNumber")

        val requestBody : RequestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json.toString())

        val call : Call<Model.PayNow> = apiService.payNow("Basic ${Auth().auth(requireActivity())}", requestBody)
        call.enqueue(object : Callback<Model.PayNow> {
            override fun onFailure(call: Call<Model.PayNow>?, t: Throwable?) {
                progressDialog.dismiss()
                uncodedErrors(t as Throwable, activity!!)
            }

            override fun onResponse(call: Call<Model.PayNow>?, response: Response<Model.PayNow>?) {

                progressDialog.dismiss()

                if (response!!.isSuccessful){
                    Log.d("data_returned", "${response.body()}")
                   if(response.body()!!.status == "00"){
                       if(paymentMethod == "CARD"){
                           val intent = Intent(activity, CardPaymentActivity::class.java)
                           intent.putExtra("orderId", "${response.body()!!.data.reference}")
                           intent.putExtra("channelSelect", "$paymentMethod")
                           intent.putExtra("payPhoneNumber", "$phoneNumber")
                           intent.putExtra("amount", amount)
                           requireActivity().startActivity(intent)
                           dismiss()
                           return
                       }
                       val builder = AlertDialog.Builder(activity!!, R.style.AppCompatAlertDialogStyle)
                       builder.setMessage("${response.body()!!.message}")
                       builder.setPositiveButton(
                           "Ok"
                       ) {
                               dialog, which -> dialog.cancel()
                           dismiss()
                       }
                       builder.setCancelable(false)
                       builder.show()
                   }else{
                       val builder = AlertDialog.Builder(activity!!, R.style.AppCompatAlertDialogStyle)
                       builder.setMessage("${response.body()!!.message}")
                       builder.setPositiveButton(
                           "Ok"
                       ) {
                               dialog, which -> dialog.cancel()
                       }
                       builder.setCancelable(false)
                       builder.show()
                   }
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
}
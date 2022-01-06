package com.paylater.paylater.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.paylater.paylater.R
import com.paylater.paylater.utils.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceOrder : BottomSheetDialogFragment() {

    private val apiService by lazy {
        Api.create()
    }

    lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view = inflater.inflate(R.layout.layout_place_order, container, false)

        val depositAmount = arguments?.getString("deposit")
        val deviceId = arguments?.getString("device_id")

        if(deviceId.toString().isEmpty() || depositAmount.toString().isEmpty()){
            Toast.makeText(activity, "error occurred! please try again", Toast.LENGTH_LONG).show()
            dismiss()
            return view
        }

        var profileIsCreated = LibSession(requireActivity()).retrieveLibSession("profile_is_created") as Boolean
        var phone = LibSession(requireActivity()).retrieveLibSession("phone_number")

        val closeOrderPopup = view.findViewById<ImageView>(R.id.close_order_popup)
        val full_name = view.findViewById<TextInputLayout>(R.id.full_name)
        val profileFullName = view.findViewById<TextInputEditText>(R.id.profile_full_name)
        val phone_number = view.findViewById<TextInputLayout>(R.id.phone_number)
        val profilePhoneNumber = view.findViewById<TextInputEditText>(R.id.profile_phone_number)
        val orderDeposit = view.findViewById<TextInputEditText>(R.id.order_deposit)
        val orderButton = view.findViewById<MaterialButton>(R.id.order_button)

        //check if profile has been created
        if(!profileIsCreated){
            full_name.visibility = View.VISIBLE
            phone_number.visibility = View.VISIBLE
        }else{
            full_name.visibility = View.GONE
            phone_number.visibility = View.GONE
        }

        profilePhoneNumber.setText("$phone")
        orderDeposit.setText("${depositAmount?.floatToInt()}")

        orderButton.setOnClickListener{
            if(profilePhoneNumber.text.toString().trim().isEmpty()){
                profilePhoneNumber.error = "required"
                profilePhoneNumber.requestFocus()
                return@setOnClickListener
            }
            if(orderDeposit.text.toString().trim().isEmpty()){
                orderDeposit.error = "required"
                orderDeposit.requestFocus()
                return@setOnClickListener
            }
            if(Integer.parseInt(orderDeposit.text.toString().trim()) < Integer.parseInt("${depositAmount?.floatToInt()}")){
                orderDeposit.error = "min amount is $depositAmount"
                orderDeposit.requestFocus()
                return@setOnClickListener
            }

            if(profileIsCreated){
                placeOrder(orderDeposit.text.toString().trim(), profilePhoneNumber.text.toString().trim(), deviceId)
            }else{
                if(profileFullName.text.toString().trim().isEmpty()){
                    profileFullName.error = "required"
                    profileFullName.requestFocus()
                    return@setOnClickListener
                }
                createProfile(profileFullName.text.toString().trim(),
                    profilePhoneNumber.text.toString().trim(),
                    orderDeposit.text.toString().trim(), deviceId)
            }
        }

        closeOrderPopup.setOnClickListener{
            dismiss()
        }

        return view
    }

    fun String.floatToInt(): Int {
        return this.toFloat().toInt()
    }

    fun createProfile(name:String, phone:String, depositAmount: String, productId: String?){
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("loading please wait...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        var json = JSONObject()
        json.put("firebaseToken", "")
        json.put("fullNames", "$name")
        json.put("phoneNumber", "$phone")
        json.put("score", "${LibSession(requireActivity()).retrieveLibSession("score")}")

        LibSession(requireActivity()).profilePhone("$phone")
        LibSession(requireActivity()).profileFullName("$name")

        val requestBody : RequestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json.toString())

        val call : Call<Model.CreateProfile> = apiService.createProfile("Basic ${Auth().auth(requireActivity())}", requestBody)
        call.enqueue(object : Callback<Model.CreateProfile> {
            override fun onFailure(call: Call<Model.CreateProfile>?, t: Throwable?) {
                LibSession(requireActivity()).profileIsCreated(false)
                progressDialog.dismiss()
                uncodedErrors(t as Throwable, activity!!)
            }

            override fun onResponse(call: Call<Model.CreateProfile>?, response: Response<Model.CreateProfile>?) {

                progressDialog.dismiss()

                if (response!!.isSuccessful){

                        Log.d("data_returned", "${response.body()}")
                        LibSession(requireActivity()).profileIsCreated(true)
                        placeOrder(depositAmount, phone, productId)

                }else{
                    LibSession(requireActivity()).profileIsCreated(false)

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

    fun placeOrder(depositAmount:String, phoneNumber:String, productId: String?){

        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("loading please wait...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        var json = JSONObject()
        json.put("depositAmount", "$depositAmount")
        json.put("phoneNumber", "$phoneNumber")
        json.put("productId", "$productId")

        val requestBody : RequestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json.toString())

        val call : Call<Model.PlaceOrder> = apiService.placeOrder("Basic ${Auth().auth(requireActivity())}", requestBody)
        call.enqueue(object : Callback<Model.PlaceOrder> {
            override fun onFailure(call: Call<Model.PlaceOrder>?, t: Throwable?) {
                progressDialog.dismiss()
                uncodedErrors(t as Throwable, activity!!)
            }

            override fun onResponse(call: Call<Model.PlaceOrder>?, response: Response<Model.PlaceOrder>?) {

                progressDialog.dismiss()

                if (response!!.isSuccessful){
                    Log.d("data_returned", "${response.body()}")

                    if(response.body()!!.status == "00"){
                        var bundle = Bundle()
                        bundle.putString("amount", "${response.body()!!.data.order.depositPaid}")
                        bundle.putString("order_id", "${response.body()!!.data.order.id}")
                        val ft = (activity as FragmentActivity).supportFragmentManager
                        val bottomSheetFragment = MakePayment()
                        bottomSheetFragment.arguments = bundle
                        bottomSheetFragment.show(ft, bottomSheetFragment.tag)
                        dismiss()
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
                    //go to choose payment option
                    //dismiss
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
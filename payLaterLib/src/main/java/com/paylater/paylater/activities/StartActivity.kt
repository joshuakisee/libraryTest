package com.paylater.paylater.activities

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PointF.length
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.paylater.paylater.R
import com.paylater.paylater.adaptors.ProductsAdaptor
import com.paylater.paylater.utils.*
import com.paylater.paylater.utils.DB.DBHelper
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class StartActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val apiService by lazy {
        Api.create()
    }

    lateinit var productsAdaptor: ProductsAdaptor

    lateinit var progressDialog: ProgressDialog

    var callBrands: CallBrands? = null

    private lateinit var parentContainer: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        supportActionBar?.title = Html.fromHtml("<font color='#702473'>Pay Later</font>")

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

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

        val searchByKeyword: EditText = findViewById(R.id.search_by_keyword)
        val searchInitiator: ImageView = findViewById(R.id.search_initiator)
        val productRecycler: RecyclerView = findViewById(R.id.products_list)
        parentContainer = findViewById(R.id.parent_container)
        val filter: LinearLayout = findViewById(R.id.filter)
        val filterNow: LinearLayout = findViewById(R.id.filter_now)

        FirebaseDatabase.getInstance();

        if(!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);

            Toast.makeText(this, "firebase was not initialized", Toast.LENGTH_LONG).show()
            Log.d("firebaseTokenNot", "firebase was not initialized")

        }


//        FirebaseApp.initializeApp(this);
//        FirebaseApp.initializeApp(this)
//        if(FirebaseApp.getApps(this).isEmpty()){
//            FirebaseApp.initializeApp(this)
//            Toast.makeText(this, "firebase was not initialized", Toast.LENGTH_LONG).show()
//            Log.d("firebaseTokenNot", "firebase was not initialized")
//        }

        if(FirebaseApp.getApps(this).isNotEmpty()){
            getFirebaseToken()
        }


        getAllNotifications()

        productRecycler.apply {
            layoutManager = GridLayoutManager(this@StartActivity, 1)
            productsAdaptor = ProductsAdaptor(this@StartActivity)
            adapter = productsAdaptor
        }

        products(this, "", "", "", "")

        searchInitiator.setOnClickListener{
            if(searchByKeyword.text.toString().trim().isNotEmpty())
                products(this, "", "", "", "${searchByKeyword.text.toString().trim()}")
        }

        searchByKeyword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Call onDone result here
                searchInitiator.performClick()
                true
            }
            false
        }

        filter.setOnClickListener{
            getBrands(this, object:CallBrands{
                override fun onActionSuccess(body: Model.GetBrand) {

                    Log.d("returned_this","successfully ${body.data}");

                    filterPopUp(this@StartActivity, body)

                }

                override fun onActionFailure(errorMessage:String) {

                    Log.d("returned_this_error","Error: $errorMessage");

                }
            })
        }

        filterNow.setOnClickListener{
            filter.performClick()
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_bar_menu, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.navigation_home -> {
                products(this, "", "", "", "")
                true
            }
            R.id.navigation_category -> {
                Toast.makeText(this, "to open a new activity", Toast.LENGTH_LONG).show()
                true
            }
            R.id.navigation_orders -> {
                Toast.makeText(this, "to open a new activity", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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

    fun getFirebaseToken(){
        Toast.makeText(this, "firebase was ready", Toast.LENGTH_LONG).show()
        Log.d("firebaseTokenReady", "firebase was ready")
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<*> ->
            if (task.isSuccessful) {
                val token = task.result
                Toast.makeText(this, "got firebase token", Toast.LENGTH_LONG).show()
                Log.d("firebaseTokenReady", "got firebase token")
                createCustomerProfile(token.toString(), "${LibSession(this).retrieveLibSession("full_name")}", "${LibSession(this).retrieveLibSession("phone_number")}", "${LibSession(this).retrieveLibSession("score")}")
            }else{
                Toast.makeText(this, "didn't get firebase token", Toast.LENGTH_LONG).show()
                Log.d("firebaseNotAvailable", "didn't get firebase token")
                createCustomerProfile("", "${LibSession(this).retrieveLibSession("full_name")}", "${LibSession(this).retrieveLibSession("phone_number")}", "${LibSession(this).retrieveLibSession("score")}")
            }
        }
    }

    fun getAllNotifications(){
        var notification = DBHelper(this, null).getAllNotifications() as ArrayList<*>


        Log.d("notifications", "${notification.size}")
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
                LibSession(this@StartActivity).profileIsCreated(false)
            }

            override fun onResponse(call: Call<Model.CreateProfile>?, response: Response<Model.CreateProfile>?) {

                if (response!!.isSuccessful){
                    if(response.body()!!.status == "00"){
                        Log.d("data_returned", "${response.body()}")
                        LibSession(this@StartActivity).profileIsCreated(true)
                    }else{
                        Log.d("data_returned", "${response.body()}")
                        LibSession(this@StartActivity).profileIsCreated(false)
                    }
                }else{
                    LibSession(this@StartActivity).profileIsCreated(false)
                }
            }
        })
    }

    fun getBrands(context: Context, callBack:CallBrands){

        this.callBrands = callBack;

        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("loading please wait...")
        progressDialog.setCancelable(false)
        progressDialog.show()


        val call : Call<Model.GetBrand> = apiService.getBrands("Basic ${Auth().auth(context)}")
        call.enqueue(object : Callback<Model.GetBrand> {
            override fun onFailure(call: Call<Model.GetBrand>?, t: Throwable?) {
                progressDialog.dismiss()
                uncodedErrors(t as Throwable, context)
            }

            override fun onResponse(call: Call<Model.GetBrand>?, response: Response<Model.GetBrand>?) {

                progressDialog.dismiss()

                if (response!!.isSuccessful){
                    Log.d("data_returned", "${response.body()}")
                    //successBrandFilterResponse(response.body()!!, context)
                    callBrands?.onActionSuccess(response.body()!!)
                }else{

                    callBrands?.onActionFailure("error occurred")

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

    //pull all products
    fun products(context: Context, maxAmount:String, minAmount:String, productId:String, keyword:String){

        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("loading please wait...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val call : Call<Model.GetProduct> = apiService.getProducts("Basic ${Auth().auth(context)}","${LibSession(context).retrieveLibSession("score")}", maxAmount, minAmount, productId, keyword)
        call.enqueue(object : Callback<Model.GetProduct> {
            override fun onFailure(call: Call<Model.GetProduct>?, t: Throwable?) {
                progressDialog.dismiss()
                uncodedErrors(t as Throwable, context)
            }

            override fun onResponse(call: Call<Model.GetProduct>?, response: Response<Model.GetProduct>?) {

                progressDialog.dismiss()

                if (response!!.isSuccessful){
                    Log.d("data_returned", "${response.body()}")
                    successProductsResponse(response.body()!!, context)
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

    fun successProductsResponse(results: Model.GetProduct, context: Context) {

        if (results.data != null){
            return productsAdaptor.setEvent(results.data.availableProducts)
        }

        val builder = AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
        builder.setMessage("Oops, something happened try again")
        builder.setPositiveButton(
            "Ok"
        ) {
                dialog, which -> dialog.cancel()
        }
        builder.setCancelable(false)
        builder.show()

    }

    fun filterPopUp(mContext: Context, body: Model.GetBrand){

        var brand_list: ArrayList<String> = ArrayList()
        var brandList = JSONArray()

        var mPopupWindow: PopupWindow? = null

        val inflater = mContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // Inflate the custom view
        val customView: View = inflater.inflate(R.layout.layout_filter, null, false)

        val closePopup = customView.findViewById<ImageView>(R.id.close_popup)
        val searchablespinner = customView.findViewById<SearchableSpinner>(R.id.searchablespinner)
        val minAmount = customView.findViewById<TextInputEditText>(R.id.min_amount)
        val maxAmount = customView.findViewById<TextInputEditText>(R.id.max_amount)
        val filterButton = customView.findViewById<MaterialButton>(R.id.filter_button)

        var brand = body.data.productTypes

        brand_list.add("Select Brand")
        for (q in 0 until brand.size) {
            brand_list.add("${brand[q].type}")
//      jsonCategoryStore.put("id", "${categoty[q].id}")
//      jsonCategoryStore.put("name", "${categoty[q].name}")
//      categotyList.put(jsonCategoryStore)
        }

        searchablespinner.adapter = ArrayAdapter<String>(
            mContext,
            android.R.layout.simple_spinner_dropdown_item,
            brand_list
        )

        closePopup.setOnClickListener{
            mPopupWindow?.dismiss()
        }

        filterButton.setOnClickListener{

            //check if brand isset
            var selectedName = ""
            var selectedId = ""
            if(searchablespinner.selectedItemPosition>=1){
                selectedName = "${searchablespinner.selectedItem}"
                for (q in 0 until brand.size) {
                    if (brand[q].type.trim().uppercase(Locale.getDefault()) == selectedName.trim()
                            .uppercase(Locale.getDefault())){
                        selectedId = brand[q].id
                    }
                }
            }

            //check min or max amount isset
            var lowAmount = "0"
            if(minAmount.text.toString().trim().isNotEmpty() &&
                maxAmount.text.toString().trim().isNotEmpty()){
                if(Integer.parseInt(minAmount.text.toString().trim()) >
                    Integer.parseInt(maxAmount.text.toString().trim())){

                    minAmount.error = "invalid amount"
                    maxAmount.error = "invalid amount"
                    minAmount.requestFocus()
                    return@setOnClickListener
                }
            }

            lowAmount = if(minAmount.text.toString().trim().isEmpty()) {
                "0"
            }else{
                minAmount.text.toString().trim()
            }


            products(this, "${maxAmount.text.toString().trim()}",
                "$lowAmount", "$selectedId", "")

            closePopup.performClick()

        }

        mPopupWindow = PopupWindow(
            customView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.elevation = 5.0f
        }

        mPopupWindow.isFocusable;
        mPopupWindow.isFocusable = true;
        mPopupWindow.isOutsideTouchable = false;
        mPopupWindow.showAtLocation(parentContainer, Gravity.CENTER,0,0);

    }

}
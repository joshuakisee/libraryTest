package com.paylater.paylater.activities.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paylater.paylater.R
import com.paylater.paylater.adaptors.ProductsAdaptor
import com.paylater.paylater.databinding.FragmentHomeBinding
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.paylater.paylater.activities.CallBrands
import com.paylater.paylater.utils.Model
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

private var _binding: FragmentHomeBinding? = null

  private val binding get() = _binding!!

  private lateinit var homeViewModel: HomeViewModel

  private lateinit var parentContainer: ConstraintLayout

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    val root: View = binding.root

    homeViewModel =
      ViewModelProvider(this).get(HomeViewModel::class.java)

    val searchByKeyword: EditText = root.findViewById(R.id.search_by_keyword)
    val searchInitiator: ImageView = root.findViewById(R.id.search_initiator)
    val productRecycler: RecyclerView = root.findViewById(R.id.products_list)
    parentContainer = root.findViewById(R.id.parent_container)
    val filter: LinearLayout = root.findViewById(R.id.filter)
    val filterNow: LinearLayout = root.findViewById(R.id.filter_now)

    productRecycler.apply {
      layoutManager = GridLayoutManager(activity, 1)
      homeViewModel.productsAdaptor = ProductsAdaptor(requireActivity())
      adapter = homeViewModel.productsAdaptor
    }

    homeViewModel.products(requireActivity(), "", "", "", "")

    searchInitiator.setOnClickListener{
      if(searchByKeyword.text.toString().trim().isNotEmpty())
        homeViewModel.products(requireActivity(), "", "", "", "${searchByKeyword.text.toString().trim()}")
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
      homeViewModel.getBrands(requireActivity(), object:CallBrands{
        override fun onActionSuccess(body: Model.GetBrand) {

          Log.d("returned_this","successfully ${body.data}");

          filterPopUp(requireActivity(), body)

        }

        override fun onActionFailure(errorMessage:String) {

          Log.d("returned_this_error","Error: $errorMessage");

        }
      })
    }
    filterNow.setOnClickListener{
      filter.performClick()
    }

    return root
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


      homeViewModel.products(requireActivity(), "${maxAmount.text.toString().trim()}",
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


  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
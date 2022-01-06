package com.paylater.paylater.activities.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paylater.paylater.R
import com.paylater.paylater.adaptors.BrandsAdaptor
import com.paylater.paylater.databinding.FragmentCategoryBinding
import android.content.Intent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.paylater.paylater.adaptors.BrandFilterAdaptor


class CategoryFragment : Fragment() {

  private lateinit var dashboardViewModel: CategoryViewModel
  private var _binding: FragmentCategoryBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

    lateinit var brand_title: TextView

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    dashboardViewModel =
            ViewModelProvider(this).get(CategoryViewModel::class.java)

    _binding = FragmentCategoryBinding.inflate(inflater, container, false)
    val root: View = binding.root

      val brandsRecycler: RecyclerView = root.findViewById(R.id.products_category_list)
      val productsCategoryFilter: RecyclerView = root.findViewById(R.id.products_category_filter_list)
      brand_title = root.findViewById(R.id.brand_title)

      brandsRecycler.apply {
          layoutManager = GridLayoutManager(activity,1)
          dashboardViewModel.brandAdaptor = BrandsAdaptor(requireActivity())
          adapter = dashboardViewModel.brandAdaptor
      }

      productsCategoryFilter.apply {
          layoutManager = GridLayoutManager(activity,2)
          dashboardViewModel.brandFilterAdaptor = BrandFilterAdaptor(requireActivity())
          adapter = dashboardViewModel.brandFilterAdaptor
      }

      dashboardViewModel.brands(requireActivity())

      LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(mMessageReceiver,
          IntentFilter("load_brand_items"));

    return root
  }

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            val categoryId = intent.getStringExtra("categoryId")
            val productId = intent.getStringExtra("productId")
            val productTitle = intent.getStringExtra("title")
            val updateTitleOnly = intent.getBooleanExtra("updateTitleOnly", false)

            if(updateTitleOnly){
                brand_title.text = productTitle.toString()
            }else{
                brand_title.text = productTitle.toString()

                dashboardViewModel.filterBrands(categoryId, productId, productTitle, requireActivity())
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(mMessageReceiver)
        _binding = null
    }
}
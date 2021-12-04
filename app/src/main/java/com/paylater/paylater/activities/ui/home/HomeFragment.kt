package com.paylater.paylater.activities.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paylater.paylater.R
import com.paylater.paylater.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

private var _binding: FragmentHomeBinding? = null

  private val binding get() = _binding!!

  private lateinit var homeViewModel: HomeViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    val root: View = binding.root

    homeViewModel =
      ViewModelProvider(this).get(HomeViewModel::class.java)

    val productRecycler: RecyclerView = root.findViewById(R.id.products_list)

//    productRecycler.apply {
//      layoutManager = LinearLayoutManager(activity)
//      homeViewModel.productsAdaptor = ProductsAdaptor(activity!!)
//      adapter = homeViewModel.productsAdaptor
//    }

    homeViewModel.products(activity!!)

    return root
  }


  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
package com.paylater.paylater.activities.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paylater.paylater.R
import com.paylater.paylater.adaptors.MyOdersAdaptor
import com.paylater.paylater.databinding.FragmentOrdersBinding
import com.paylater.paylater.utils.LibSession

class OrdersFragment : Fragment() {

  private lateinit var ordersViewModel: OrdersViewModel
private var _binding: FragmentOrdersBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
      ordersViewModel =
            ViewModelProvider(this).get(OrdersViewModel::class.java)

    _binding = FragmentOrdersBinding.inflate(inflater, container, false)
    val root: View = binding.root

      val odersRecycler: RecyclerView = root.findViewById(R.id.my_orders_list)

      odersRecycler.apply {
          layoutManager = GridLayoutManager(activity, 1)
          ordersViewModel.myOdersAdaptor = MyOdersAdaptor(requireActivity())
          adapter = ordersViewModel.myOdersAdaptor
      }

      ordersViewModel.myOrders(requireActivity(), "${LibSession(requireActivity()).retrieveLibSession("phone_number")}")

    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
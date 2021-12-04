//package com.paylater.paylater.adaptors
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.paylater.paylater.R
//import com.paylater.paylater.utils.Model
//import kotlinx.android.synthetic.main.layout_products.view.*
//
//class ProductsAdaptor (val mContext: Context
//) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    private val items: MutableList<Model.AvailableProducts> = mutableListOf()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return NewRequestViewHolder(
//            LayoutInflater.from(parent.context).inflate(R.layout.layout_products, parent, false)
//        )
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        when (holder) {
//            is NewRequestViewHolder -> {
//                holder.bind(items.get(position), mContext)
//            }
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return items.size
//    }
//
//    fun setEvent(data: List<Model.AvailableProducts>) {
//        items.clear()
//        items.addAll(data)
//        notifyDataSetChanged()
//    }
//
//    class NewRequestViewHolder constructor(
//        itemView: View
//    ) : RecyclerView.ViewHolder(itemView) {
//        val deviceDescription = itemView.device_description
//        val deviceCost = itemView.device_cost
//        fun bind(request: Model.AvailableProducts, mContext: Context) {
//
//            deviceCost.text = "KES. ${request.price}"
//
//        }
//    }
//
//}
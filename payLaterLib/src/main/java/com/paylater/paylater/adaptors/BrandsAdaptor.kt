package com.paylater.paylater.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.paylater.paylater.R
import com.paylater.paylater.utils.Model
import androidx.localbroadcastmanager.content.LocalBroadcastManager

import android.content.Intent




class BrandsAdaptor (val mContext: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: MutableList<Model.BrandDataProductTypes> = mutableListOf()
    private val items1: MutableList<Model.BrandDataCategories> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NewRequestViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_category_brands, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NewRequestViewHolder -> {
                holder.bind(items.get(position), items1, position, mContext)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setEvent(data: List<Model.BrandDataProductTypes>, data1: List<Model.BrandDataCategories>) {
        items.clear()
        items.addAll(data)
        items1.clear()
        items1.addAll(data1)
        notifyDataSetChanged()
    }

    class NewRequestViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val brandName = itemView.findViewById<TextView>(R.id.brand_name)!!
        fun bind(request: Model.BrandDataProductTypes, request1: MutableList<Model.BrandDataCategories>, position: Int, mContext: Context) {

            brandName.text = "${request.type}"

            brandName.setOnClickListener{
                val intent = Intent("load_brand_items")
                intent.putExtra("categoryId", request1[0].id)
                intent.putExtra("productId", request.id)
                intent.putExtra("title", "${request.type}")
                intent.putExtra("updateTitleOnly", false)
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent)
            }


        }
    }

}
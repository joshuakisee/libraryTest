package com.paylater.paylater.adaptors

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.paylater.paylater.R
import com.paylater.paylater.activities.PurchaseActivity
import com.paylater.paylater.utils.Model
import com.squareup.picasso.Picasso

class BrandFilterAdaptor (val mContext: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items: MutableList<Model.GetBrandFilterAvailableProducts> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NewRequestViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_category_brand_filter, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NewRequestViewHolder -> {
                holder.bind(items.get(position), mContext)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setEvent(data: List<Model.GetBrandFilterAvailableProducts>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    class NewRequestViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val deviceFilter = itemView.findViewById<LinearLayout>(R.id.device_filter)!!
        private val deviceFilterImage = itemView.findViewById<ImageView>(R.id.device_filter_image)!!
        private val deviceFilterDescription = itemView.findViewById<TextView>(R.id.device_filter_description)!!
        private val deviceFilterCost = itemView.findViewById<TextView>(R.id.device_filter_cost)!!
        fun bind(request: Model.GetBrandFilterAvailableProducts, mContext: Context) {

            var description = ""
            if(request.name.isNotEmpty())
                description += request.name
            if(request.screenSize.isNotEmpty())
                description = description+", "+request.screenSize
            if(request.rom.isNotEmpty())
                description = description+", "+request.rom+" ROM"
            if(request.ram.isNotEmpty())
                description = description+" + "+request.ram+" RAM"

            deviceFilterDescription.text = "$description"

            deviceFilterCost.text = "KES. ${request.deposit}"

            var installments = ""
            for(i in request.orderInstallments.indices){
                installments += "\n${request.orderInstallments[i].installmentName} - ${request.orderInstallments[i].installmentPercent}% amounting to KES. ${request.orderInstallments[i].installmentAmount}"
            }

            Picasso.get()
                .load("${request.image}")
                .error( R.drawable.ic_baseline_image_24)
                .placeholder( R.drawable.loading)
                .into(deviceFilterImage)

            deviceFilter.setOnClickListener{
                var map = HashMap<String, Any>()
                map.put("id", "${request.id}")
                map.put("image", "${request.image}")
                map.put("otherImage", "${request.moreImages}")
                map.put("description", deviceFilterDescription.text.toString().trim())
                map.put("deposit", "${request.deposit}")
                map.put("price", "${request.price}")
                map.put("keyFeatures", "${request.description}")
                map.put("orderInstallments", "$installments".trim())
                val intent = Intent(mContext, PurchaseActivity::class.java)
                intent.putExtra("data", map)
                mContext.startActivity(intent)
            }
        }
    }


}
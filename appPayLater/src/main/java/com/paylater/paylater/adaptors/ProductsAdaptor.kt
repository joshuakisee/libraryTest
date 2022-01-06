package com.paylater.paylater.adaptors

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.paylater.paylater.R
import com.paylater.paylater.activities.PurchaseActivity
import com.paylater.paylater.utils.Model
import com.squareup.picasso.Picasso

class ProductsAdaptor (val mContext: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: MutableList<Model.AvailableProducts> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NewRequestViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_products, parent, false)
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

    fun setEvent(data: List<Model.AvailableProducts>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    class NewRequestViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val deviceImage = itemView.findViewById<ImageView>(R.id.device_image)!!
        private val deviceName = itemView.findViewById<TextView>(R.id.device_name)!!
        private val deviceTotalPrice = itemView.findViewById<TextView>(R.id.device_total_price)!!
        private val deviceDeposit = itemView.findViewById<TextView>(R.id.device_deposit)!!
        private val installments = itemView.findViewById<TextView>(R.id.installments)!!
        private val deviceInstallments = itemView.findViewById<TextView>(R.id.device_installments)!!
        private val deviceDescription = itemView.findViewById<TextView>(R.id.device_description)!!
        private val device_price = itemView.findViewById<TextView>(R.id.device_price)!!
        private val deviceViewMoreDescription = itemView.findViewById<CardView>(R.id.device_view_more_description)!!
        fun bind(request: Model.AvailableProducts, mContext: Context) {

            var description = ""
            if(null != request.productType && request.productType.type.isNotEmpty())
                description += request.productType.type+", "
            if(request.name.isNotEmpty())
                description += request.name
            if(request.screenSize.isNotEmpty())
                description = description+", "+request.screenSize
            if(request.rom.isNotEmpty())
                description = description+", "+request.rom+" ROM"
            if(request.ram.isNotEmpty())
                description = description+" + "+request.ram+" RAM"

            deviceDescription.text = "$description"
            device_price.text = "KES. ${request.price}"

            deviceName.text = "${request.name}"
            deviceTotalPrice.text = "KES. ${request.price}"
            deviceDeposit.text = "KES. ${request.deposit}"
            installments.text = "5 MONTHLY\nINSTALLMENTS OF"
            deviceInstallments.text = "KES. 3000"

            Picasso.get()
                .load("${request.image}")
                .error( R.drawable.ic_baseline_image_24)
                .placeholder( R.drawable.loading)
                .into(deviceImage)

            deviceViewMoreDescription.setOnClickListener{

                var installments = ""
                for(i in request.orderInstallments.indices){
                    installments += "\n${request.orderInstallments[i].installmentName} - ${request.orderInstallments[i].installmentPercent}% amounting to KES. ${request.orderInstallments[i].installmentAmount}"
                }

                var map = HashMap<String, Any>()
                map.put("id", "${request.id}")
                map.put("image", "${request.image}")
                map.put("otherImage", "${request.moreImages}")
                map.put("description", deviceDescription.text.toString().trim())
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
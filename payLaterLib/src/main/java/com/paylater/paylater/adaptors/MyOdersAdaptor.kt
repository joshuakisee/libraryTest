package com.paylater.paylater.adaptors

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.paylater.paylater.R
import com.paylater.paylater.fragment.MakePayment
import com.paylater.paylater.fragment.OrderDetails
import com.paylater.paylater.utils.Model
import com.squareup.picasso.Picasso

class MyOdersAdaptor (val mContext: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: MutableList<Model.MyOrdersDataOrders> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NewRequestViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_my_oreders, parent, false)
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

    fun setEvent(data: List<Model.MyOrdersDataOrders>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    class NewRequestViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val orderImage = itemView.findViewById<ImageView>(R.id.order_image)!!
        private val orderName = itemView.findViewById<TextView>(R.id.order_name)!!
        private val orderDeposit = itemView.findViewById<TextView>(R.id.order_deposit)!!
        private val orderTotalPrice = itemView.findViewById<TextView>(R.id.order_total_price)!!
        private val orderBalance = itemView.findViewById<TextView>(R.id.order_balance)!!
        private val depositPaid = itemView.findViewById<ImageView>(R.id.deposit_paid)!!
        private val depositNotPaid = itemView.findViewById<ImageView>(R.id.deposit_not_paid)!!
        private val orderDescription = itemView.findViewById<TextView>(R.id.order_description)!!
        private val orderStatus = itemView.findViewById<TextView>(R.id.order_status)!!
        private val orderViewMore = itemView.findViewById<LinearLayout>(R.id.order_view_more)!!

        fun bind(request: Model.MyOrdersDataOrders, mContext: Context) {

            var instalmentDueDate = ""
            var installmentBalance = ""
            var installmentList = request.orderInstallments

            for (i in installmentList.indices){
                if(installmentList[i].paymentStatus.trim().toUpperCase() == "UN-PAID"){
                    instalmentDueDate = installmentList[i].installmentDueOn
                    installmentBalance = installmentList[i].installmentBalance
                    break
                }

            }


            if(request.depositPaidState) {
                depositPaid.visibility = View.VISIBLE
                depositNotPaid.visibility = View.GONE
                orderDescription.text = "Next installment of\namount KES. $installmentBalance\nis due on\n$instalmentDueDate"
                orderStatus.text = "${request.deliveryStatus}"
                orderStatus.setBackgroundResource(R.drawable.rounded_edittext_green)
            } else{
                depositPaid.visibility = View.GONE
                depositNotPaid.visibility = View.VISIBLE
                orderDescription.text = "Deposit is not paid\npay now."
                orderStatus.text = "awaiting_payment"
                orderStatus.setBackgroundResource(R.drawable.rounded_gray_backgraound)
            }

            var productType = ""
            if(request.product.productType.type.isNotEmpty())
                productType = "${request.product.productType.type}\n"

            orderName.text = "$productType${request.productName}"
            orderTotalPrice.text = "KES. ${request.price}"
            orderDeposit.text = "KES. ${request.depositPaid}"
            orderBalance.text = if(request.depositPaidState) "KES. ${request.balance}" else "KES. ${request.price}"

            Picasso.get()
                .load("${request.image}")
                .error( R.drawable.ic_baseline_image_24)
                .placeholder( R.drawable.loading)
                .into(orderImage)

            orderStatus.setOnClickListener{
                orderDescription.performClick()
            }

            orderDescription.setOnClickListener{
                if(request.depositPaidState) {
                    orderViewMore.performClick()
                }else{

                    var bundle = Bundle()
                    bundle.putString("amount", "${request.depositPaid}")
                    bundle.putString("order_id", "${request.id}")

                    val ft = (mContext as FragmentActivity).supportFragmentManager
                    val bottomSheetFragment = MakePayment()
                    bottomSheetFragment.arguments = bundle
                    bottomSheetFragment.show(ft, bottomSheetFragment.tag)

                }
            }

            orderViewMore.setOnClickListener{
                if(!request.depositPaidState) {
                    orderDescription.performClick()
                    return@setOnClickListener
                }

                var bundle = Bundle()
                bundle.putString("itemName", "${request.productName}")
                bundle.putString("itemStatus", "${request.deliveryStatus}")
                bundle.putString("itemPrice", "${request.price}")
                bundle.putString("itemDeposit", "${request.depositPaid}")
                bundle.putString("amountBalance", "${request.balance}")
                bundle.putString("itemDuration", "${request.duration}")
                bundle.putString("device_id", "${request.productId}")
                bundle.putBoolean("fully_paid", request.fullyPaid)
                bundle.putString("due_installment", "$installmentBalance")
                bundle.putString("installment_date", "$instalmentDueDate")

                val ft = (mContext as FragmentActivity).supportFragmentManager
                val bottomSheetFragment = OrderDetails()
                bottomSheetFragment.arguments = bundle
                bottomSheetFragment.show(ft, bottomSheetFragment.tag)

            }

        }
    }

}
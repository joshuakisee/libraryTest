package com.paylater.paylater.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.paylater.paylater.R
import com.paylater.paylater.activities.PurchaseActivity
import com.paylater.paylater.activities.TrackerActivity

class OrderDetails : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view = inflater.inflate(R.layout.layout_order_details, container, false)

        val itemNameValue = arguments?.getString("itemName")
        val itemStatusValue = arguments?.getString("itemStatus")
        val itemPriceValue = arguments?.getString("itemPrice")
        val itemDepositValue = arguments?.getString("itemDeposit")
        val amountBalanceValue = arguments?.getString("amountBalance")
        val itemDurationValue = arguments?.getString("itemDuration")
        val deviceId = arguments?.getString("device_id")
        val fullyPaid = arguments?.getBoolean("fully_paid")
        val dueInstallment = arguments?.getString("due_installment")
        val installmentDate = arguments?.getString("installment_date")

        if(deviceId.toString().isEmpty()){
            Toast.makeText(activity, "error occurred! please try again", Toast.LENGTH_LONG).show()
            dismiss()
            return view
        }

        val itemName = view.findViewById<TextView>(R.id.item_name)
        val closeItemPopup = view.findViewById<ImageView>(R.id.close_item_popup)
        val itemStatus = view.findViewById<TextView>(R.id.item_status)
        val itemTracker = view.findViewById<TextView>(R.id.item_tracker)
        val itemPrice = view.findViewById<TextView>(R.id.item_price)
        val itemDeposit = view.findViewById<TextView>(R.id.item_deposit)
        val amountBalance = view.findViewById<TextView>(R.id.amount_balance)
        val itemDuration = view.findViewById<TextView>(R.id.item_duration)
        val itemDueInstallment = view.findViewById<TextView>(R.id.item_due_installment)
        val itemDueDate = view.findViewById<TextView>(R.id.item_due_date)
        val paymentButton = view.findViewById<MaterialButton>(R.id.payment_button)

        itemName.text = "$itemNameValue"
        itemStatus.text = "$itemStatusValue"
        itemPrice.text = ": KES. $itemPriceValue"
        itemDeposit.text = ": KES. $itemDepositValue"
        amountBalance.text = ": KES. $amountBalanceValue"
        itemDuration.text = ": $itemDurationValue months"
        itemDueInstallment.text = ": KES. $dueInstallment"
        itemDueDate.text = ": $installmentDate"

        closeItemPopup.setOnClickListener{
            dismiss()
        }

        itemTracker.setOnClickListener{
            val intent = Intent(activity, TrackerActivity::class.java)
            intent.putExtra("status", "$itemStatusValue")
            requireActivity().startActivity(intent)
        }

        paymentButton.setOnClickListener{
            if(fullyPaid == true){
                Toast.makeText(activity, "fully paid, place another order.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            Toast.makeText(activity, "coming soon.", Toast.LENGTH_LONG).show()
        }

        return view
    }
}
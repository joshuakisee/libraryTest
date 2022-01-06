package com.paylater.paylater.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.paylater.paylater.R
import com.paylater.paylater.activities.NotificationActivity
import com.paylater.paylater.utils.DB.DBHelper
import com.paylater.paylater.utils.Model

class NotificationAdaptor (val mContext: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: MutableList<Model.NotificationData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NewRequestViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_notifications, parent, false)
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

    fun setEvent(data: List<Model.NotificationData>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    class NewRequestViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.notify_title)!!
        private val msg = itemView.findViewById<TextView>(R.id.notify_msg)!!
        private val newMsg = itemView.findViewById<TextView>(R.id.newMsg)!!
        private val deleteMsg = itemView.findViewById<ImageView>(R.id.deleteMsg)!!
        private val notificationHouse = itemView.findViewById<LinearLayout>(R.id.notification_house)!!

        fun bind(request: Model.NotificationData, mContext: Context) {

            title.text = "${request.title}"
            msg.text = "${request.body}"

            if(request.status == "1"){
                newMsg.visibility = View.VISIBLE
                deleteMsg.visibility = View.GONE

                notificationHouse.setOnClickListener{
                    if (mContext is NotificationActivity) {
                        (mContext as NotificationActivity).makeAsReadNotification(request.id)
                    }
                }
            }else{
                newMsg.visibility = View.GONE
                deleteMsg.visibility = View.VISIBLE
            }

            deleteMsg.setOnClickListener{
                if (mContext is NotificationActivity) {
                    (mContext as NotificationActivity).deleteNotification(request.id)
                }
            }

        }
    }

}
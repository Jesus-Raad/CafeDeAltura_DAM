package com.example.cafedealtura_dam.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R

// adapter secundario

class OrderItemsAdapter(
    private val items: List<OrderItemUiModel>
) : RecyclerView.Adapter<OrderItemsAdapter.OrderItemViewHolder>() {

    inner class OrderItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvItemName: TextView = view.findViewById(R.id.tvItemName)
        val tvItemQuantity: TextView = view.findViewById(R.id.tvItemQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_product, parent, false)
        return OrderItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        val item = items[position]
        holder.tvItemName.text = item.productName
        holder.tvItemQuantity.text = "x${item.quantity}"
    }

    override fun getItemCount(): Int = items.size
}
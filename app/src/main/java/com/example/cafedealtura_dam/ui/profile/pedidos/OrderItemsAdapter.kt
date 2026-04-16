package com.example.cafedealtura_dam.ui.profile.pedidos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.ProductsRepository
import com.example.cafedealtura_dam.model.Orders_item

class OrderItemsAdapter(
    private val items: List<Orders_item>
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
        val product = ProductsRepository.getProductById(item.id_product)

        holder.tvItemName.text = product?.brand ?: "Producto #${item.id_product}"
        holder.tvItemQuantity.text = "x${item.quantity}"
    }

    override fun getItemCount(): Int = items.size
}
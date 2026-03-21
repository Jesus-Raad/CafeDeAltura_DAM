package com.example.cafedealtura_dam.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.model.CartItem


class CarsItemsAdapter : RecyclerView.Adapter<CarsItemsAdapter.VH>() {
    private var items: List<CartItem> = emptyList()

    fun submitList(newItems: List<CartItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val img: ImageView = itemView.findViewById(R.id.imageView)
        private val tvName: TextView = itemView.findViewById(R.id.textTitle)
        private val tvOrigin: TextView = itemView.findViewById(R.id.textUbication)
        private val tvCacacteristic: TextView = itemView.findViewById(R.id.textCaracteristic)
        private val tvPrice: TextView = itemView.findViewById(R.id.textPrice)

        private var tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)


        fun bind(product: CartItem) {
            img.setImageResource(product.product.imgURL?: R.drawable.ic_launcher_background)
            tvName.text = product.product.brand
            tvOrigin.text = product.product.origin
            tvCacacteristic.text = "${product.product.weight}g • ${product.product.category}"
            tvPrice.text = "$${ product.product.price}"
            tvQuantity.text="${product.quantity}"

        }
    }
}
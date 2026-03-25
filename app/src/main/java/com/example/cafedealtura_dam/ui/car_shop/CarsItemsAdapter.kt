package com.example.cafedealtura_dam.ui.car_shop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.CartRepository
import com.example.cafedealtura_dam.model.CartItem
import java.util.Locale

class CarsItemsAdapter(
    private val onCartChanged: () -> Unit
) : RecyclerView.Adapter<CarsItemsAdapter.VH>() {

    private var items: List<CartItem> = emptyList()

    fun submitList(newItems: List<CartItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return VH(view, onCartChanged)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    class VH(
        itemView: View,
        private val onCartChanged: () -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val img: ImageView = itemView.findViewById(R.id.imageView)
        private val tvName: TextView = itemView.findViewById(R.id.textTitle)
        private val tvOrigin: TextView = itemView.findViewById(R.id.textUbication)
        private val tvCharacteristic: TextView = itemView.findViewById(R.id.textCaracteristic)
        private val tvPrice: TextView = itemView.findViewById(R.id.textPrice)
        private val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)

        private val btnPlus: TextView = itemView.findViewById(R.id.btnPlus)
        private val btnMinus: TextView = itemView.findViewById(R.id.btnMinus)
        private val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)

        fun bind(cartItem: CartItem) {
            val product = cartItem.product

            Glide.with(itemView.context)
                .load(product.img_url)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(img)
            tvName.text = product.brand
            tvOrigin.text = product.origin
            tvCharacteristic.text = "${product.weight}g • ${product.category}"
            tvPrice.text = formatPrice(product.price)
            tvQuantity.text = cartItem.quantity.toString()

            btnPlus.setOnClickListener {
                CartRepository.increaseQuantity(product.id_coffe)
                onCartChanged()
            }

            btnMinus.setOnClickListener {
                CartRepository.decreaseQuantity(product.id_coffe)
                onCartChanged()
            }

            btnDelete.setOnClickListener {
                CartRepository.deleteItem(product.id_coffe)
                onCartChanged()
            }
        }

        private fun formatPrice(value: Double): String {
            return String.format(Locale.US, "%.2f €", value)
        }
    }
}
package com.example.cafedealtura_dam.ui.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.model.Products_coffe
import com.google.android.material.card.MaterialCardView

class ProductsAdapter(
    private val onProductClick: (Products_coffe) -> Unit
) : RecyclerView.Adapter<ProductsAdapter.VH>() {

    private var items: List<Products_coffe> = emptyList()

    fun submitList(newItems: List<Products_coffe>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val product = items[position]
        holder.bind(product)

        val isAvailable = product.available == 1

        holder.cardProduct.setOnClickListener(null)
        holder.cardProduct.isClickable = isAvailable
        holder.cardProduct.isFocusable = isAvailable
        holder.cardProduct.isEnabled = isAvailable

        if (isAvailable) {
            holder.cardProduct.setOnClickListener {
                onProductClick(product)
            }
        }
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val cardProduct: MaterialCardView = itemView.findViewById(R.id.cardProduct)

        private val img: ImageView = itemView.findViewById(R.id.imgProduct)
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvOrigin: TextView = itemView.findViewById(R.id.tvOrigin)
        private val tvMeta: TextView = itemView.findViewById(R.id.tvMeta)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)

        private val overlayUnavailable: View = itemView.findViewById(R.id.overlayUnavailable)
        private val tvUnavailable: TextView = itemView.findViewById(R.id.tvUnavailable)
        private val contentContainer: View = itemView.findViewById(R.id.contentContainer)

        fun bind(product: Products_coffe) {
            Glide.with(itemView.context)
                .load(product.img_url)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_close_clear_cancel)
                .into(img)

            tvName.text = product.brand
            tvOrigin.text = product.origin ?: ""
            tvMeta.text = "${product.weight}g"
            tvPrice.text = String.format("$%.2f", product.price)

            val isAvailable = product.available == 1

            if (isAvailable) {
                overlayUnavailable.visibility = View.GONE
                tvUnavailable.visibility = View.GONE
                contentContainer.alpha = 1f
            } else {
                overlayUnavailable.visibility = View.VISIBLE
                tvUnavailable.visibility = View.VISIBLE
                contentContainer.alpha = 0.55f
            }
        }
    }
}
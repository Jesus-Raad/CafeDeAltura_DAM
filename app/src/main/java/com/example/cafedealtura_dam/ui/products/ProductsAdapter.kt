package com.example.cafedealtura_dam.ui.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R

class ProductsAdapter : RecyclerView.Adapter<ProductsAdapter.VH>() {

    private var items: List<ProductUiModel> = emptyList()

    fun submitList(newItems: List<ProductUiModel>) {
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
        holder.bind(items[position])
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val img: ImageView = itemView.findViewById(R.id.img)
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvOrigin: TextView = itemView.findViewById(R.id.tvOrigin)
        private val tvMeta: TextView = itemView.findViewById(R.id.tvMeta)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)

        fun bind(product: ProductUiModel) {
            img.setImageResource(product.imageRes)
            tvName.text = product.name
            tvOrigin.text = product.origin
            tvMeta.text = product.meta
            tvPrice.text = product.price
        }
    }
}
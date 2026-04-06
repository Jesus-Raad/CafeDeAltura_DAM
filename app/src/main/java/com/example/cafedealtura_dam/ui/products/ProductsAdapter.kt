package com.example.cafedealtura_dam.ui.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cafedealtura_dam.R

class ProductsAdapter(
    private var products: List<ProductUiModel>,
    private val onProductClick: (ProductUiModel) -> Unit
) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    private var allProducts: List<ProductUiModel> = products
    private var activeFilter: String = "Todos"

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgProduct)
        val name: TextView = view.findViewById(R.id.tvName)
        val origin: TextView = view.findViewById(R.id.tvOrigin)
        val meta: TextView = view.findViewById(R.id.tvMeta)
        val price: TextView = view.findViewById(R.id.tvPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.name.text = product.name
        holder.origin.text = product.origin
        holder.meta.text = "250g • ${product.category.ifEmpty { "En grano" }}"
        holder.price.text = String.format("$%.2f", product.price)

        Glide.with(holder.itemView.context)
            .load(product.imageUrl)
            .into(holder.img)

        holder.itemView.setOnClickListener {
            onProductClick(product)
        }
    }

    fun updateData(newProducts: List<ProductUiModel>) {
        allProducts = newProducts
        applyFilter(activeFilter)
    }

    fun applyFilter(category: String) {
        activeFilter = category
        products = if (category == "Todos") {
            allProducts
        } else {
            allProducts.filter { it.category.equals(category, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }
}
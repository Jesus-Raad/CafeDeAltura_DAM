package com.example.cafedealtura_dam.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cafedealtura_dam.R


class ProductsAdapter(
    private var products: List<ProductUiModel>
) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

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
        holder.meta.text = product.meta
        holder.price.text = product.price

        Glide.with(holder.itemView.context)
            .load(product.imageUrl)
            .into(holder.img)

        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putString("name", product.name)
                putString("origin", product.origin)
                putString("price", product.price)
                putString("image", product.imageUrl)
            }

            holder.itemView.findNavController()
                .navigate(R.id.products_to_detail, bundle)
        }
    }

    fun updateData(newProducts: List<ProductUiModel>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
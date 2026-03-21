package com.example.cafedealtura_dam.ui.products

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cafedealtura_dam.R

class ProductDetailFragment : Fragment(R.layout.fragment_product_detail) {

    private var quantity = 1
    private var unitPrice = 0.0
    private var isFavorite = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = arguments?.getString("name") ?: ""
        val origin = arguments?.getString("origin") ?: ""
        val priceText = arguments?.getString("price") ?: "0 €"
        val image = arguments?.getString("image") ?: ""

        unitPrice = priceText
            .replace("€", "")
            .replace("$", "")
            .trim()
            .replace(",", ".")
            .toDoubleOrNull() ?: 0.0

        val imgProduct = view.findViewById<ImageView>(R.id.imgProduct)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvOrigin = view.findViewById<TextView>(R.id.tvOrigin)
        val tvPrice = view.findViewById<TextView>(R.id.tvPrice)
        val tvQuantity = view.findViewById<TextView>(R.id.tvQuantity)

        val btnPlus = view.findViewById<Button>(R.id.btnPlus)
        val btnMinus = view.findViewById<Button>(R.id.btnMinus)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        val btnFavorite = view.findViewById<ImageButton>(R.id.btnFavorite)

        // 👉 Установка данных
        tvTitle.text = name
        tvOrigin.text = origin
        tvQuantity.text = quantity.toString()
        updatePrice(tvPrice)

        Glide.with(requireContext())
            .load(image)
            .into(imgProduct)

        // 👉 КНОПКИ
        btnPlus.setOnClickListener {
            quantity++
            tvQuantity.text = quantity.toString()
            updatePrice(tvPrice)
        }

        btnMinus.setOnClickListener {
            if (quantity > 1) {
                quantity--
                tvQuantity.text = quantity.toString()
                updatePrice(tvPrice)
            }
        }

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        btnFavorite.setOnClickListener {
            isFavorite = !isFavorite
            btnFavorite.setImageResource(
                if (isFavorite) R.drawable.ic_favorite
                else R.drawable.ic_favorite_border
            )
        }
    }

    private fun updatePrice(tvPrice: TextView) {
        val total = unitPrice * quantity
        tvPrice.text = String.format("%.2f €", total)
    }
}
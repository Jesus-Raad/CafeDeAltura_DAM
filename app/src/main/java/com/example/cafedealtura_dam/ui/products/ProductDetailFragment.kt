package com.example.cafedealtura_dam.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cafedealtura_dam.R

class ProductDetailFragment : Fragment() {

    private var quantity = 1
    private var unitPrice = 0.0
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = arguments?.getString("name") ?: ""
        val origin = arguments?.getString("origin") ?: ""
        val price = arguments?.getDouble("price") ?: 0.0
        val image = arguments?.getString("image") ?: ""
        val description = arguments?.getString("description") ?: ""
        val rating = arguments?.getDouble("rating") ?: 0.0

        unitPrice = price

        val imgProduct = view.findViewById<ImageView>(R.id.imgProduct)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvOrigin = view.findViewById<TextView>(R.id.tvOrigin)
        val tvPrice = view.findViewById<TextView>(R.id.tvPrice)
        val tvQuantity = view.findViewById<TextView>(R.id.tvQuantity)
        val tvDescription = view.findViewById<TextView>(R.id.tvDescription)
        val tvRating = view.findViewById<TextView>(R.id.tvRating)

        val btnPlus = view.findViewById<Button>(R.id.btnPlus)
        val btnMinus = view.findViewById<Button>(R.id.btnMinus)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        val btnFavorite = view.findViewById<ImageButton>(R.id.btnFavorite)
        val btnAddToCart = view.findViewById<Button>(R.id.btnAddToCart)
        val btnGrano = view.findViewById<Button>(R.id.btnGrano)
        val btnMolido = view.findViewById<Button>(R.id.btnMolido)

        tvTitle.text = name
        tvOrigin.text = origin
        tvDescription.text = description
        tvRating.text = "★ $rating"
        tvQuantity.text = quantity.toString()

        if (image.isNotEmpty()) {
            Glide.with(requireContext())
                .load(image)
                .into(imgProduct)
        }

        updatePrice(tvPrice, btnAddToCart)

        btnPlus.setOnClickListener {
            quantity++
            tvQuantity.text = quantity.toString()
            updatePrice(tvPrice, btnAddToCart)
        }

        btnMinus.setOnClickListener {
            if (quantity > 1) {
                quantity--
                tvQuantity.text = quantity.toString()
                updatePrice(tvPrice, btnAddToCart)
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

        btnGrano.setOnClickListener {
            btnGrano.setBackgroundResource(R.drawable.bg_selected)
            btnMolido.setBackgroundResource(R.drawable.bg_unselected)
        }

        btnMolido.setOnClickListener {
            btnMolido.setBackgroundResource(R.drawable.bg_selected)
            btnGrano.setBackgroundResource(R.drawable.bg_unselected)
        }

        btnAddToCart.setOnClickListener {
            // TODO: добавить в корзину
        }
    }

    private fun updatePrice(tvPrice: TextView, btnAddToCart: Button) {
        val total = unitPrice * quantity
        val priceText = String.format("$%.2f", total)
        tvPrice.text = priceText
        btnAddToCart.text = "Agregar al carrito - $priceText"
    }
}
package com.example.cafedealtura_dam.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.ProductsRepository
import com.example.cafedealtura_dam.model.Products_coffe
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup

class ProductDetailFragment : Fragment() {

    private var quantity = 1
    private var unitPrice = 0.0
    private var isFavorite = false
    private var selectedGrindType = "grano"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productId = arguments?.getInt("id_coffe", -1) ?: -1
        val product = ProductsRepository.getProducts().find { it.id_coffe == productId }

        if (product == null) {
            findNavController().navigateUp()
            return
        }

        unitPrice = product.price

        val imgProduct = view.findViewById<ImageView>(R.id.imgProduct)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvOrigin = view.findViewById<TextView>(R.id.tvOrigin)
        val tvPrice = view.findViewById<TextView>(R.id.tvPrice)
        val tvQuantity = view.findViewById<TextView>(R.id.tvQuantity)
        val tvDescription = view.findViewById<TextView>(R.id.tvDescription)
        val tvRating = view.findViewById<TextView>(R.id.tvRating)

        val btnPlus = view.findViewById<MaterialButton>(R.id.btnPlus)
        val btnMinus = view.findViewById<MaterialButton>(R.id.btnMinus)
        val btnAddToCart = view.findViewById<MaterialButton>(R.id.btnAddToCart)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        val btnFavorite = view.findViewById<ImageButton>(R.id.btnFavorite)

        val toggleGroup = view.findViewById<MaterialButtonToggleGroup>(R.id.toggleGroupMolido)

        bindProduct(
            product = product,
            imgProduct = imgProduct,
            tvTitle = tvTitle,
            tvOrigin = tvOrigin,
            tvDescription = tvDescription,
            tvRating = tvRating
        )

        tvQuantity.text = quantity.toString()
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

        toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                selectedGrindType = when (checkedId) {
                    R.id.btnGrano -> "grano"
                    R.id.btnMolido -> "molido"
                    else -> "grano"
                }
            }
        }

        btnAddToCart.setOnClickListener {
            // TODO: añadir al carrito usando product, quantity y selectedGrindType
        }
    }

    private fun bindProduct(
        product: Products_coffe,
        imgProduct: ImageView,
        tvTitle: TextView,
        tvOrigin: TextView,
        tvDescription: TextView,
        tvRating: TextView
    ) {
        tvTitle.text = product.brand

        tvOrigin.text = if (!product.origin.isNullOrEmpty()) {
            "${product.origin} · ${product.weight}g"
        } else {
            "${product.weight}g"
        }

        tvDescription.text = product.description ?: ""

        // Tu modelo actual no tiene rating
        tvRating.text = ""

        if (!product.img_url.isNullOrEmpty()) {
            Glide.with(requireContext())
                .load(product.img_url)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_close_clear_cancel)
                .into(imgProduct)
        }
    }

    private fun updatePrice(tvPrice: TextView, btnAddToCart: MaterialButton) {
        val total = unitPrice * quantity
        val priceText = String.format("$%.2f", total)
        tvPrice.text = priceText
        btnAddToCart.text = "Agregar al carrito · $priceText"
    }
}
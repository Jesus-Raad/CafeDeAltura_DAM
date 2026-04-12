package com.example.cafedealtura_dam.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.CartRepository
import com.example.cafedealtura_dam.data.ProductsRepository
import com.example.cafedealtura_dam.data.UserSession
import com.example.cafedealtura_dam.dataAPI.ApiService
import com.example.cafedealtura_dam.model.Products_coffe
import com.google.android.material.button.MaterialButton
import java.util.Locale

class ProductDetailFragment : Fragment() {

    private var quantity = 1
    private var unitPrice = 0.0
    private var isFavorite = false
    private var isProcessingFavorite = false

    private var currentProduct: Products_coffe? = null
    private var btnFavorite: ImageButton? = null

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

        currentProduct = product
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
        btnFavorite = view.findViewById(R.id.btnFavorite)

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
        updateFavoriteIcon()

        loadFavoriteState(product.id_coffe)

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

        btnFavorite?.setOnClickListener {
            if (!isProcessingFavorite) {
                toggleFavorite()
            }
        }

        btnAddToCart.setOnClickListener {
            CartRepository.addProduct(product, quantity)
            Toast.makeText(
                requireContext(),
                "Añadido al carrito: ${product.brand} x$quantity",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun loadFavoriteState(productId: Int) {
        val user = UserSession.getUser()

        if (user == null) {
            isFavorite = false
            updateFavoriteIcon()
            return
        }

        btnFavorite?.isEnabled = false

        ApiService.Get.getFavorites(
            context = requireContext(),
            idUser = user.id_user,
            onResult = { favorites ->
                isFavorite = favorites.any { it.id_coffe == productId }
                updateFavoriteIcon()
                btnFavorite?.isEnabled = true
            },
            onError = {
                isFavorite = false
                updateFavoriteIcon()
                btnFavorite?.isEnabled = true
                Toast.makeText(
                    requireContext(),
                    "No se pudo comprobar favoritos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    private fun toggleFavorite() {
        val user = UserSession.getUser()
        val product = currentProduct

        if (user == null || product == null) {
            Toast.makeText(
                requireContext(),
                "Debes iniciar sesión",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        isProcessingFavorite = true
        btnFavorite?.isEnabled = false

        if (isFavorite) {
            ApiService.Post.deleteFavorite(
                context = requireContext(),
                idUser = user.id_user,
                idCoffe = product.id_coffe,
                onResult = {
                    isFavorite = false
                    updateFavoriteIcon()
                    isProcessingFavorite = false
                    btnFavorite?.isEnabled = true

                    Toast.makeText(
                        requireContext(),
                        "Eliminado de favoritos",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onError = { error ->
                    isProcessingFavorite = false
                    btnFavorite?.isEnabled = true

                    Toast.makeText(
                        requireContext(),
                        error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        } else {
            ApiService.Post.createFavorite(
                context = requireContext(),
                idUser = user.id_user,
                idCoffe = product.id_coffe,
                onResult = {
                    isFavorite = true
                    updateFavoriteIcon()
                    isProcessingFavorite = false
                    btnFavorite?.isEnabled = true

                    Toast.makeText(
                        requireContext(),
                        "Añadido a favoritos",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onError = { error ->
                    isProcessingFavorite = false
                    btnFavorite?.isEnabled = true

                    Toast.makeText(
                        requireContext(),
                        error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }

    private fun updateFavoriteIcon() {
        btnFavorite?.setImageResource(
            if (isFavorite) R.drawable.ic_favorite
            else R.drawable.ic_favorite_border
        )
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
        val priceText = String.format(Locale.US, "%.2f €", total)
        tvPrice.text = priceText
        btnAddToCart.text = "Agregar al carrito · $priceText"
    }
}
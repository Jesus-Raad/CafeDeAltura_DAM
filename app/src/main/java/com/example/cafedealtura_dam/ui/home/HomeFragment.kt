package com.example.cafedealtura_dam.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.ProductsRepository
import com.example.cafedealtura_dam.data.UserSession
import com.example.cafedealtura_dam.dataAPI.ApiService
import com.example.cafedealtura_dam.model.Products_coffe
import com.example.cafedealtura_dam.utils.applyTopInsets
import com.google.android.material.textfield.TextInputEditText

class HomeFragment : Fragment() {

    private var recommendedProducts: List<Products_coffe> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        view.applyTopInsets()

        setupGreeting(view)
        setupRecommendedViews(view)
        setupOriginNavigation(view)
        setupCategoryNavigation(view)
        setupSearch(view)

        loadProductsAndRecommendations(view)

        return view
    }

    private fun setupGreeting(view: View) {
        val nombre = UserSession.getUser()?.name ?: "Usuario"
        val tvSaludo = view.findViewById<TextView>(R.id.tvSaludo)
        tvSaludo.text = "Hola, $nombre ☕"
    }

    private fun loadProductsAndRecommendations(view: View) {
        if (!ProductsRepository.isEmpty()) {
            val products = ProductsRepository.getProducts()
            showRecommendedProducts(view, products)
        } else {
            ApiService.Get.getProducts(
                context = requireContext(),
                onResult = { products ->
                    ProductsRepository.setProducts(products)
                    requireActivity().runOnUiThread {
                        showRecommendedProducts(view, products)
                    }
                },
                onError = { error ->
                    println("ERROR API: $error")
                }
            )
        }
    }

    private fun showRecommendedProducts(view: View, products: List<Products_coffe>) {
        val imgRec1 = view.findViewById<ImageView>(R.id.imgRec1)
        val tvRecName1 = view.findViewById<TextView>(R.id.tvRecName1)
        val tvRecOrigin1 = view.findViewById<TextView>(R.id.tvRecOrigin1)
        val tvRecMeta1 = view.findViewById<TextView>(R.id.tvRecMeta1)
        val tvRecPrice1 = view.findViewById<TextView>(R.id.tvRecPrice1)

        val imgRec2 = view.findViewById<ImageView>(R.id.imgRec2)
        val tvRecName2 = view.findViewById<TextView>(R.id.tvRecName2)
        val tvRecOrigin2 = view.findViewById<TextView>(R.id.tvRecOrigin2)
        val tvRecMeta2 = view.findViewById<TextView>(R.id.tvRecMeta2)
        val tvRecPrice2 = view.findViewById<TextView>(R.id.tvRecPrice2)

        recommendedProducts = products.shuffled().take(2)

        if (recommendedProducts.isNotEmpty()) {
            bindRecommendedProduct(
                product = recommendedProducts[0],
                imageView = imgRec1,
                nameView = tvRecName1,
                originView = tvRecOrigin1,
                metaView = tvRecMeta1,
                priceView = tvRecPrice1
            )
        }

        if (recommendedProducts.size > 1) {
            bindRecommendedProduct(
                product = recommendedProducts[1],
                imageView = imgRec2,
                nameView = tvRecName2,
                originView = tvRecOrigin2,
                metaView = tvRecMeta2,
                priceView = tvRecPrice2
            )
        }
    }

    private fun setupRecommendedViews(view: View) {
        val cardRecommended1 = view.findViewById<View>(R.id.cardRecommended1)
        val cardRecommended2 = view.findViewById<View>(R.id.cardRecommended2)

        cardRecommended1.setOnClickListener {
            if (recommendedProducts.isNotEmpty()) {
                val bundle = Bundle().apply {
                    putInt("id_coffe", recommendedProducts[0].id_coffe)
                }
                findNavController().navigate(R.id.productDetailFragment, bundle)
            }
        }

        cardRecommended2.setOnClickListener {
            if (recommendedProducts.size > 1) {
                val bundle = Bundle().apply {
                    putInt("id_coffe", recommendedProducts[1].id_coffe)
                }
                findNavController().navigate(R.id.productDetailFragment, bundle)
            }
        }
    }

    private fun setupOriginNavigation(view: View) {
        val imgOriginCostaRica = view.findViewById<ImageView>(R.id.imgOriginCostaRica)
        val imgOriginColombia = view.findViewById<ImageView>(R.id.imgOriginColombia)
        val imgOriginEtiopia = view.findViewById<ImageView>(R.id.imgOriginEtiopia)
        val imgOriginKenia = view.findViewById<ImageView>(R.id.imgOriginKenia)
        val imgOriginLaos = view.findViewById<ImageView>(R.id.imgOriginLaos)

        Glide.with(requireContext())
            .load("https://res.cloudinary.com/dcfvpqztb/image/upload/v1774039781/origen_costarica_urioc7.png")
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(imgOriginCostaRica)

        Glide.with(requireContext())
            .load("https://res.cloudinary.com/dcfvpqztb/image/upload/v1774039781/origen_colombia_omygqz.png")
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(imgOriginColombia)

        Glide.with(requireContext())
            .load("https://res.cloudinary.com/dcfvpqztb/image/upload/v1774039781/origen_etiopia_ayx2cd.png")
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(imgOriginEtiopia)

        Glide.with(requireContext())
            .load("https://res.cloudinary.com/dcfvpqztb/image/upload/v1774039781/origen_kenia_lshnti.png")
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(imgOriginKenia)

        Glide.with(requireContext())
            .load("https://res.cloudinary.com/dcfvpqztb/image/upload/v1774039781/origen_laos_g9zymj.png")
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(imgOriginLaos)

        imgOriginCostaRica.setOnClickListener {
            navigateToFilteredProducts("origin", "Costa Rica")
        }

        imgOriginColombia.setOnClickListener {
            navigateToFilteredProducts("origin", "Colombia")
        }

        imgOriginEtiopia.setOnClickListener {
            navigateToFilteredProducts("origin", "Etiopía")
        }

        imgOriginKenia.setOnClickListener {
            navigateToFilteredProducts("origin", "Kenia")
        }

        imgOriginLaos.setOnClickListener {
            navigateToFilteredProducts("origin", "Laos")
        }
    }

    private fun setupCategoryNavigation(view: View) {
        val cardCategoryGrano = view.findViewById<View>(R.id.cardCategoryGrano)
        val cardCategoryMolido = view.findViewById<View>(R.id.cardCategoryMolido)
        val cardCategoryEspecial = view.findViewById<View>(R.id.cardCategoryEspecial)

        cardCategoryGrano.setOnClickListener {
            navigateToFilteredProducts("category", "En grano")
        }

        cardCategoryMolido.setOnClickListener {
            navigateToFilteredProducts("category", "Molido")
        }

        cardCategoryEspecial.setOnClickListener {
            navigateToFilteredProducts("category", "Especial")
        }
    }

    private fun setupSearch(view: View) {
        val etSearch = view.findViewById<TextInputEditText>(R.id.etSearch)

        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                val query = etSearch.text?.toString()?.trim().orEmpty()

                if (query.isNotEmpty()) {
                    navigateToFilteredProducts("query", query)
                }

                true
            } else {
                false
            }
        }
    }

    private fun navigateToFilteredProducts(key: String, value: String) {
        val bundle = Bundle().apply {
            putString(key, value)
        }
        findNavController().navigate(R.id.filteredProductsFragment, bundle)
    }

    private fun bindRecommendedProduct(
        product: Products_coffe,
        imageView: ImageView,
        nameView: TextView,
        originView: TextView,
        metaView: TextView,
        priceView: TextView
    ) {
        Glide.with(requireContext())
            .load(product.img_url)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(imageView)

        nameView.text = product.brand
        originView.text = product.origin ?: "Origen desconocido"
        metaView.text = "Disponibles: ${product.available}"
        priceView.text = String.format("$%.2f", product.price)
    }
}
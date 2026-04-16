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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.ProductsRepository
import com.example.cafedealtura_dam.data.UserSession
import com.example.cafedealtura_dam.dataAPI.ApiService
import com.example.cafedealtura_dam.ui.products.ProductsAdapter
import com.example.cafedealtura_dam.utils.applyTopInsets
import com.google.android.material.textfield.TextInputEditText

class HomeFragment : Fragment() {

    private lateinit var recommendedAdapter: ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        view.applyTopInsets()

        setupGreeting(view)
        setupRecommendedRecycler(view)
        setupOriginNavigation(view)
        setupCategoryNavigation(view)
        setupSearch(view)

        loadProductsAndRecommendations()

        return view
    }

    private fun setupGreeting(view: View) {
        val nombre = UserSession.getUser()?.name ?: "Usuario"
        val tvSaludo = view.findViewById<TextView>(R.id.tvSaludo)
        tvSaludo.text = "Hola, $nombre ☕"
    }

    private fun setupRecommendedRecycler(view: View) {
        val rvRecommended = view.findViewById<RecyclerView>(R.id.rvRecommended)

        recommendedAdapter = ProductsAdapter { product ->
            val bundle = Bundle().apply {
                putInt("id_coffe", product.id_coffe)
            }

            findNavController().navigate(
                R.id.productDetailFragment,
                bundle
            )
        }

        rvRecommended.layoutManager = GridLayoutManager(requireContext(), 2)
        rvRecommended.adapter = recommendedAdapter
        rvRecommended.isNestedScrollingEnabled = false
    }

    private fun loadProductsAndRecommendations() {
        if (!ProductsRepository.isEmpty()) {
            val products = ProductsRepository.getProducts()
            showRecommendedProducts(products)
        } else {
            ApiService.Get.getProducts(
                context = requireContext(),
                onResult = { products ->
                    ProductsRepository.setProducts(products)
                    requireActivity().runOnUiThread {
                        showRecommendedProducts(products)
                    }
                },
                onError = { error ->
                    println("ERROR API: $error")
                }
            )
        }
    }

    private fun showRecommendedProducts(products: List<com.example.cafedealtura_dam.model.Products_coffe>) {
        val recommendedProducts = products.shuffled().take(2)
        recommendedAdapter.submitList(recommendedProducts)
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
}
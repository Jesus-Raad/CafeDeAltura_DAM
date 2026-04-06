package com.example.cafedealtura_dam.ui.home

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.repository.ProductsRepository
import com.example.cafedealtura_dam.ui.products.ProductsAdapter
import com.example.cafedealtura_dam.ui.products.ProductUiModel
import com.example.cafedealtura_dam.utils.applyTopInsets
import kotlinx.coroutines.launch
import java.text.Normalizer


class FilteredProductsFragment : Fragment(R.layout.fragment_filtered_products) {

    private var category: String? = null
    private var origin: String? = null

    private var query: String? = null

    private fun normalizeText(text: String): String {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .lowercase()
            .trim()
    }

    private lateinit var adapter: ProductsAdapter
    private val repository = ProductsRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category = arguments?.getString("category")
        origin = arguments?.getString("origin")
        query = arguments?.getString("query")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.applyTopInsets()

        val btnBack = view.findViewById<ImageButton>(R.id.btnBackFiltered)
        val tvTitle = view.findViewById<TextView>(R.id.tvFilteredTitle)
        val tvCount = view.findViewById<TextView>(R.id.tvFilteredCount)
        val rvProducts = view.findViewById<RecyclerView>(R.id.rvFilteredProducts)

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        adapter = ProductsAdapter(emptyList()) { product ->
            val bundle = Bundle().apply {
                putString("name", product.name)
                putString("origin", product.origin)
                putDouble("price", product.price)
                putString("image", product.imageUrl)
                putString("description", product.description)
                putDouble("rating", product.rating)
            }

            findNavController().navigate(
                R.id.productDetailFragment,
                bundle
            )
        }
        rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        rvProducts.adapter = adapter

        tvTitle.text = when {
            !origin.isNullOrBlank() -> origin
            !category.isNullOrBlank() -> category
            else -> "Productos"
        }

        lifecycleScope.launch {
            val allProducts = repository.getProducts()

            val normalizedQuery = query?.let { normalizeText(it) }

            val filteredProducts = allProducts.filter { product ->

                val productName = normalizeText(product.name)
                val productOrigin = normalizeText(product.origin)
                val productDescription = normalizeText(product.description)
                val productCategory = normalizeText(product.category)

                val matchesCategory = category.isNullOrBlank() ||
                        productCategory == normalizeText(category!!)

                val matchesOrigin = origin.isNullOrBlank() ||
                        productOrigin.contains(normalizeText(origin!!)) ||
                        productName.contains(normalizeText(origin!!))

                val matchesQuery = normalizedQuery.isNullOrBlank() ||
                        productName.contains(normalizedQuery) ||
                        productOrigin.contains(normalizedQuery) ||
                        productDescription.contains(normalizedQuery)

                matchesCategory && matchesOrigin && matchesQuery
            }
            adapter.updateData(filteredProducts)
            tvCount.text = "${filteredProducts.size} cafés disponibles"
        }
    }
}
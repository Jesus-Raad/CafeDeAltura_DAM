package com.example.cafedealtura_dam.ui.products

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.repository.ProductsRepository
import kotlinx.coroutines.launch

class ProductsFragment : Fragment(R.layout.fragment_products) {

    private lateinit var adapter: ProductsAdapter
    private val repository = ProductsRepository()
    private var allProducts: List<ProductUiModel> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvProducts = view.findViewById<RecyclerView>(R.id.rvProducts)
        val tvCount = view.findViewById<TextView>(R.id.tvCount)

        val btnTodos = view.findViewById<Button>(R.id.btnTodos)
        val btnGrano = view.findViewById<Button>(R.id.btnGrano)
        val btnMolido = view.findViewById<Button>(R.id.btnMolido)
        val btnEspecial = view.findViewById<Button>(R.id.btnEspecial)
        val filterButtons = listOf(btnTodos, btnGrano, btnMolido, btnEspecial)

        adapter = ProductsAdapter(emptyList())
        rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        rvProducts.adapter = adapter

        fun setActiveButton(active: Button) {
            filterButtons.forEach { btn ->
                btn.setBackgroundResource(R.drawable.bg_filter_inactive)
                btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.cafe_marron_suave))
            }
            active.setBackgroundResource(R.drawable.bg_filter_active)
            active.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        }

        fun applyFilter(category: String, activeBtn: Button) {
            setActiveButton(activeBtn)
            adapter.applyFilter(category)
            val count = if (category == "Todos") allProducts.size
            else allProducts.count { it.category.equals(category, ignoreCase = true) }
            tvCount.text = "$count cafés disponibles"
        }

        btnTodos.setOnClickListener { applyFilter("Todos", btnTodos) }
        btnGrano.setOnClickListener { applyFilter("En grano", btnGrano) }
        btnMolido.setOnClickListener { applyFilter("Molido", btnMolido) }
        btnEspecial.setOnClickListener { applyFilter("Especial", btnEspecial) }

        setActiveButton(btnTodos)

        lifecycleScope.launch {
            allProducts = repository.getProducts()
            adapter.updateData(allProducts)
            tvCount.text = "${allProducts.size} cafés disponibles"
        }
    }
}
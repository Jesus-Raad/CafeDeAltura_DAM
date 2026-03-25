package com.example.cafedealtura_dam.ui.car_shop

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.CartRepository
import com.example.cafedealtura_dam.dataAPI.ApiService
import com.example.cafedealtura_dam.model.Products_coffe
import java.util.Locale

class CartFragment : Fragment(R.layout.fragment_cart) {

    private lateinit var adapter: CarsItemsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CarsItemsAdapter {
            updateCartUI()
        }

        val rv = view.findViewById<RecyclerView>(R.id.rvCartProducts)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        loadTestProductsIfNeeded()
        updateCartUI()
    }

    override fun onResume() {
        super.onResume()
        updateCartUI()
    }

    private fun loadTestProductsIfNeeded() {
        if (!CartRepository.isEmpty()) return

        ApiService.Get.getProducts(
            context = requireContext(),
            onResult = { listaProductos ->

                for (product in listaProductos) {
                    CartRepository.addProduct(product)
                }

                // si tienes adapter o recycler, aquí actualizas
                adapter.submitList(CartRepository.getItems())

            },
            onError = { error ->
                println("ERROR API: $error")
            }
        )
    }

        private fun updateCartUI() {
        val currentView = view ?: return

        val tvCount = currentView.findViewById<TextView>(R.id.tvCartCount)
        val tvSubtotalValue = currentView.findViewById<TextView>(R.id.tvSubtotalValue)
        val tvShippingValue = currentView.findViewById<TextView>(R.id.tvShippingValue)
        val tvTotalValue = currentView.findViewById<TextView>(R.id.tvTotalValue)

        adapter.submitList(CartRepository.getItems())

        tvCount.text = "${CartRepository.getTotalItemsCount()} cafés seleccionados"
        tvSubtotalValue.text = formatPrice(CartRepository.getSubtotal())
        tvShippingValue.text = if (CartRepository.hasFreeShipping()) {
            "Gratis"
        } else {
            formatPrice(CartRepository.getShippingCost())
        }
        tvTotalValue.text = formatPrice(CartRepository.getTotal())
    }

    private fun formatPrice(value: Double): String {
        return String.format(Locale.US, "%.2f €", value)
    }
}
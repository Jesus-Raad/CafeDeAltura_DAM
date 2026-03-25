package com.example.cafedealtura_dam.ui.car_shop

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.CartRepository
import com.example.cafedealtura_dam.model.Products_coffe
import java.util.Locale

class CartFragment : Fragment(R.layout.fragment_cart) {

    private lateinit var adapter: CarsItemsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                v.paddingLeft,
                systemBars.top,
                v.paddingRight,
                v.paddingBottom
            )

            insets
        }

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

        val products = listOf(
            Products_coffe(
                1,
                "Colombia La Casita",
                12.50,
                R.drawable.ic_launcher_background,
                true,
                "Cafés de Origen",
                250,
                "Colombia",
                "Este es un café de origen colombiano"
            ),
            Products_coffe(
                2,
                "Etiopía Yirgacheffe",
                18.90,
                R.drawable.ic_launcher_background,
                true,
                "Cafés de Origen",
                250,
                "Etiopía",
                "Este es un café de origen etíope"
            ),
            Products_coffe(
                3,
                "Brasil Santos",
                14.20,
                R.drawable.ic_launcher_background,
                true,
                "Cafés de Origen",
                250,
                "Brasil",
                "Este es un café de origen brasileño"
            ),
            Products_coffe(
                4,
                "Guatemala Antigua",
                16.40,
                R.drawable.ic_launcher_background,
                true,
                "Cafés de Origen",
                250,
                "Guatemala",
                "Este es un café de origen guatemalteco"
            )
        )

        for (product in products) {
            CartRepository.addProduct(product)
        }
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
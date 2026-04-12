package com.example.cafedealtura_dam.ui.car_shop

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.CartRepository
import com.google.android.material.button.MaterialButton
import java.util.Locale

class CartFragment : Fragment(R.layout.fragment_cart) {

    private lateinit var adapter: CarsItemsAdapter
    private lateinit var btnCheckout: MaterialButton

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

        btnCheckout = view.findViewById(R.id.btnCheckout)

        btnCheckout.setOnClickListener {
            if (CartRepository.getItems().isNotEmpty()) {
                findNavController().navigate(R.id.paymentFragment)
            }
        }

        updateCartUI()
    }

    override fun onResume() {
        super.onResume()
        updateCartUI()
    }

    private fun updateCartUI() {
        val currentView = view ?: return

        val tvCount = currentView.findViewById<TextView>(R.id.tvCartCount)
        val tvSubtotalValue = currentView.findViewById<TextView>(R.id.tvSubtotalValue)
        val tvShippingValue = currentView.findViewById<TextView>(R.id.tvShippingValue)
        val tvTotalValue = currentView.findViewById<TextView>(R.id.tvTotalValue)

        val items = CartRepository.getItems()

        adapter.submitList(items)

        tvCount.text = "${CartRepository.getTotalItemsCount()} cafés seleccionados"
        tvSubtotalValue.text = formatPrice(CartRepository.getSubtotal())
        tvShippingValue.text = if (CartRepository.hasFreeShipping()) {
            "Gratis"
        } else {
            formatPrice(CartRepository.getShippingCost())
        }
        tvTotalValue.text = formatPrice(CartRepository.getTotal())

        updateCheckoutButtonState(items.isEmpty())
    }

    private fun updateCheckoutButtonState(isEmpty: Boolean) {
        btnCheckout.isEnabled = !isEmpty
        btnCheckout.alpha = if (isEmpty) 0.5f else 1f
    }

    private fun formatPrice(value: Double): String {
        return String.format(Locale.US, "%.2f €", value)
    }
}
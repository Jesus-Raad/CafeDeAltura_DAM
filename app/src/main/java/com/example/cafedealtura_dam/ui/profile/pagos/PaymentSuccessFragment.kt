package com.example.cafedealtura_dam.ui.profile.pagos

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.CheckoutSession
import com.example.cafedealtura_dam.utils.applyTopInsets
import com.google.android.material.button.MaterialButton

class PaymentSuccessFragment : Fragment(R.layout.fragment_payment_success) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.applyTopInsets()

        val tvOrderId = view.findViewById<TextView>(R.id.tvOrderId)
        val tvProductsCount = view.findViewById<TextView>(R.id.tvProductsCount)
        val tvSubtotalSuccess = view.findViewById<TextView>(R.id.tvSubtotalSuccess)
        val tvShippingSuccess = view.findViewById<TextView>(R.id.tvShippingSuccess)
        val tvTotalSuccess = view.findViewById<TextView>(R.id.tvTotalSuccess)
        val tvSelectedAddress = view.findViewById<TextView>(R.id.tvSelectedAddressSuccess)

        val totalItems = CheckoutSession.items.sumOf { it.quantity }

        tvOrderId.text = CheckoutSession.orderCode
        tvProductsCount.text = "$totalItems artículo${if (totalItems == 1) "" else "s"}"

        tvSubtotalSuccess.text = "%.2f €".format(CheckoutSession.subtotal)
        tvShippingSuccess.text =
            if (CheckoutSession.shipping == 0.0) "Gratis" else "%.2f €".format(CheckoutSession.shipping)

        tvTotalSuccess.text = "%.2f €".format(CheckoutSession.total)

        val btnContinue = view.findViewById<MaterialButton>(R.id.btnContinue)

        val address = CheckoutSession.selectedAddress

        tvSelectedAddress.text = if (address != null) {
            "Dirección de envío\n${address.label}\n${address.receiver}\n${address.street}\n${address.city}\nCP: ${address.postal_code}\n${address.phone}"
        } else {
            "Dirección de envío\nNo se seleccionó dirección"
        }

        btnContinue.setOnClickListener {
            CheckoutSession.clear()
            findNavController().navigate(R.id.homeFragment)
        }
    }
}
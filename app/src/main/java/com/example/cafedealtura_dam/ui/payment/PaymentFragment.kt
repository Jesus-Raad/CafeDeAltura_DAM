package com.example.cafedealtura_dam.ui.payment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.CartRepository
import com.google.android.material.button.MaterialButton
import com.example.cafedealtura_dam.utils.applyTopInsets

class PaymentFragment : Fragment(R.layout.fragment_payment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.applyTopInsets()

        val btnBack = view.findViewById<ImageView>(R.id.btnBack)
        val btnPay = view.findViewById<MaterialButton>(R.id.btnPayNow)

        val tvSubtotal = view.findViewById<TextView>(R.id.tvSubtotalValue)
        val tvShipping = view.findViewById<TextView>(R.id.tvShippingValue)
        val tvTotal = view.findViewById<TextView>(R.id.tvTotalValue)

        val subtotal = CartRepository.getSubtotal()
        val shipping = CartRepository.getShippingCost()
        val total = CartRepository.getTotal()

        tvSubtotal.text = "€%.2f".format(subtotal)
        tvShipping.text = if (shipping == 0.0) "Gratis" else "€%.2f".format(shipping)
        tvTotal.text = "€%.2f".format(total)

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        btnPay.setOnClickListener {
            // DEMO por ahora
            findNavController().navigate(R.id.action_paymentFragment_to_paymentSuccessFragment)
        }
    }
}
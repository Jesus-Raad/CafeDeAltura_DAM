package com.example.cafedealtura_dam.ui.profile.pagos

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.CartRepository
import com.example.cafedealtura_dam.data.UserSession
import com.example.cafedealtura_dam.dataAPI.ApiService
import com.example.cafedealtura_dam.utils.applyTopInsets
import com.google.android.material.button.MaterialButton

class PaymentFragment : Fragment(R.layout.fragment_payment) {

    private lateinit var btnPay: MaterialButton
    private var isProcessingPayment = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.applyTopInsets()

        val btnBack = view.findViewById<ImageView>(R.id.btnBack)
        btnPay = view.findViewById(R.id.btnPayNow)

        val tvSubtotal = view.findViewById<TextView>(R.id.tvSubtotalValue)
        val tvShipping = view.findViewById<TextView>(R.id.tvShippingValue)
        val tvTotal = view.findViewById<TextView>(R.id.tvTotalValue)

        renderSummary(tvSubtotal, tvShipping, tvTotal)
        updatePayButtonState()

        btnBack.setOnClickListener {
            if (!isProcessingPayment) {
                findNavController().popBackStack()
            }
        }

        btnPay.setOnClickListener {
            if (isProcessingPayment) return@setOnClickListener

            if (CartRepository.isEmpty()) {
                Toast.makeText(requireContext(), "El carrito está vacío", Toast.LENGTH_SHORT).show()
                updatePayButtonState()
                return@setOnClickListener
            }

            val user = UserSession.getUser()
            if (user == null) {
                Toast.makeText(requireContext(), "No hay usuario iniciado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            isProcessingPayment = true
            updatePayButtonState()

            ApiService.Post.createOrder(
                context = requireContext(),
                idUser = user.id_user,
                items = CartRepository.getItems(),
                shippingCost = CartRepository.getShippingCost(),
                onResult = {
                    CartRepository.clearCart()

                    isProcessingPayment = false
                    updatePayButtonState()

                    findNavController().navigate(R.id.action_paymentFragment_to_paymentSuccessFragment)
                },
                onError = { error ->
                    isProcessingPayment = false
                    updatePayButtonState()

                    Toast.makeText(
                        requireContext(),
                        error.ifBlank { "Error al crear el pedido" },
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }

    private fun renderSummary(
        tvSubtotal: TextView,
        tvShipping: TextView,
        tvTotal: TextView
    ) {
        val subtotal = CartRepository.getSubtotal()
        val shipping = CartRepository.getShippingCost()
        val total = CartRepository.getTotal()

        tvSubtotal.text = "€%.2f".format(subtotal)
        tvShipping.text = if (shipping == 0.0) "Gratis" else "€%.2f".format(shipping)
        tvTotal.text = "€%.2f".format(total)
    }

    private fun updatePayButtonState() {
        val enabled = !CartRepository.isEmpty() && !isProcessingPayment

        btnPay.isEnabled = enabled
        btnPay.alpha = if (enabled) 1f else 0.5f
        btnPay.text = if (isProcessingPayment) "Procesando..." else "Pagar con PayPal"
    }
}
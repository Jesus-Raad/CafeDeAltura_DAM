package com.example.cafedealtura_dam.ui.profile.pagos

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.utils.applyTopInsets

class PaymentMethodsFragment : Fragment(R.layout.fragment_payment_methods) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.applyTopInsets()

        val btnBack = view.findViewById<ImageView>(R.id.btnBack)
        val cardPaypal = view.findViewById<View>(R.id.cardPaypal)

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        cardPaypal.setOnClickListener {
            findNavController().navigate(R.id.paymentFragment)
        }
    }
}
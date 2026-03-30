package com.example.cafedealtura_dam.ui.payment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cafedealtura_dam.R
import com.google.android.material.button.MaterialButton

class PaymentSuccessFragment : Fragment(R.layout.fragment_payment_success) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnContinue = view.findViewById<MaterialButton>(R.id.btnContinue)

        btnContinue.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
    }
}
package com.example.cafedealtura_dam.ui.autent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.utils.applyTopInsets

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        view.applyTopInsets()

        val tvLogin = view.findViewById<View>(R.id.tvLogin)
        val btnBack = view.findViewById<ImageView>(R.id.btnBack)

        tvLogin.setOnClickListener {
            requireActivity().onBackPressed()
        }

        btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return view
    }
}
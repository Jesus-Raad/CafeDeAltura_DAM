package com.example.cafedealtura_dam.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R

class OrdersFragment : Fragment(R.layout.fragment_orders) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack = view.findViewById<ImageView>(R.id.btnBack)
        val rvOrders = view.findViewById<RecyclerView>(R.id.rvOrders)

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        rvOrders.layoutManager = LinearLayoutManager(requireContext())


    }
}
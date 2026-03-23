package com.example.cafedealtura_dam.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.adapter.CarsItemsAdapter
import com.example.cafedealtura_dam.data.CartRepository
import com.example.cafedealtura_dam.model.Products_coffe


class CartFragment: Fragment(R.layout.fragment_cart) {


    private val adapter = CarsItemsAdapter()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.rvCartProducts)
        val tvCount = view.findViewById<TextView>(R.id.tvCartCount)

        rv.layoutManager = GridLayoutManager(requireContext(), 2)
        rv.adapter = adapter

        val products = listOf(
            Products_coffe(1,"Colombia La Casita", 2.50, R.drawable.coffee_colombia_la_casita, true, "Cafés de Origen",250,"colombia"),
            Products_coffe(2,"Colombia La Casita", 2.50, R.drawable.coffee_colombia_la_casita, true, "Cafés de Origen",250,"colombia"),
            Products_coffe(3,"Colombia La Casita", 2.50, R.drawable.coffee_colombia_la_casita, true, "Cafés de Origen",250,"colombia"),
            Products_coffe(1,"Colombia La Casita", 2.50, R.drawable.coffee_colombia_la_casita, true, "Cafés de Origen",250,"colombia")

        )




        for (product in products) {
            CartRepository.addProduct(product)

        }



        tvCount.text = "${products.size} cafés disponibles"
        adapter.submitList(CartRepository.getItems())
    }
}
package com.example.cafedealtura_dam.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R

class OrdersFragment : Fragment(R.layout.fragment_orders) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack = view.findViewById<ImageView>(R.id.btnBack)
        val tvSubtitle = view.findViewById<TextView>(R.id.tvSubtitle)
        val rvOrders = view.findViewById<RecyclerView>(R.id.rvOrders)

        val demoOrders = listOf(
            OrderUiModel(
                date = "5 de marzo, 2026",
                orderCode = "ORD-2024-001",
                status = "Entregado",
                total = 53.80,
                items = listOf(
                    OrderItemUiModel(
                        "Kenia Molido",
                        1,
                        "https://res.cloudinary.com/dcfvpqztb/image/upload/v1774027240/cafe_kenia_molido_x1jjco.png"
                    ),
                    OrderItemUiModel(
                        "Colombia Molido",
                        2,
                        "https://res.cloudinary.com/dcfvpqztb/image/upload/v1774027240/cafe_colombia_molido_ln55th.png"
                    ),
                    OrderItemUiModel(
                        "Kenia en grano",
                        1,
                        "https://res.cloudinary.com/dcfvpqztb/image/upload/v1774027240/cafe_kenia_grano_gvmjoc.png"
                    )
                )
            )
        )

        tvSubtitle.text = if (demoOrders.size == 1) {
            "1 pedido"
        } else {
            "${demoOrders.size} pedidos"
        }

        rvOrders.layoutManager = LinearLayoutManager(requireContext())
        rvOrders.adapter = OrdersAdapter(demoOrders) { selectedOrder ->
            val bundle = Bundle().apply {
                putString("orderCode", selectedOrder.orderCode)
            }
            findNavController().navigate(R.id.orderDetailFragment, bundle)
        }

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
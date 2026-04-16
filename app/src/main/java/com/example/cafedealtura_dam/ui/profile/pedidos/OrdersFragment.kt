package com.example.cafedealtura_dam.ui.profile.pedidos

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.ProductsRepository
import com.example.cafedealtura_dam.data.UserSession
import com.example.cafedealtura_dam.dataAPI.ApiService
import com.example.cafedealtura_dam.model.Orders
import com.example.cafedealtura_dam.model.Orders_item
import com.example.cafedealtura_dam.utils.applyTopInsets

class OrdersFragment : Fragment(R.layout.fragment_orders) {

    private lateinit var btnBack: ImageView
    private lateinit var tvSubtitle: TextView
    private lateinit var rvOrders: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.applyTopInsets()

        btnBack = view.findViewById(R.id.btnBack)
        tvSubtitle = view.findViewById(R.id.tvSubtitle)
        rvOrders = view.findViewById(R.id.rvOrders)

        rvOrders.layoutManager = LinearLayoutManager(requireContext())

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        val user = UserSession.getUser()
        if (user == null) {
            Toast.makeText(requireContext(), "No hay usuario logueado", Toast.LENGTH_SHORT).show()
            tvSubtitle.text = "0 pedidos"
            rvOrders.adapter = OrdersAdapter(emptyList(), emptyMap()) { }
            return
        }

        ensureProductsLoadedAndLoadOrders(user.id_user)
    }

    private fun ensureProductsLoadedAndLoadOrders(idUser: Int) {
        if (ProductsRepository.isEmpty()) {
            ApiService.Get.getProducts(
                context = requireContext(),
                onResult = { products ->
                    ProductsRepository.setProducts(products)
                    loadOrders(idUser)
                },
                onError = { error ->
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    tvSubtitle.text = "0 pedidos"
                    rvOrders.adapter = OrdersAdapter(emptyList(), emptyMap()) { }
                }
            )
        } else {
            loadOrders(idUser)
        }
    }

    private fun loadOrders(idUser: Int) {
        ApiService.Get.getOrders(
            context = requireContext(),
            idUser = idUser,
            onResult = { orders ->
                if (orders.isEmpty()) {
                    tvSubtitle.text = "0 pedidos"
                    rvOrders.adapter = OrdersAdapter(emptyList(), emptyMap()) { }
                } else {
                    tvSubtitle.text = if (orders.size == 1) "1 pedido" else "${orders.size} pedidos"
                    loadItemsForOrders(orders)
                }
            },
            onError = { error ->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                tvSubtitle.text = "0 pedidos"
                rvOrders.adapter = OrdersAdapter(emptyList(), emptyMap()) { }
            }
        )
    }

    private fun loadItemsForOrders(orders: List<Orders>) {
        val itemsByOrderId = mutableMapOf<Int, List<Orders_item>>()
        var pendingRequests = orders.size

        for (order in orders) {
            ApiService.Get.getOrderItems(
                context = requireContext(),
                idOrder = order.id_order,
                onResult = { items ->
                    itemsByOrderId[order.id_order] = items
                    pendingRequests--

                    if (pendingRequests == 0) {
                        showOrders(orders, itemsByOrderId)
                    }
                },
                onError = {
                    itemsByOrderId[order.id_order] = emptyList()
                    pendingRequests--

                    if (pendingRequests == 0) {
                        showOrders(orders, itemsByOrderId)
                    }
                }
            )
        }
    }

    private fun showOrders(
        orders: List<Orders>,
        itemsByOrderId: Map<Int, List<Orders_item>>
    ) {
        rvOrders.adapter = OrdersAdapter(
            orders = orders,
            itemsByOrderId = itemsByOrderId,
            onDetailClick = { selectedOrder ->
                val bundle = Bundle().apply {
                    putInt("id_order", selectedOrder.id_order)
                }
                findNavController().navigate(R.id.orderDetailFragment, bundle)
            }
        )
    }
}
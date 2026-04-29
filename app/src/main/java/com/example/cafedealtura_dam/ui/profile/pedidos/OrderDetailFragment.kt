package com.example.cafedealtura_dam.ui.profile.pedidos

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.CartRepository
import com.example.cafedealtura_dam.data.ProductsRepository
import com.example.cafedealtura_dam.data.UserSession
import com.example.cafedealtura_dam.dataAPI.ApiService
import com.example.cafedealtura_dam.model.Orders
import com.example.cafedealtura_dam.model.Orders_item
import com.example.cafedealtura_dam.utils.applyTopInsets

class OrderDetailFragment : Fragment(R.layout.fragment_order_detail) {

    private lateinit var tvOrderId: TextView
    private lateinit var tvOrderDate: TextView
    private lateinit var tvOrderStatus: TextView
    private lateinit var rvOrderProducts: RecyclerView
    private lateinit var tvSubtotal: TextView
    private lateinit var tvShipping: TextView
    private lateinit var tvTotal: TextView
    private lateinit var btnReorder: Button

    private var currentOrder: Orders? = null
    private var currentItems: List<Orders_item> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.applyTopInsets()

        val btnBack = view.findViewById<ImageView>(R.id.btnBack)

        tvOrderId = view.findViewById(R.id.tvOrderId)
        tvOrderDate = view.findViewById(R.id.tvOrderDate)
        tvOrderStatus = view.findViewById(R.id.tvOrderStatus)
        rvOrderProducts = view.findViewById(R.id.rvOrderProducts)
        tvSubtotal = view.findViewById(R.id.tvSubtotal)
        tvShipping = view.findViewById(R.id.tvShipping)
        tvTotal = view.findViewById(R.id.tvTotal)
        btnReorder = view.findViewById(R.id.btnReorder)

        rvOrderProducts.layoutManager = LinearLayoutManager(requireContext())

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        val idOrder = arguments?.getInt("id_order", -1) ?: -1

        if (idOrder == -1) {
            Toast.makeText(requireContext(), "No se encontró el pedido", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        cargarDetallePedido(idOrder)

        btnReorder.setOnClickListener {
            repetirPedido()
        }
    }

    private fun cargarDetallePedido(idOrder: Int) {
        val user = UserSession.getUser()

        if (user == null) {
            Toast.makeText(requireContext(), "No hay usuario logueado", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        if (ProductsRepository.isEmpty()) {
            ApiService.Get.getProducts(
                context = requireContext(),
                onResult = { products ->
                    ProductsRepository.setProducts(products)
                    cargarPedidoEItems(user.id_user, idOrder)
                },
                onError = { error ->
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
            )
        } else {
            cargarPedidoEItems(user.id_user, idOrder)
        }
    }

    private fun cargarPedidoEItems(idUser: Int, idOrder: Int) {
        ApiService.Get.getOrders(
            context = requireContext(),
            idUser = idUser,
            onResult = { orders ->
                val order = orders.find { it.id_order == idOrder }

                if (order == null) {
                    Toast.makeText(requireContext(), "Pedido no encontrado", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                    return@getOrders
                }

                currentOrder = order

                ApiService.Get.getOrderItems(
                    context = requireContext(),
                    idOrder = idOrder,
                    onResult = { items ->
                        currentItems = items
                        mostrarPedido(order, items)
                    },
                    onError = { error ->
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                        mostrarPedido(order, emptyList())
                    }
                )
            },
            onError = { error ->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun mostrarPedido(order: Orders, items: List<Orders_item>) {
        tvOrderId.text = "Pedido #${order.id_order}"
        tvOrderDate.text = order.date
        tvOrderStatus.text = getStatusText(order.status_order)

        rvOrderProducts.adapter = OrderItemsAdapter(items)

        val subtotal = items.sumOf { it.total_amount }
        val total = order.total_amount
        val shipping = (total - subtotal).coerceAtLeast(0.0)

        tvSubtotal.text = "€%.2f".format(subtotal)
        tvShipping.text = if (shipping == 0.0) "Gratis" else "€%.2f".format(shipping)
        tvTotal.text = "€%.2f".format(total)
    }

    private fun repetirPedido() {
        if (currentItems.isEmpty()) {
            Toast.makeText(requireContext(), "Este pedido no tiene productos", Toast.LENGTH_SHORT).show()
            return
        }

        currentItems.forEach { item ->
            val product = ProductsRepository.getProductById(item.id_product)

            if (product != null) {
                CartRepository.addProduct(product, item.quantity)
            }
        }

        Toast.makeText(requireContext(), "Productos añadidos al carrito", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.cartFragment)
    }

    private fun getStatusText(status: Int): String {
        return when (status) {
            0 -> "Pendiente"
            1 -> "Completado"
            2 -> "Cancelado"
            else -> "Desconocido"
        }
    }
}
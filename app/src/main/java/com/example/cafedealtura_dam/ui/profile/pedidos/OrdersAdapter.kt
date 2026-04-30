package com.example.cafedealtura_dam.ui.profile.pedidos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.ProductsRepository
import com.example.cafedealtura_dam.model.Orders
import com.example.cafedealtura_dam.model.Orders_item

class OrdersAdapter(
    private val orders: List<Orders>,
    private val itemsByOrderId: Map<Int, List<Orders_item>>,
    private val onDetailClick: (Orders) -> Unit,
    private val onRepeatClick: (List<Orders_item>) -> Unit
) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvOrderDate: TextView = view.findViewById(R.id.tvOrderDate)
        val tvOrderCode: TextView = view.findViewById(R.id.tvOrderCode)
        val tvOrderStatus: TextView = view.findViewById(R.id.tvOrderStatus)
        val tvOrderTotal: TextView = view.findViewById(R.id.tvOrderTotal)

        val ivOrderProduct: ImageView = view.findViewById(R.id.ivOrderProduct)
        val tvOrderProductName: TextView = view.findViewById(R.id.tvOrderProductName)
        val tvOrderQuantity: TextView = view.findViewById(R.id.tvOrderQuantity)

        val rvOrderItems: RecyclerView = view.findViewById(R.id.rvOrderItems)

        val btnOrderDetail: TextView = view.findViewById(R.id.btnOrderDetail)
        val btnOrderRepeat: TextView = view.findViewById(R.id.btnOrderRepeat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        val orderItems = itemsByOrderId[order.id_order].orEmpty()

        holder.tvOrderDate.text = order.date
        holder.tvOrderCode.text = "Pedido #${order.id_order}"
        holder.tvOrderStatus.text = getStatusText(order.status_order)
        holder.tvOrderTotal.text = "€%.2f".format(order.total_amount)

        val firstItem = orderItems.firstOrNull()

        if (firstItem != null) {
            val product = ProductsRepository.getProductById(firstItem.id_product)

            holder.tvOrderProductName.text = product?.brand ?: "Producto #${firstItem.id_product}"
            holder.tvOrderQuantity.text = "Cantidad: ${firstItem.quantity}"

            Glide.with(holder.itemView.context)
                .load(product?.img_url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.ivOrderProduct)
        } else {
            holder.tvOrderProductName.text = "Sin productos"
            holder.tvOrderQuantity.text = ""

            Glide.with(holder.itemView.context)
                .load(R.drawable.ic_launcher_foreground)
                .into(holder.ivOrderProduct)
        }

        holder.rvOrderItems.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.rvOrderItems.adapter = OrderItemsAdapter(orderItems)
        holder.rvOrderItems.isNestedScrollingEnabled = false

        holder.btnOrderDetail.visibility = View.VISIBLE
        holder.btnOrderRepeat.visibility = View.VISIBLE

        holder.btnOrderDetail.setOnClickListener {
            onDetailClick(order)
        }

        holder.btnOrderRepeat.setOnClickListener {
            onRepeatClick(orderItems)
        }
    }

    override fun getItemCount(): Int = orders.size

    private fun getStatusText(status: Int): String {
        return when (status) {
            0 -> "Pendiente"
            1 -> "Completado"
            2 -> "Cancelado"
            else -> "Desconocido"
        }
    }
}
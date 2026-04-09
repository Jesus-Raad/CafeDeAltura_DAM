package com.example.cafedealtura_dam.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cafedealtura_dam.R

class OrdersAdapter(
    private val orders: List<OrderUiModel>,
    private val onDetailClick: (OrderUiModel) -> Unit
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

        // Datos básicos
        holder.tvOrderDate.text = order.date
        holder.tvOrderCode.text = order.orderCode
        holder.tvOrderStatus.text = order.status
        holder.tvOrderTotal.text = "€%.2f".format(order.total)

        // Primer producto del pedido
        val firstItem = order.items.firstOrNull()

        if (firstItem != null) {
            holder.tvOrderProductName.text = firstItem.productName
            holder.tvOrderQuantity.text = "Cantidad: ${firstItem.quantity}"

            Glide.with(holder.itemView.context)
                .load(firstItem.imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.ivOrderProduct)
        } else {
            holder.tvOrderProductName.text = "Sin productos"
            holder.tvOrderQuantity.text = ""

            Glide.with(holder.itemView.context)
                .load(R.drawable.ic_launcher_foreground)
                .into(holder.ivOrderProduct)
        }

        // Recycler interno (productos del pedido)
        holder.rvOrderItems.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.rvOrderItems.adapter = OrderItemsAdapter(order.items)
        holder.rvOrderItems.isNestedScrollingEnabled = false

        // Botones
        holder.btnOrderDetail.visibility = View.VISIBLE
        holder.btnOrderRepeat.visibility = View.VISIBLE

        holder.btnOrderDetail.setOnClickListener {
            onDetailClick(order)
        }

        holder.btnOrderRepeat.setOnClickListener {
            // Aquí luego puedes implementar repetir pedido
        }
    }

    override fun getItemCount(): Int = orders.size
}
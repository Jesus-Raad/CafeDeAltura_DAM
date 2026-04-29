package com.example.cafedealtura_dam.data

import com.example.cafedealtura_dam.model.CartItem
import com.example.cafedealtura_dam.model.Direccion
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CheckoutSession {

    var orderCode: String = ""
    var items: List<CartItem> = emptyList()
    var subtotal: Double = 0.0
    var shipping: Double = 0.0
    var total: Double = 0.0
    var selectedAddress: Direccion? = null

    fun saveOrderSnapshot() {
        orderCode = generateOrderCode()
        items = CartRepository.getItems()
        subtotal = CartRepository.getSubtotal()
        shipping = CartRepository.getShippingCost()
        total = CartRepository.getTotal()
    }

    fun clear() {
        orderCode = ""
        items = emptyList()
        subtotal = 0.0
        shipping = 0.0
        total = 0.0
        selectedAddress = null
    }

    private fun generateOrderCode(): String {
        val date = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        return "ORD-$date"
    }
}
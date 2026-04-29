package com.example.cafedealtura_dam.ui.profile.pagos

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.CartRepository
import com.example.cafedealtura_dam.data.CheckoutSession
import com.example.cafedealtura_dam.data.UserSession
import com.example.cafedealtura_dam.dataAPI.ApiService
import com.example.cafedealtura_dam.model.Direccion
import com.example.cafedealtura_dam.ui.profile.direcciones.AddressDialogHelper
import com.example.cafedealtura_dam.ui.profile.direcciones.DireccionesAdapter
import com.example.cafedealtura_dam.utils.applyTopInsets

class DireccionPagosFragment : Fragment(R.layout.fragment_direccion_pagos) {

    private lateinit var adapter: DireccionesAdapter
    private lateinit var rvDirecciones: RecyclerView

    private var currentUserId: Int? = null
    private var listaDirecciones: List<Direccion> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.applyTopInsets()

        currentUserId = UserSession.getUserId()

        if (currentUserId == null) {
            Toast.makeText(requireContext(), "No hay usuario en sesión", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        rvDirecciones = view.findViewById(R.id.rvDirecciones)

        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        val btnAddAddress = view.findViewById<ImageButton>(R.id.btnAddAddress)
        val btnAddNew = view.findViewById<View>(R.id.btnAddNewAddress)
        val btnContinue = view.findViewById<Button>(R.id.btnContinueToPayment)

        val tvSubtotal = view.findViewById<TextView>(R.id.tvSubtotal)
        val tvShipping = view.findViewById<TextView>(R.id.tvShipping)
        val tvTotal = view.findViewById<TextView>(R.id.tvTotal)

        renderSummary(tvSubtotal, tvShipping, tvTotal)

        adapter = DireccionesAdapter(
            onEditar = { direccion ->
                AddressDialogHelper.showAddressDialog(
                    context = requireContext(),
                    userId = currentUserId ?: return@DireccionesAdapter,
                    direccionExistente = direccion,
                    onSaved = { cargarDirecciones() }
                )
            },
            onEliminar = { },
            onSeleccionar = { direccion ->
                CheckoutSession.selectedAddress = direccion
                Toast.makeText(
                    requireContext(),
                    "Dirección seleccionada: ${direccion.label}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        rvDirecciones.layoutManager = LinearLayoutManager(requireContext())
        rvDirecciones.adapter = adapter

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        btnAddAddress.setOnClickListener {
            AddressDialogHelper.showAddressDialog(
                context = requireContext(),
                userId = currentUserId ?: return@setOnClickListener,
                direccionExistente = null,
                onSaved = { cargarDirecciones() }
            )
        }

        btnAddNew.setOnClickListener {
            AddressDialogHelper.showAddressDialog(
                context = requireContext(),
                userId = currentUserId ?: return@setOnClickListener,
                direccionExistente = null,
                onSaved = { cargarDirecciones() }
            )
        }

        btnContinue.setOnClickListener {
            val direccionSeleccionada = adapter.getSelectedAddress()

            if (direccionSeleccionada == null) {
                Toast.makeText(
                    requireContext(),
                    "Selecciona una dirección antes de continuar",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                CheckoutSession.selectedAddress = direccionSeleccionada
                findNavController().navigate(R.id.paymentFragment)
            }
        }

        cargarDirecciones()
    }

    private fun cargarDirecciones() {
        val userId = currentUserId ?: return

        ApiService.Get.getAddresses(
            context = requireContext(),
            idUser = userId,
            onResult = { direcciones ->
                listaDirecciones = direcciones
                adapter.submitList(listaDirecciones)
            },
            onError = { error ->
                Toast.makeText(
                    requireContext(),
                    "Error cargando direcciones: $error",
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }

    private fun renderSummary(
        tvSubtotal: TextView,
        tvShipping: TextView,
        tvTotal: TextView
    ) {
        val subtotal = CartRepository.getSubtotal()
        val shipping = CartRepository.getShippingCost()
        val total = CartRepository.getTotal()

        tvSubtotal.text = "%.2f €".format(subtotal)
        tvShipping.text = if (shipping == 0.0) "Gratis" else "%.2f €".format(shipping)
        tvTotal.text = "%.2f €".format(total)
    }
}
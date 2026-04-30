package com.example.cafedealtura_dam.ui.profile.pagos

import android.app.AlertDialog
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

    private var draftDireccion: AddressDialogHelper.AddressFormDraft? = null
    private var direccionTemporal: Direccion? = null

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
            onEditar = { direccion -> mostrarDialogo(direccion) },
            onEliminar = { direccion -> confirmarEliminarDireccion(direccion) },
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
            mostrarDialogo(null)
        }

        btnAddNew.setOnClickListener {
            mostrarDialogo(null)
        }

        parentFragmentManager.setFragmentResultListener("location_result", viewLifecycleOwner) { _, bundle ->
            val draftActual = draftDireccion ?: AddressDialogHelper.AddressFormDraft()

            draftDireccion = draftActual.copy(
                street = bundle.getString("street").orEmpty(),
                city = bundle.getString("city").orEmpty(),
                postalCode = bundle.getString("postalCode").orEmpty()
            )

            mostrarDialogo(direccionTemporal, draftDireccion)
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

    private fun mostrarDialogo(
        direccionExistente: Direccion?,
        draft: AddressDialogHelper.AddressFormDraft? = null
    ) {
        AddressDialogHelper.showAddressDialog(
            context = requireContext(),
            direccionExistente = direccionExistente,
            draft = draft,
            onSelectLocation = { currentDraft ->
                draftDireccion = currentDraft
                direccionTemporal = direccionExistente
                findNavController().navigate(R.id.seleccionarUbicacionFragment)
            },
            onSave = { formData ->
                val userId = currentUserId ?: return@showAddressDialog

                if (direccionExistente == null) {
                    crearDireccion(
                        userId = userId,
                        label = formData.label,
                        receiver = formData.receiver,
                        street = formData.street,
                        city = formData.city,
                        postalCode = formData.postalCode,
                        phone = formData.phone,
                        isDefault = formData.isDefault,
                        onSuccess = {
                            limpiarEstadoTemporal()
                        }
                    )
                } else {
                    actualizarDireccion(
                        idAddress = direccionExistente.id_address,
                        userId = userId,
                        label = formData.label,
                        receiver = formData.receiver,
                        street = formData.street,
                        city = formData.city,
                        postalCode = formData.postalCode,
                        phone = formData.phone,
                        isDefault = formData.isDefault,
                        onSuccess = {
                            limpiarEstadoTemporal()
                        }
                    )
                }
            }
        )
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

    private fun crearDireccion(
        userId: Int,
        label: String,
        receiver: String,
        street: String,
        city: String,
        postalCode: String,
        phone: String,
        isDefault: Int,
        onSuccess: () -> Unit
    ) {
        ApiService.Post.createAddress(
            context = requireContext(),
            idUser = userId,
            label = label,
            receiver = receiver,
            street = street,
            city = city,
            postalCode = postalCode,
            phone = phone,
            isDefault = isDefault,
            onResult = { response ->
                Toast.makeText(
                    requireContext(),
                    response.message ?: "Dirección creada correctamente",
                    Toast.LENGTH_SHORT
                ).show()
                cargarDirecciones()
                onSuccess()
            },
            onError = { error ->
                Toast.makeText(
                    requireContext(),
                    "Error creando dirección: $error",
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }

    private fun actualizarDireccion(
        idAddress: Int,
        userId: Int,
        label: String,
        receiver: String,
        street: String,
        city: String,
        postalCode: String,
        phone: String,
        isDefault: Int,
        onSuccess: () -> Unit
    ) {
        ApiService.Post.updateAddress(
            context = requireContext(),
            idAddress = idAddress,
            idUser = userId,
            label = label,
            receiver = receiver,
            street = street,
            city = city,
            postalCode = postalCode,
            phone = phone,
            isDefault = isDefault,
            onResult = { response ->
                Toast.makeText(
                    requireContext(),
                    response.message ?: "Dirección actualizada correctamente",
                    Toast.LENGTH_SHORT
                ).show()
                cargarDirecciones()
                onSuccess()
            },
            onError = { error ->
                Toast.makeText(
                    requireContext(),
                    "Error actualizando dirección: $error",
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }

    private fun confirmarEliminarDireccion(direccion: Direccion) {
        val userId = currentUserId ?: return

        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar dirección")
            .setMessage("¿Seguro que quieres eliminar \"${direccion.label}\"?")
            .setPositiveButton("Eliminar") { _, _ ->
                ApiService.Post.deleteAddress(
                    context = requireContext(),
                    idAddress = direccion.id_address,
                    idUser = userId,
                    onResult = { response ->
                        Toast.makeText(
                            requireContext(),
                            response.message ?: "Dirección eliminada correctamente",
                            Toast.LENGTH_SHORT
                        ).show()

                        if (CheckoutSession.selectedAddress?.id_address == direccion.id_address) {
                            CheckoutSession.selectedAddress = null
                        }

                        cargarDirecciones()
                    },
                    onError = { error ->
                        Toast.makeText(
                            requireContext(),
                            "Error eliminando dirección: $error",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            }
            .setNegativeButton("Cancelar", null)
            .show()
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

    private fun limpiarEstadoTemporal() {
        draftDireccion = null
        direccionTemporal = null
    }
}
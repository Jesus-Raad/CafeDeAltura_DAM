package com.example.cafedealtura_dam.ui.profile.direcciones

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.UserSession
import com.example.cafedealtura_dam.dataAPI.ApiService
import com.example.cafedealtura_dam.model.Direccion
import com.example.cafedealtura_dam.utils.applyTopInsets

class DireccionesFragment : Fragment(R.layout.fragment_direcciones) {

    private lateinit var adapter: DireccionesAdapter
    private lateinit var tvContador: TextView
    private lateinit var rvDirecciones: RecyclerView

    private var currentUserId: Int? = null
    private var listaDirecciones: List<Direccion> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.applyTopInsets()

        currentUserId = UserSession.getUserId()

        if (currentUserId == null) {
            Toast.makeText(requireContext(), "No hay usuario en sesión", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
            return
        }

        rvDirecciones = view.findViewById(R.id.rvDirecciones)
        tvContador = view.findViewById(R.id.tvAddressCount)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        val btnAddAddress = view.findViewById<ImageButton>(R.id.btnAddAddress)
        val btnAddNew = view.findViewById<View>(R.id.btnAddNewAddress)

        adapter = DireccionesAdapter(
            onEditar = { direccion ->
                AddressDialogHelper.showAddressDialog(
                    context = requireContext(),
                    userId = currentUserId ?: return@DireccionesAdapter,
                    direccionExistente = direccion,
                    onSaved = { cargarDirecciones() }
                )
            },
            onEliminar = { direccion -> confirmarEliminarDireccion(direccion) }
        )

        rvDirecciones.layoutManager = LinearLayoutManager(requireContext())
        rvDirecciones.adapter = adapter

        btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
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

        cargarDirecciones()
    }

    private fun cargarDirecciones() {
        val userId = currentUserId ?: return

        ApiService.Get.getAddresses(
            context = requireContext(),
            idUser = userId,
            onResult = { direcciones ->
                listaDirecciones = direcciones
                refrescarLista()
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

    private fun mostrarDialogo(direccionExistente: Direccion?) {
        val userId = currentUserId ?: return

        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_direccion, null)

        val etLabel = dialogView.findViewById<EditText>(R.id.etLocation)
        val etReceiver = dialogView.findViewById<EditText>(R.id.etReceptor)
        val etStreet = dialogView.findViewById<EditText>(R.id.etStreet)
        val etCity = dialogView.findViewById<EditText>(R.id.etCity)
        val etPostalCode = dialogView.findViewById<EditText>(R.id.etCodigoPostal)
        val etPhone = dialogView.findViewById<EditText>(R.id.etTelefono)
        val cbDefault = dialogView.findViewById<CheckBox>(R.id.cbDefault)

        direccionExistente?.let { direccion ->
            etLabel.setText(direccion.label)
            etReceiver.setText(direccion.receiver)
            etStreet.setText(direccion.street)
            etCity.setText(direccion.city)
            etPostalCode.setText(direccion.postal_code)
            etPhone.setText(direccion.phone)
            cbDefault.isChecked = direccion.is_default == 1
        }

        val titulo = if (direccionExistente == null) {
            "Nueva dirección"
        } else {
            "Editar dirección"
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(titulo)
            .setView(dialogView)
            .setPositiveButton("Guardar", null)
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.setOnShowListener {
            val btnGuardar = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            btnGuardar.setOnClickListener {
                val label = etLabel.text.toString().trim()
                val receiver = etReceiver.text.toString().trim()
                val street = etStreet.text.toString().trim()
                val city = etCity.text.toString().trim()
                val postalCode = etPostalCode.text.toString().trim()
                val phone = etPhone.text.toString().trim()
                val isDefault = if (cbDefault.isChecked) 1 else 0

                if (
                    label.isEmpty() ||
                    receiver.isEmpty() ||
                    street.isEmpty() ||
                    city.isEmpty() ||
                    postalCode.isEmpty() ||
                    phone.isEmpty()
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Todos los campos son obligatorios",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                if (direccionExistente == null) {
                    crearDireccion(
                        userId = userId,
                        label = label,
                        receiver = receiver,
                        street = street,
                        city = city,
                        postalCode = postalCode,
                        phone = phone,
                        isDefault = isDefault,
                        onSuccess = {
                            dialog.dismiss()
                        }
                    )
                } else {
                    actualizarDireccion(
                        idAddress = direccionExistente.id_address,
                        userId = userId,
                        label = label,
                        receiver = receiver,
                        street = street,
                        city = city,
                        postalCode = postalCode,
                        phone = phone,
                        isDefault = isDefault,
                        onSuccess = {
                            dialog.dismiss()
                        }
                    )
                }
            }
        }

        dialog.show()
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

    private fun refrescarLista() {
        adapter.submitList(listaDirecciones)

        val n = listaDirecciones.size
        tvContador.text = if (n == 1) {
            "1 dirección guardada"
        } else {
            "$n direcciones guardadas"
        }
    }
}
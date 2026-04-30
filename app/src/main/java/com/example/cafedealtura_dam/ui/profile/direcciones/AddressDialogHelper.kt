package com.example.cafedealtura_dam.ui.profile.direcciones

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.dataAPI.ApiService
import com.example.cafedealtura_dam.model.Direccion

object AddressDialogHelper {

    fun showAddressDialog(
        context: Context,
        userId: Int,
        direccionExistente: Direccion? = null,
        onSaved: () -> Unit
    ) {
        val dialogView = LayoutInflater.from(context)
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

        val title = if (direccionExistente == null) {
            "Nueva dirección"
        } else {
            "Editar dirección"
        }

        val dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setView(dialogView)
            .setPositiveButton("Guardar", null)
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.setOnShowListener {
            val btnSave = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            btnSave.setOnClickListener {
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
                        context,
                        "Todos los campos son obligatorios",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                if (direccionExistente == null) {
                    ApiService.Post.createAddress(
                        context = context,
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
                                context,
                                response.message ?: "Dirección creada correctamente",
                                Toast.LENGTH_SHORT
                            ).show()

                            dialog.dismiss()
                            onSaved()
                        },
                        onError = { error ->
                            Toast.makeText(
                                context,
                                "Error creando dirección: $error",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                } else {
                    ApiService.Post.updateAddress(
                        context = context,
                        idAddress = direccionExistente.id_address,
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
                                context,
                                response.message ?: "Dirección actualizada correctamente",
                                Toast.LENGTH_SHORT
                            ).show()

                            dialog.dismiss()
                            onSaved()
                        },
                        onError = { error ->
                            Toast.makeText(
                                context,
                                "Error actualizando dirección: $error",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                }
            }
        }

        dialog.show()
    }
}
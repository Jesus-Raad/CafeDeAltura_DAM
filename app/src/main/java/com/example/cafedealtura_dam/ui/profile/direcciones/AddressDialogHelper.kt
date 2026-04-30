package com.example.cafedealtura_dam.ui.profile.direcciones

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.model.Direccion

object AddressDialogHelper {

    data class AddressFormDraft(
        val label: String = "",
        val receiver: String = "",
        val street: String = "",
        val city: String = "",
        val postalCode: String = "",
        val phone: String = "",
        val isDefault: Boolean = false
    )

    data class AddressFormData(
        val label: String,
        val receiver: String,
        val street: String,
        val city: String,
        val postalCode: String,
        val phone: String,
        val isDefault: Int
    )

    fun showAddressDialog(
        context: Context,
        direccionExistente: Direccion? = null,
        draft: AddressFormDraft? = null,
        onSelectLocation: (AddressFormDraft) -> Unit,
        onSave: (AddressFormData) -> Unit
    ) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_direccion, null)

        val btnSelectLocation = dialogView.findViewById<View>(R.id.btnSelectLocation)
        val etLabel = dialogView.findViewById<EditText>(R.id.etLocation)
        val etReceiver = dialogView.findViewById<EditText>(R.id.etReceptor)
        val etStreet = dialogView.findViewById<EditText>(R.id.etStreet)
        val etCity = dialogView.findViewById<EditText>(R.id.etCity)
        val etPostalCode = dialogView.findViewById<EditText>(R.id.etCodigoPostal)
        val etPhone = dialogView.findViewById<EditText>(R.id.etTelefono)
        val cbDefault = dialogView.findViewById<CheckBox>(R.id.cbDefault)

        if (draft != null) {
            etLabel.setText(draft.label)
            etReceiver.setText(draft.receiver)
            etStreet.setText(draft.street)
            etCity.setText(draft.city)
            etPostalCode.setText(draft.postalCode)
            etPhone.setText(draft.phone)
            cbDefault.isChecked = draft.isDefault
        } else {
            direccionExistente?.let { direccion ->
                etLabel.setText(direccion.label)
                etReceiver.setText(direccion.receiver)
                etStreet.setText(direccion.street)
                etCity.setText(direccion.city)
                etPostalCode.setText(direccion.postal_code)
                etPhone.setText(direccion.phone)
                cbDefault.isChecked = direccion.is_default == 1
            }
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

            btnSelectLocation.setOnClickListener {
                val currentDraft = AddressFormDraft(
                    label = etLabel.text.toString(),
                    receiver = etReceiver.text.toString(),
                    street = etStreet.text.toString(),
                    city = etCity.text.toString(),
                    postalCode = etPostalCode.text.toString(),
                    phone = etPhone.text.toString(),
                    isDefault = cbDefault.isChecked
                )

                dialog.dismiss()
                onSelectLocation(currentDraft)
            }

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

                onSave(
                    AddressFormData(
                        label = label,
                        receiver = receiver,
                        street = street,
                        city = city,
                        postalCode = postalCode,
                        phone = phone,
                        isDefault = isDefault
                    )
                )

                dialog.dismiss()
            }
        }

        dialog.show()
    }
}
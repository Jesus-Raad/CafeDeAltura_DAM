package com.example.cafedealtura_dam.ui.profile

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.model.Direccion
import com.example.cafedealtura_dam.utils.applyTopInsets

class DireccionesFragment : Fragment(R.layout.fragment_direcciones) {

    private lateinit var adapter: DireccionesAdapter
    private lateinit var tvContador: TextView

    private val listaDirecciones = mutableListOf(
        Direccion(
            id = 1,
            id_user = 1,
            location = "Casa",
            receptor = "María García",
            street = "Calle Principal #123",
            city = "Bogotá, Colombia",
            poste_code = "110111",
            phone = "+57 300 123 4567"
        ),
        Direccion(
            id = 2,
            id_user = 1,
            location = "Trabajo",
            receptor = "María García",
            street = "Avenida Central #456",
            city = "Bogotá, Colombia",
            poste_code = "110222",
            phone = "+57 333 123 4567"
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.applyTopInsets()

        val rvDirecciones = view.findViewById<RecyclerView>(R.id.rvDirecciones)
        tvContador = view.findViewById(R.id.tvAddressCount)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        val btnAddAddress = view.findViewById<ImageButton>(R.id.btnAddAddress)
        val btnAddNew = view.findViewById<View>(R.id.btnAddNewAddress)

        adapter = DireccionesAdapter(
            onEditar = { direccion -> mostrarDialogo(direccion) },
            onEliminar = { direccion -> eliminarDireccion(direccion) }
        )

        rvDirecciones.layoutManager = LinearLayoutManager(requireContext())
        rvDirecciones.adapter = adapter
        refrescarLista()

        btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        btnAddAddress.setOnClickListener { mostrarDialogo(null) }
        btnAddNew.setOnClickListener { mostrarDialogo(null) }
    }

    private fun mostrarDialogo(direccionExistente: Direccion?) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_direccion, null)

        val etLocation  = dialogView.findViewById<EditText>(R.id.etLocation)
        val etReceptor  = dialogView.findViewById<EditText>(R.id.etReceptor)
        val etStreet    = dialogView.findViewById<EditText>(R.id.etStreet)
        val etCity      = dialogView.findViewById<EditText>(R.id.etCity)
        val etCP        = dialogView.findViewById<EditText>(R.id.etCodigoPostal)
        val etTelefono  = dialogView.findViewById<EditText>(R.id.etTelefono)

        direccionExistente?.let {
            etLocation.setText(it.location)
            etReceptor.setText(it.receptor)
            etStreet.setText(it.street)
            etCity.setText(it.city)
            etCP.setText(it.poste_code.removePrefix("CP: "))
            etTelefono.setText(it.phone)
        }

        val titulo = if (direccionExistente == null) "Nueva dirección" else "Editar dirección"

        AlertDialog.Builder(requireContext())
            .setTitle(titulo)
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val location = etLocation.text.toString().trim()
                val receptor = etReceptor.text.toString().trim()
                val street   = etStreet.text.toString().trim()
                val city     = etCity.text.toString().trim()
                val cp       = etCP.text.toString().trim()
                val tel      = etTelefono.text.toString().trim()

                if (location.isEmpty() || street.isEmpty()) {
                    Toast.makeText(requireContext(), "Location y street son obligatorios", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (direccionExistente == null) {
                    val nuevoId = (listaDirecciones.maxOfOrNull { it.id } ?: 0) + 1
                    listaDirecciones.add(
                        Direccion(
                            id = nuevoId,
                            id_user = 1,
                            location = location,
                            receptor = receptor,
                            street = street,
                            city = city,
                            poste_code = "CP: $cp",
                            phone = tel
                        )
                    )
                    Toast.makeText(requireContext(), "Dirección añadida", Toast.LENGTH_SHORT).show()
                } else {
                    val index = listaDirecciones.indexOfFirst { it.id == direccionExistente.id }
                    if (index >= 0) {
                        listaDirecciones[index] = direccionExistente.copy(
                            location = location,
                            receptor = receptor,
                            street = street,
                            city = city,
                            poste_code = "CP: $cp",
                            phone = tel
                        )
                    }
                    Toast.makeText(requireContext(), "Dirección actualizada", Toast.LENGTH_SHORT).show()
                }
                refrescarLista()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarDireccion(direccion: Direccion) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar dirección")
            .setMessage("¿Seguro que quieres eliminar \"${direccion.location}\"?")
            .setPositiveButton("Eliminar") { _, _ ->
                listaDirecciones.removeIf { it.id == direccion.id }
                refrescarLista()
                Toast.makeText(requireContext(), "${direccion.location} eliminada", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun refrescarLista() {
        adapter.submitList(listaDirecciones.toList())
        val n = listaDirecciones.size
        tvContador.text = if (n == 1) "1 dirección guardada" else "$n direcciones guardadas"
    }
}
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
            nombre = "Casa",
            nombrePersona = "María García",
            calle = "Calle Principal #123",
            ciudad = "Bogotá, Colombia",
            codigoPostal = "CP: 110111",
            telefono = "+57 300 123 4567",
            esPredeterminada = true
        ),
        Direccion(
            id = 2,
            nombre = "Oficina",
            nombrePersona = "María García",
            calle = "Av. El Dorado #45-67",
            ciudad = "Bogotá, Colombia",
            codigoPostal = "CP: 110221",
            telefono = "+57 300 123 4567",
            esPredeterminada = false
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
            onEliminar = { direccion -> eliminarDireccion(direccion) },
            onEstablecerPredeterminada = { direccion -> establecerPredeterminada(direccion) }
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

        val etNombre       = dialogView.findViewById<EditText>(R.id.etNombre)
        val etNombrePersona = dialogView.findViewById<EditText>(R.id.etNombrePersona)
        val etCalle        = dialogView.findViewById<EditText>(R.id.etCalle)
        val etCiudad       = dialogView.findViewById<EditText>(R.id.etCiudad)
        val etCP           = dialogView.findViewById<EditText>(R.id.etCodigoPostal)
        val etTelefono     = dialogView.findViewById<EditText>(R.id.etTelefono)

        // Si es edición, rellenar campos con datos existentes
        direccionExistente?.let {
            etNombre.setText(it.nombre)
            etNombrePersona.setText(it.nombrePersona)
            etCalle.setText(it.calle)
            etCiudad.setText(it.ciudad)
            etCP.setText(it.codigoPostal.removePrefix("CP: "))
            etTelefono.setText(it.telefono)
        }

        val titulo = if (direccionExistente == null) "Nueva dirección" else "Editar dirección"

        AlertDialog.Builder(requireContext())
            .setTitle(titulo)
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre  = etNombre.text.toString().trim()
                val persona = etNombrePersona.text.toString().trim()
                val calle   = etCalle.text.toString().trim()
                val ciudad  = etCiudad.text.toString().trim()
                val cp      = etCP.text.toString().trim()
                val tel     = etTelefono.text.toString().trim()

                if (nombre.isEmpty() || calle.isEmpty()) {
                    Toast.makeText(requireContext(), "Nombre y calle son obligatorios", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (direccionExistente == null) {
                    // AÑADIR nueva
                    val nuevoId = (listaDirecciones.maxOfOrNull { it.id } ?: 0) + 1
                    listaDirecciones.add(
                        Direccion(
                            id = nuevoId,
                            nombre = nombre,
                            nombrePersona = persona,
                            calle = calle,
                            ciudad = ciudad,
                            codigoPostal = "CP: $cp",
                            telefono = tel,
                            esPredeterminada = listaDirecciones.isEmpty()
                        )
                    )
                    Toast.makeText(requireContext(), "Dirección añadida", Toast.LENGTH_SHORT).show()
                } else {
                    // EDITAR existente
                    val index = listaDirecciones.indexOfFirst { it.id == direccionExistente.id }
                    if (index >= 0) {
                        listaDirecciones[index] = direccionExistente.copy(
                            nombre = nombre,
                            nombrePersona = persona,
                            calle = calle,
                            ciudad = ciudad,
                            codigoPostal = "CP: $cp",
                            telefono = tel
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
            .setMessage("¿Seguro que quieres eliminar \"${direccion.nombre}\"?")
            .setPositiveButton("Eliminar") { _, _ ->
                listaDirecciones.removeIf { it.id == direccion.id }
                refrescarLista()
                Toast.makeText(requireContext(), "${direccion.nombre} eliminada", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun establecerPredeterminada(direccion: Direccion) {
        listaDirecciones.replaceAll { it.copy(esPredeterminada = it.id == direccion.id) }
        refrescarLista()
        Toast.makeText(requireContext(), "${direccion.nombre} es ahora predeterminada", Toast.LENGTH_SHORT).show()
    }

    private fun refrescarLista() {
        adapter.submitList(listaDirecciones.toList())
        val n = listaDirecciones.size
        tvContador.text = if (n == 1) "1 dirección guardada" else "$n direcciones guardadas"
    }
}
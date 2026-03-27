package com.example.cafedealtura_dam.ui.profile

import android.os.Bundle
import android.view.View
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
        val tvContador = view.findViewById<TextView>(R.id.tvAddressCount)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        val btnAddAddress = view.findViewById<ImageButton>(R.id.btnAddAddress)
        val btnAddNew = view.findViewById<View>(R.id.btnAddNewAddress)

        adapter = DireccionesAdapter(
            onEditar = { direccion ->
                Toast.makeText(requireContext(), "Editar: ${direccion.nombre}", Toast.LENGTH_SHORT).show()
                // TODO: abrir diálogo de edición
            },
            onEliminar = { direccion ->
                listaDirecciones.removeIf { it.id == direccion.id }
                adapter.submitList(listaDirecciones.toList())
                actualizarContador(tvContador)
                Toast.makeText(requireContext(), "${direccion.nombre} eliminada", Toast.LENGTH_SHORT).show()
            },
            onEstablecerPredeterminada = { direccion ->
                listaDirecciones.replaceAll { it.copy(esPredeterminada = it.id == direccion.id) }
                adapter.submitList(listaDirecciones.toList())
                Toast.makeText(requireContext(), "${direccion.nombre} establecida como predeterminada", Toast.LENGTH_SHORT).show()
            }
        )

        rvDirecciones.layoutManager = LinearLayoutManager(requireContext())
        rvDirecciones.adapter = adapter
        adapter.submitList(listaDirecciones.toList())
        actualizarContador(tvContador)

        btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val anadirNueva = {
            Toast.makeText(requireContext(), "Añadir nueva dirección", Toast.LENGTH_SHORT).show()
            // TODO: abrir diálogo de nueva dirección
        }

        btnAddAddress.setOnClickListener { anadirNueva() }
        btnAddNew.setOnClickListener { anadirNueva() }
    }

    private fun actualizarContador(tv: TextView) {
        val n = listaDirecciones.size
        tv.text = if (n == 1) "1 dirección guardada" else "$n direcciones guardadas"
    }
}
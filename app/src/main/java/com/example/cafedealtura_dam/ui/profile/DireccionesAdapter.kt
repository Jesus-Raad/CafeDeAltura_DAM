package com.example.cafedealtura_dam.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.model.Direccion

class DireccionesAdapter(
    private val onEditar: (Direccion) -> Unit,
    private val onEliminar: (Direccion) -> Unit
) : RecyclerView.Adapter<DireccionesAdapter.VH>() {

    private var items: List<Direccion> = emptyList()

    fun submitList(nuevaLista: List<Direccion>) {
        items = nuevaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_direccion, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvNombre: TextView   = itemView.findViewById(R.id.tvNombreDireccion)
        private val tvPersona: TextView  = itemView.findViewById(R.id.tvNombrePersona)
        private val tvCalle: TextView    = itemView.findViewById(R.id.tvCalle)
        private val tvCiudad: TextView   = itemView.findViewById(R.id.tvCiudad)
        private val tvCP: TextView       = itemView.findViewById(R.id.tvCodigoPostal)
        private val tvTelefono: TextView = itemView.findViewById(R.id.tvTelefono)
        private val btnEditar: ImageButton   = itemView.findViewById(R.id.btnEditar)
        private val btnEliminar: ImageButton = itemView.findViewById(R.id.btnEliminar)

        fun bind(direccion: Direccion) {
            tvNombre.text   = direccion.location
            tvPersona.text  = direccion.receptor
            tvCalle.text    = direccion.street
            tvCiudad.text   = direccion.city
            tvCP.text       = direccion.poste_code
            tvTelefono.text = direccion.phone

            btnEditar.setOnClickListener { onEditar(direccion) }
            btnEliminar.setOnClickListener { onEliminar(direccion) }
        }
    }
}
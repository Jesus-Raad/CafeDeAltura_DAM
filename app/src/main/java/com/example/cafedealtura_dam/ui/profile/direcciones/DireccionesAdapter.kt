package com.example.cafedealtura_dam.ui.profile.direcciones

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
    private val onEliminar: (Direccion) -> Unit,
    private val onSeleccionar: ((Direccion) -> Unit)? = null
) : RecyclerView.Adapter<DireccionesAdapter.VH>() {

    private var items: List<Direccion> = emptyList()
    private var selectedAddressId: Int? = null

    fun submitList(nuevaLista: List<Direccion>) {
        items = nuevaLista

        if (selectedAddressId == null) {
            selectedAddressId = items.find { it.is_default == 1 }?.id_address
                ?: items.firstOrNull()?.id_address
        }

        notifyDataSetChanged()
    }

    fun getSelectedAddress(): Direccion? {
        return items.find { it.id_address == selectedAddressId }
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

        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombreDireccion)
        private val tvPredeterminada: TextView = itemView.findViewById(R.id.tvPredeterminada)
        private val tvPersona: TextView = itemView.findViewById(R.id.tvNombrePersona)
        private val tvCalle: TextView = itemView.findViewById(R.id.tvCalle)
        private val tvCiudad: TextView = itemView.findViewById(R.id.tvCiudad)
        private val tvCP: TextView = itemView.findViewById(R.id.tvCodigoPostal)
        private val tvTelefono: TextView = itemView.findViewById(R.id.tvTelefono)
        private val btnEditar: ImageButton = itemView.findViewById(R.id.btnEditar)
        private val btnEliminar: ImageButton = itemView.findViewById(R.id.btnEliminar)

        fun bind(direccion: Direccion) {
            tvNombre.text = direccion.label
            tvPersona.text = direccion.receiver
            tvCalle.text = direccion.street
            tvCiudad.text = direccion.city
            tvCP.text = "CP: ${direccion.postal_code}"
            tvTelefono.text = direccion.phone

            tvPredeterminada.visibility =
                if (direccion.is_default == 1) View.VISIBLE else View.GONE

            if (onSeleccionar != null) {
                itemView.isClickable = true
                itemView.isFocusable = true

                itemView.alpha = if (direccion.id_address == selectedAddressId) 1f else 0.65f

                itemView.setOnClickListener {
                    selectedAddressId = direccion.id_address
                    onSeleccionar.invoke(direccion)
                    notifyDataSetChanged()
                }
            } else {
                itemView.alpha = 1f
                itemView.setOnClickListener(null)
            }

            btnEditar.setOnClickListener { onEditar(direccion) }
            btnEliminar.setOnClickListener { onEliminar(direccion) }
        }
    }
}
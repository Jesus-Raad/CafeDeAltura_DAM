package com.example.cafedealtura_dam.ui.profile.direcciones

import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cafedealtura_dam.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class SeleccionarUbicacionFragment : Fragment(R.layout.fragment_seleccionar_ubicacion),
    OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private var selectedLatLng: LatLng? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        val btnConfirm = view.findViewById<Button>(R.id.btnConfirmLocation)

        btnConfirm.setOnClickListener {
            selectedLatLng?.let { latLng ->
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]

                    val street = "${address.thoroughfare ?: ""} ${address.subThoroughfare ?: ""}"
                    val city = "${address.locality ?: ""}, ${address.countryName ?: ""}"
                    val postalCode = address.postalCode ?: ""

                    val result = Bundle().apply {
                        putString("street", street)
                        putString("city", city)
                        putString("postalCode", postalCode)
                    }

                    parentFragmentManager.setFragmentResult("location_result", result)
                    findNavController().popBackStack()
                }
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val defaultLocation = LatLng(40.4168, -3.7038) // Madrid
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f))

        googleMap.setOnMapClickListener { latLng ->
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(latLng))
            selectedLatLng = latLng
        }
    }
}
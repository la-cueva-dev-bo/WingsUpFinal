package com.wingsupfinal

import android.content.Context
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_maps.*


class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private var latus: Double? = null
    private var lonus: Double? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var lastLocation: Location

    override fun onMarkerClick(p0: Marker?) = false

    private lateinit var googleMap: GoogleMap

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        confirMap.onCreate(savedInstanceState)
        confirMap.onResume()

        confirMap.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_maps, container, false)

    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            googleMap = it
        }

        map?.setOnMarkerClickListener(this)
        map?.uiSettings?.isZoomControlsEnabled = true





        map?.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener {location ->

            if(location !=null){

                lastLocation = location

                latus = location.latitude
                lonus = location.longitude

                val currentLatLong = LatLng(latus!!, lonus!!)

                map?.addMarker(MarkerOptions().position(currentLatLong))
                map?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 15f))

                this.confirmacionButton.setOnClickListener {

                    val prefs =
                        requireActivity().getSharedPreferences(
                            getString(R.string.prefs_file),
                            Context.MODE_PRIVATE
                        ).edit()
                    prefs.putString("latUs", latus.toString())
                    prefs.putString("lonUs", lonus.toString())
                    prefs.apply()

                    findNavController().popBackStack()

                }

            }
        }
    }


}

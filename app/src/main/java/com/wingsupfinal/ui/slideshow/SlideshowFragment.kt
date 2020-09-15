package com.wingsupfinal.ui.slideshow

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.type.LatLng
import com.wingsupfinal.R
import java.util.*

class SlideshowFragment : Fragment(), GoogleMap.OnMarkerClickListener {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googlemap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var geocoder: Geocoder
    //  private lateinit var dirUser: <UsersAddress>

    companion object {

        private const val REQUEST_LOCATION = 1001
    }

    private var idProd: String? = null
    private var tipos: String? = null
    private val datos = mutableMapOf<String, Any>()

    private var lonUser: Double? = null
    private var latUser: Double? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idProd = SlideshowFragmentArgs.fromBundle(requireArguments()).idProd
        tipos = SlideshowFragmentArgs.fromBundle(requireArguments()).tipo


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_slideshow, container, false)
        val imgprod = view.findViewById<ImageView>(R.id.imgProdConf)
        val nompr = view.findViewById<TextView>(R.id.nomProdConf)
        val detprod = view.findViewById<TextView>(R.id.detProdConf)
        val precprod = view.findViewById<TextView>(R.id.preProdConf)
        val nomfac = view.findViewById<EditText>(R.id.nombFacConf)
        val nit = view.findViewById<EditText>(R.id.nitFacConf)
        val direccion = view.findViewById<TextView>(R.id.dirConf)
        val refe = view.findViewById<EditText>(R.id.refConf)
        val telef = view.findViewById<EditText>(R.id.telfConf)
        val locButton = view.findViewById<ImageButton>(R.id.locConf)
        val confButton = view.findViewById<Button>(R.id.confirmarPedButton)

        val fb = FirebaseFirestore.getInstance().collection("Productos").document(tipos!!)
            .collection("tipos").document(idProd!!)
        fb.get().addOnSuccessListener { documents ->
            if (documents.exists()) {
                val datos1 = documents.data
                for (dat in datos1!!) {
                    val llave = dat.key
                    val valor = dat.value.toString()
                    datos[llave] = valor
                }
                imgprod.let {
                    Glide.with(requireContext()).load(datos["imgProd"]).into(imgprod)
                }
                nompr.text = datos["nombreProd"].toString()
                detprod.text = datos["descripcion"].toString()
                precprod.text = datos["precio"].toString()
            }
        }

        locButton.setOnClickListener {

            val dialogMap = LayoutInflater.from(context).inflate(R.layout.fragment_maps, null)
            val builderMap: AlertDialog.Builder = AlertDialog.Builder(context).setView(dialogMap)
            val alertMap = builderMap.show()
            val mapView = alertMap.findViewById<MapView>(R.id.confirMap)
            MapsInitializer.initialize(context)
            mapView.onCreate(alertMap.onSaveInstanceState())
            mapView.onResume()
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
            geocoder = Geocoder(requireContext(), Locale.getDefault())

            mapView.getMapAsync { map ->
                map?.let {
                    googlemap = it
                }



                map?.setOnMarkerClickListener(this@SlideshowFragment)
                map?.uiSettings?.isZoomControlsEnabled = true

                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_LOCATION
                    )
                } else {

                    map?.isMyLocationEnabled = true
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->

                        if (location != null) {
                            lastLocation = location

                            latUser = location.latitude
                            lonUser = location.longitude

      //                      val currentLatLng = LatLng(latUser!!, lonUser!!)

                            map?.animateCamera(
                                CameraUpdateFactory.newLatLng(
           //                         currentLatLng, 15f
                //                )


             //               )

                            //             dirUser =

                   //     }


             //       }


        //        }

                //   confButton.setOnClickListener {

                //    }

                }
           }
        }


        return view
    }
}
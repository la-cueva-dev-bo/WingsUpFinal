package com.wingsupfinal.ui.slideshow

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.firebase.firestore.FirebaseFirestore
import com.wingsupfinal.AppWingsUp
import com.wingsupfinal.AuthActivity
import com.wingsupfinal.R

class SlideshowFragment : Fragment(), GoogleMap.OnMarkerClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var fusedLocationClient    : FusedLocationProviderClient
    private lateinit var googlemap              : GoogleMap
    private lateinit var lastLocation           : Location
    private lateinit var geocoder               : Geocoder
    private lateinit var dirUser                : List<Address>

    companion object {

        private const val REQUEST_LOCATION = 1001
    }

    private var idProd  : String? = null
    private var tipos   : String? = null
    private var img : String? = null
    private var precio : String? = null
    private var descr : String? = null
    private var contTotExt: Double = 0.0

    private var user: String? = null
    private var userId: String? = null

    private var lonUser             : Double? = null
    private var latUser             : Double? = null
    private var direction           : String? = null
    private var finalLoc            : TextView? = null
    private var totalPed            : TextView? = null
    private val adapterPedExtras = AdapterPedExtras()
    private val adapterSalsas = AdapterPedExtras()
    private val adapterTopp = AdapterPedExtras()

    private val listaextras = mutableMapOf<String, Any>()

    private var contGral: Int = 0

    private var contextrtotl : Double = 0.0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idProd = SlideshowFragmentArgs.fromBundle(requireArguments()).idProd
        tipos = SlideshowFragmentArgs.fromBundle(requireArguments()).tipo
        img = SlideshowFragmentArgs.fromBundle(requireArguments()).imgprod
        precio = SlideshowFragmentArgs.fromBundle(requireArguments()).precio
        descr = SlideshowFragmentArgs.fromBundle(requireArguments()).descr

        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        val pref = requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        user = pref.getString("user", "")
        userId = pref.getString("userId", "")
    }

    override fun onResume() {
        super.onResume()
        requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener(
            this
        )
    }
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_slideshow, container, false)
        val imgprod = view.findViewById<ImageView>(R.id.imgProdConf)

        val detprod = view.findViewById<TextView>(R.id.detProdConf)
        val precprod = view.findViewById<TextView>(R.id.preProdConf)
        val recyPedExtras = view.findViewById<RecyclerView>(R.id.recyPedExtras)
        val recySalsas = view.findViewById<RecyclerView>(R.id.recySalsas)
        val recyTppings = view.findViewById<RecyclerView>(R.id.recTopp)
        val confButton = view.findViewById<Button>(R.id.confirmarPedButton)
        val backbutton = view.findViewById<ImageButton>(R.id.popBackImageButton)
        val masgral = view.findViewById<ImageView>(R.id.masIV)
        val mengral = view.findViewById<ImageView>(R.id.menosIV)
        val cantgral = view.findViewById<TextView>(R.id.contTV)
        totalPed = view.findViewById(R.id.totalPedConfTextView)

        backbutton.setOnClickListener {
            val builderBack = AlertDialog.Builder(requireContext())
            builderBack.setTitle("¡ATENCIÓN!")
            builderBack.setMessage("Tiene un pedido en curso, ¿desea eliminarlo?")
            builderBack.setPositiveButton("ACEPTAR"){dialog, _ ->
                val fbextr = FirebaseFirestore.getInstance().collection("Productos")
                val fbrefr = fbextr.document("Subextras").collection("Refrescos")
                val fbsal = fbextr.document("Subextras").collection("Salsas")
                val fbtopp = fbextr.document("Toppings").collection("toppings")

                fbrefr.get().addOnSuccessListener { documents->
                    for(docs in documents){
                        val refrextr = docs["nombre"].toString()
                        val pref = requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                        pref.remove(refrextr)
                        pref.apply()

                    }
                    fbsal.get().addOnSuccessListener { documents2->
                        for(docs2 in documents2){
                            val salsextr = docs2["nombre"].toString()
                            val pref = requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                            pref.remove(salsextr)
                            pref.apply()
                        }
                        fbtopp.get().addOnSuccessListener { documents3->
                            for(docs3 in documents3){
                                val toppextr = docs3["nombre"].toString()
                                val pref = requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                                pref.remove(toppextr)
                                pref.apply()
                            }
                            val pref = requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                            pref.remove("totalExtras")
                            pref.apply()
                            findNavController().popBackStack()
                            (activity as AppCompatActivity?)!!.supportActionBar!!.show()
                            dialog.dismiss()

                        }

                    }
                }




            }
            builderBack.setNegativeButton("CANCELAR"){dialog, _->
                dialog.dismiss()
            }
            val alertBack = builderBack.create()
            alertBack.show()

        }





        masgral.setOnClickListener {
            val newcontgral = contGral + 1
            contGral = newcontgral
            cantgral.text = "$contGral"
            val precdob = precio!!.toDouble()
            val prectot = precdob * contGral
            val sumatot= prectot + contextrtotl
            totalPed?.text = "$sumatot Bs."

        }

        mengral.setOnClickListener {
            if(contGral!=0){
                val newcontgral = contGral - 1
                contGral = newcontgral
                cantgral.text = "$contGral"
                val precdob = precio!!.toDouble()
                val prectot = precdob * contGral
                val sumatot= prectot + contextrtotl
                totalPed?.text = "$sumatot Bs."

            }
        }



        imgprod.let{
            Glide.with(requireContext()).load(img).into(imgprod)
        }
        detprod.text = descr
        precprod.text = "$precio Bs."
        val extrasfb = FirebaseFirestore.getInstance().collection("Productos").document("Subextras")
            .collection("Refrescos")
        extrasfb.get().addOnSuccessListener{ documentos->
            val listRefrescos = mutableListOf<DatosPedExtras>()
            for(datosrefr in documentos){
                val idRef = datosrefr.id
                val datRef = datosrefr.data

                val datosRefresco = DatosPedExtras(idRef, datRef, "Refrescos")
                listRefrescos.add(datosRefresco)
            }
            adapterPedExtras.setListData(listRefrescos)
            adapterPedExtras.notifyDataSetChanged()


        }
        val extraSalsa = FirebaseFirestore.getInstance().collection("Productos").document("Subextras")
            .collection("Salsas")
        extraSalsa.get().addOnSuccessListener { documents->
            val listaSalsas = mutableListOf<DatosPedExtras>()
            for(datosSalsas in documents){
                val idSal = datosSalsas.id
                val datSal = datosSalsas.data
                val datSalsas = DatosPedExtras(idSal, datSal, "Salsas")
                listaSalsas.add(datSalsas)
            }
            adapterSalsas.setListData(listaSalsas)
            adapterSalsas.notifyDataSetChanged()
        }

        val toppings = FirebaseFirestore.getInstance().collection("Productos").document("Toppings")
            .collection("toppings")
        toppings.get().addOnSuccessListener { doc->
            val listaTopp = mutableListOf<DatosPedExtras>()
            for(datTop in doc){
                val idTop = datTop.id
                val datTopp = datTop.data
                val datosTopp = DatosPedExtras(idTop, datTopp, "Toppings")
                listaTopp.add(datosTopp)
            }
            adapterTopp.setListData(listaTopp)
            adapterTopp.notifyDataSetChanged()

        }

        recyPedExtras.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyPedExtras.adapter = adapterPedExtras

        recySalsas.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        recySalsas.adapter = adapterSalsas

        recyTppings.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyTppings.adapter = adapterTopp

        confButton.setOnClickListener {

            if(contGral!=0){
                val builderConf = AlertDialog.Builder(requireContext())
                builderConf.setTitle("¡ATENCIÓN!")
                builderConf.setMessage("El total de su pedido es ${totalPed!!.text} ¿desea continuar?")
                builderConf.setPositiveButton("ACEPTAR"){dialog, _->
                    val fbextr = FirebaseFirestore.getInstance().collection("Productos")
                    val fbrefr = fbextr.document("Subextras").collection("Refrescos")
                    val fbsal = fbextr.document("Subextras").collection("Salsas")
                    val fbtopp = fbextr.document("Toppings").collection("toppings")

                    fbrefr.get().addOnSuccessListener { documents ->
                        for (docs in documents) {
                            val refrextr = docs["nombre"].toString()
                            val pref = requireContext().getSharedPreferences(
                                getString(R.string.prefs_file),
                                Context.MODE_PRIVATE
                            )
                            val extra = pref.getString(refrextr, "0")
                            if(extra!= "0"){
                                listaextras[refrextr] = extra.toString()
                                val prefrem = requireContext().getSharedPreferences(
                                    getString(R.string.prefs_file),
                                    Context.MODE_PRIVATE
                                ).edit()
                                prefrem.remove(refrextr)
                                prefrem.apply()
                            }else{
                                val prefrem = requireContext().getSharedPreferences(
                                    getString(R.string.prefs_file),
                                    Context.MODE_PRIVATE
                                ).edit()
                                prefrem.remove(refrextr)
                                prefrem.apply()
                            }


                        }
                        fbsal.get().addOnSuccessListener { documents2 ->
                            for (docs2 in documents2) {
                                val salsextr = docs2["nombre"].toString()
                                val pref = requireContext().getSharedPreferences(
                                    getString(R.string.prefs_file),
                                    Context.MODE_PRIVATE
                                )
                                val extra = pref.getString(salsextr, "0")
                                if(extra!="0"){
                                    listaextras[salsextr] = extra.toString()
                                    val prefrem = requireContext().getSharedPreferences(
                                        getString(R.string.prefs_file),
                                        Context.MODE_PRIVATE
                                    ).edit()
                                    prefrem.remove(salsextr)
                                    prefrem.apply()
                                }else{
                                    val prefrem = requireContext().getSharedPreferences(
                                        getString(R.string.prefs_file),
                                        Context.MODE_PRIVATE
                                    ).edit()
                                    prefrem.remove(salsextr)
                                    prefrem.apply()
                                }

                            }
                            fbtopp.get().addOnSuccessListener { documents3 ->
                                for (docs3 in documents3) {
                                    val toppextr = docs3["nombre"].toString()
                                    val pref = requireContext().getSharedPreferences(
                                        getString(R.string.prefs_file),
                                        Context.MODE_PRIVATE
                                    )
                                    val extra = pref.getString(toppextr, "0")
                                    if(extra!= "0"){
                                        listaextras[toppextr] = extra.toString()
                                        val prefrem = requireContext().getSharedPreferences(
                                            getString(R.string.prefs_file),
                                            Context.MODE_PRIVATE
                                        ).edit()
                                        prefrem.remove(toppextr)
                                        prefrem.apply()
                                    }else{
                                        val prefrem = requireContext().getSharedPreferences(
                                            getString(R.string.prefs_file),
                                            Context.MODE_PRIVATE
                                        ).edit()
                                        prefrem.remove(toppextr)
                                        prefrem.apply()
                                    }

                                }
                                val pref = requireContext().getSharedPreferences(
                                    getString(R.string.prefs_file),
                                    Context.MODE_PRIVATE
                                )
                                val totalextrasgral = pref.getString("totalExtras", "0")
                                if(totalextrasgral!="0"){
                                    listaextras["totalExtras"] = totalextrasgral.toString()
                                    contTotExt = totalextrasgral!!.toDouble()
                                    val prefrem = requireContext().getSharedPreferences(
                                        getString(R.string.prefs_file),
                                        Context.MODE_PRIVATE
                                    ).edit()
                                    prefrem.remove("totalExtras")
                                    prefrem.apply()
                                }else{
                                    val prefrem = requireContext().getSharedPreferences(
                                        getString(R.string.prefs_file),
                                        Context.MODE_PRIVATE
                                    ).edit()
                                    prefrem.remove("totalExtras")
                                    prefrem.apply()
                                }

                                val cond1 = totalPed!!.text.toString().split(" ")
                                val totpedgral = cond1[0]

                                val totalpedidogeneral = totpedgral.toDouble() + contTotExt
                                listaextras["idProducto"] = idProd.toString()
                                listaextras["cantidad"] = contGral.toString()
                                listaextras["totalPedido"] = totalpedidogeneral.toString()
                                listaextras["imgProd"] = img.toString()

                                val fs = FirebaseFirestore.getInstance().collection("Carritos").document(userId!!)
                                fs.set(mapOf(
                                    "nombreUser" to user.toString()
                                )).addOnCompleteListener { task1->
                                    if(task1.isSuccessful){
                                        val fs2 = fs.collection("carrito").document()
                                        fs2.set(listaextras).addOnCompleteListener {task->
                                            if(task.isSuccessful){
                                                val builderfinal = AlertDialog.Builder(requireContext())
                                                builderfinal.setTitle("¡ATENCIÓN!")
                                                builderfinal.setMessage("Su pedido fue añadido a su carrito")
                                                builderfinal.setPositiveButton("ACEPTAR"){dialogo, _->
                                                    val logOut = Intent(AppWingsUp.context, AuthActivity::class.java)
                                                    requireActivity().startActivity(logOut)
                                                    requireActivity().finishAffinity()
                                                    (activity as AppCompatActivity?)!!.supportActionBar!!.show()
                                                    dialog.dismiss()
                                                }
                                                val alertFInal = builderfinal.create()
                                                alertFInal.show()

                                            }
                                        }
                                    }
                                }



                            }

                        }
                    }
                }
                builderConf.setNegativeButton("CANCELAR"){dialog, _ ->
                    dialog.dismiss()
                }
                val alertConf = builderConf.create()
                alertConf.show()
            }else{
                Toast.makeText(context, "Debe añadir 1 producto como mínimo", Toast.LENGTH_SHORT).show()
            }



        }

       // locButton.setOnClickListener {
//
       //     val dialogMap = LayoutInflater.from(context).inflate(R.layout.fragment_maps, null)
       //     val builderMap: AlertDialog.Builder = AlertDialog.Builder(context).setView(dialogMap)
       //     val alertMap = builderMap.show()
       //     //finalLoc = alertMap.findViewById(R.id.dirConf)
       //     val mapView = alertMap.findViewById<MapView>(R.id.confirMap)
       //     MapsInitializer.initialize(context)
       //     mapView.onCreate(alertMap.onSaveInstanceState())
       //     mapView.onResume()
       //     fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
       //     geocoder = Geocoder(requireContext(), Locale.getDefault())
//
       //     mapView.getMapAsync { map ->
       //         map?.let {
       //             googlemap = it
       //         }
//
//
//
       //         map?.setOnMarkerClickListener(this@SlideshowFragment)
       //         map?.uiSettings?.isZoomControlsEnabled = true
//
       //         if (ActivityCompat.checkSelfPermission(
       //                 requireContext(),
       //                 android.Manifest.permission.ACCESS_FINE_LOCATION
       //             ) != PackageManager.PERMISSION_GRANTED
       //         ) {
       //             requestPermissions(
       //                 arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
       //                 REQUEST_LOCATION
       //             )
       //         } else {
//
       //             map?.isMyLocationEnabled = true
       //             fusedLocationClient.lastLocation.addOnSuccessListener { ubicacion ->
//
       //                 if (ubicacion != null) {
       //                     lastLocation = ubicacion
//
       //                     latUser = ubicacion.latitude
       //                     lonUser = ubicacion.longitude
//
       //                     val actualLatLng =
       //                         com.google.android.gms.maps.model.LatLng(latUser!!, lonUser!!)
//
       //                     map?.animateCamera(CameraUpdateFactory.newLatLngZoom(actualLatLng, 15f))
//
       //                     dirUser = geocoder.getFromLocation(latUser!!, lonUser!!, 1)
       //                     direction = dirUser[0].getAddressLine(0)
//
       //                     map?.setOnCameraIdleListener {
       //                         val ubifinal = map.cameraPosition.target
       //                         dirUser = geocoder.getFromLocation(
       //                             ubifinal.latitude,
       //                             ubifinal.longitude, 1)
//
       //                         val dir = dirUser[0].getAddressLine(0)
       //                         direction = dir
//
       //                         Toast.makeText(context, dir, Toast.LENGTH_LONG).show()
//
//
       //                     }
       //                 }
//
       //             }
       //         }
       //     }
//
//
//
//
//
//
       //     alertMap.confirmacionButton.setOnClickListener {
//
       //         finalLoc?.text = direction
       //         alertMap.dismiss()
//
       //     }
       //}

                    //confirmarPedButton.setOnClickListener{
//
                    //}





        return view
    }

    override fun onMarkerClick(p0: Marker?) = false

    @SuppressLint("SetTextI18n")
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if(key == "totalExtras"){
            val newextr = sharedPreferences!!.getString(key, "0")
            val newextrdob = newextr!!.toDouble()
            //val sumnewext = contextrtotl + newextrdob
            contextrtotl = newextrdob
            val precdob = precio!!.toDouble()
            val prectot = precdob * contGral
            val sumatot= prectot + contextrtotl

            totalPed?.text = "$sumatot Bs."

        }
    }
}
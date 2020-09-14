package com.wingsupfinal.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.wingsupfinal.R

class SlideshowFragment : Fragment() {

    private var idProd  : String? = null
    private var tipos   : String? = null
    private val datos   = mutableMapOf<String, Any>()


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

        val fb = FirebaseFirestore.getInstance().collection("Productos").document(tipos!!).collection("tipos").document(idProd!!)
        fb.get().addOnSuccessListener { documents->
            if(documents.exists()){
                val datos1 = documents.data
                for(dat in datos1!!){
                    val llave = dat.key
                    val valor = dat.value.toString()
                    datos[llave] = valor
                }
                imgprod.let{
                    Glide.with(requireContext()).load(datos["imgProd"]).into(imgprod)
                }
                nompr.text = datos["nombreProd"].toString()
                detprod.text = datos["descripcion"].toString()
                precprod.text = datos["precio"].toString()
            }
        }



        return view
    }
}
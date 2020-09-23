package com.wingsupfinal.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.wingsupfinal.R

class HomeFragment : Fragment() {

    private val fb = FirebaseFirestore.getInstance().collection("Productos")
    private val adapter = AdapterExtras()
    private val adaptador = AdapterExtras()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fb.document("Extras").collection("extras").get().addOnCompleteListener {
            val listData = mutableListOf<DatosExtras>()
            if(it.isSuccessful){
                for(extras in it.result!!){
                    val imgprod = extras.get("imgProd").toString()
                    val datos = DatosExtras(imgprod)
                    listData.add(datos)

                }
                adapter.setListData(listData)
                adapter.notifyDataSetChanged()
            }
        }

     val fbpub = FirebaseFirestore.getInstance().collection("Publicidad")
     fbpub.addSnapshotListener{value, error ->
         val listData = mutableListOf<DatosExtras>()
         if(error!=null){
             return@addSnapshotListener
         }
         for(documents in value!!){
             val imgpub = documents.get("imgPub").toString()
             val dat = DatosExtras(imgpub)
             listData.add(dat)
         }
         adaptador.setListData(listData)
         adaptador.notifyDataSetChanged()
     }

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val burguers = view.findViewById<ImageView>(R.id.burguerImg)
        val wings = view.findViewById<ImageView>(R.id.wingsImg)
        val finjers = view.findViewById<ImageView>(R.id.finjersImg)
        val ribs = view.findViewById<ImageView>(R.id.ribsImg)
        val recExtr = view.findViewById<RecyclerView>(R.id.recyclerExtras)
        val recPub = view.findViewById<RecyclerView>(R.id.recyclerPubli)

        burguers.setOnClickListener{

            val action = HomeFragmentDirections.actionProductos("Hamburguesas")
            findNavController().navigate(action)

        }
        wings.setOnClickListener{
            val action = HomeFragmentDirections.actionProductos("Alitas")
            findNavController().navigate(action)

        }
        finjers.setOnClickListener{
            val action = HomeFragmentDirections.actionProductos("Fingers")
            findNavController().navigate(action)

        }
        ribs.setOnClickListener{

            val action = HomeFragmentDirections.actionProductos("Ribs")
            findNavController().navigate(action)
        }

        recExtr.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recExtr.adapter = adapter

        recPub.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recPub.adapter = adaptador

        fb.document("Hamburguesas").get().addOnSuccessListener{document->
            if(document.exists()){
                val imgBur = document.get("imgProd").toString()
                burguers.let {
                    Glide.with(requireContext()).load(imgBur).into(burguers)
                }
            }
        }
        fb.document("Alitas").get().addOnSuccessListener{documents->
            if(documents.exists()){
                val imgwings = documents.get("imgProd").toString()
                wings.let {
                    Glide.with(requireContext()).load(imgwings).into(wings)
                }
            }
        }
        fb.document("Fingers").get().addOnSuccessListener{documentss->
            if(documentss.exists()){
                val imgFing = documentss.get("imgProd").toString()
                finjers.let {
                    Glide.with(requireContext()).load(imgFing).into(finjers)
                }
            }
        }
        fb.document("Ribs").get().addOnSuccessListener{documentos->
            if(documentos.exists()){
                val imgRibs = documentos.get("imgProd").toString()
                ribs.let {
                    Glide.with(requireContext()).load(imgRibs).into(ribs)
                }
            }
        }


        return view
    }
}
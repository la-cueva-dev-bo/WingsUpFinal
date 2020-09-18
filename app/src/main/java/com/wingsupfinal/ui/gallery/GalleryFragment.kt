package com.wingsupfinal.ui.gallery


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.wingsupfinal.R

class GalleryFragment : Fragment() {

    private var producto: String? = null
    private val adapter = AdapterProd()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        producto = GalleryFragmentArgs.fromBundle(requireArguments()).producto

        val fb = FirebaseFirestore.getInstance().collection("Productos").document(producto!!).collection("tipos")
        fb.get().addOnCompleteListener {
            if(it.isSuccessful){
                val listData = mutableListOf<DatosProductos>()
                for(tipos in it.result!!){
                    val datosprod = tipos.data
                    val idprod = tipos.id
                    val tipoProd = producto.toString()
                    val datos = DatosProductos(datosprod, idprod, tipoProd)
                    listData.add(datos)
                }
                adapter.setListData(listData)
                adapter.notifyDataSetChanged()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        val rec1 = view.findViewById<RecyclerView>(R.id.recyclerProd)

        rec1.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rec1.adapter = adapter


        return view
    }


}

//
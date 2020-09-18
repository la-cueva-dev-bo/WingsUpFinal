package com.wingsupfinal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class CarritoFragment : Fragment() {
   // val adapter = AdapterCarrito()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_carrito, container, false)

        val recyCarrito = view.findViewById<RecyclerView>(R.id.recyCarrito)
        recyCarrito.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
     //   recyCarrito.adapter = AdapterCarrito()
        return view
    }

}
package com.wingsupfinal.ui.gallery

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wingsupfinal.R
import com.wingsupfinal.ui.home.DatosExtras

class ProdViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_prod, parent, false)){

    private var prodimg : ImageView? = null
    private var nomprod : TextView? = null
    private var detprod : TextView? = null
    private var preProd : TextView? = null
    private var card : CardView? = null


    init{
        prodimg = itemView.findViewById(R.id.imgProd)
        //nomprod = itemView.findViewById(R.id.nomProd)
        detprod = itemView.findViewById(R.id.detProd)
        preProd = itemView.findViewById(R.id.preProd)
        card    = itemView.findViewById(R.id.cardProd)
    }

    @SuppressLint("SetTextI18n")
    fun bind(datos: DatosProductos){


        prodimg?.let{
            Glide.with(itemView.context).load(datos.datosProd.get("imgProd")).into(prodimg!!)
        }

        nomprod?.text = datos.datosProd.get("nombreProd").toString()
        detprod?.text = datos.datosProd.get("descripcion").toString()
        preProd?.text = "${datos.datosProd.get("precio").toString()} Bs."

        card?.setOnClickListener {
            val action = GalleryFragmentDirections.actionConfirmacion(datos.tipos.toString()
                , datos.idProd.toString(), datos.datosProd.get("imgProd").toString(), datos.datosProd.get("precio").toString()
                , datos.datosProd.get("descripcion").toString())
            it.findNavController().navigate(action)
        }

    }



}

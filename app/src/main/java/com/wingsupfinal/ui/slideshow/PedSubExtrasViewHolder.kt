package com.wingsupfinal.ui.slideshow

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wingsupfinal.AppWingsUp
import com.wingsupfinal.R


class PedSubExtrasViewHolder(inflater: LayoutInflater, parent: ViewGroup):
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_choose_extra, parent, false)) {

    private var nomExtra        : TextView? = null
    private var precioExtra     : TextView? = null
    private var add             : ImageButton? = null
    private var cantidad        : TextView? = null
    private var remove          : ImageButton? = null
    private var cantcounter     : Int = 0


    init {
        nomExtra        = itemView.findViewById(R.id.nomExtra)
        precioExtra     = itemView.findViewById(R.id.precioExtra)
        add             = itemView.findViewById(R.id.plusIV)
        cantidad        = itemView.findViewById(R.id.counterTV)
        remove          = itemView.findViewById(R.id.removeIV)


    }

    @SuppressLint("SetTextI18n")
    fun bind(data: DatosSubExtras) {

        nomExtra?.text = data.key
        precioExtra?.text = "${data.precio} Bs."


        add?.setOnClickListener {

            val nuevacantidad = cantcounter + 1
            cantcounter = nuevacantidad
            cantidad?.text = cantcounter.toString()
            val cantidadtotal = cantcounter * (data.precio.toDouble())
            val string = AppWingsUp.context.resources.getString(R.string.prefs_file)
            val prefertotales = itemView.context.getSharedPreferences(string, Context.MODE_PRIVATE)
            val totalactual = prefertotales.getString("totalExtras", "0")
            val totalextras = totalactual!!.toDouble() + cantidadtotal
            val newpreftotales =
                itemView.context.getSharedPreferences(string, Context.MODE_PRIVATE).edit()
            newpreftotales.putString("totalExtras", totalextras.toString())
            newpreftotales.putString(data.key, "$cantcounter")
            newpreftotales.apply()

        }

        remove?.setOnClickListener {
            if (cantcounter != 0) {
                val newcont = cantcounter - 1
                cantcounter = newcont
                cantidad?.text = cantcounter.toString()
                val cantidadtotal = cantcounter * (data.precio.toDouble())
                val string = AppWingsUp.context.resources.getString(R.string.prefs_file)
                val prefertotales = itemView.context.getSharedPreferences(string, Context.MODE_PRIVATE)
                val totalactual = prefertotales.getString("totalExtras", "0")
                val totalextras = totalactual!!.toDouble() + cantidadtotal
                val newpreftotales = itemView.context.getSharedPreferences(string, Context.MODE_PRIVATE).edit()
                newpreftotales.putString("totalExtras", totalextras.toString())
                newpreftotales.putString(data.key, "$cantcounter")
                newpreftotales.apply()


            }


        }


    }

}


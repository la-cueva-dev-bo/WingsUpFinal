package com.wingsupfinal.ui.slideshow

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wingsupfinal.R


class PedExtrasViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_detalle_extras, parent, false)) {

    private var titulo: TextView? = null
    private var recycler: RecyclerView? = null

    init {
        titulo = itemView.findViewById(R.id.txtViewNomProdExtras)
        recycler = itemView.findViewById(R.id.recyPedExtras)
    }

    fun bind(datos: DatosPedExtras) {

        titulo?.text = datos.pedidosextras
        val subAdapter = AdapterSubExtras()
        val listaDatos = mutableListOf<DatosSubExtras>()

        for (subextras in datos.datos) {
            val llave = subextras.key
            val precio = subextras.value.toString()
            val datosExtras = DatosSubExtras(llave, precio)
            listaDatos.add(datosExtras)
        }
        subAdapter.setListData(listaDatos)
        subAdapter.notifyDataSetChanged()



        recycler?.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
        recycler?.adapter = subAdapter
    }

}

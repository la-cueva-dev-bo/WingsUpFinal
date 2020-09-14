package com.wingsupfinal.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AdapterExtras: RecyclerView.Adapter<ExtrasViewHolder>() {

    private var dataList = mutableListOf<DatosExtras>()

    fun setListData(data: MutableList<DatosExtras>){
        dataList = data
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtrasViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        return ExtrasViewHolder(inflate, parent)

    }

    override fun onBindViewHolder(holder: ExtrasViewHolder, position: Int) {
        val datos: DatosExtras = dataList[position]
        holder.bind(datos)
    }

    override fun getItemCount(): Int = dataList.size

}

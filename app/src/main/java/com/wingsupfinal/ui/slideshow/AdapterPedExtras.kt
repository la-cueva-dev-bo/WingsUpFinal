package com.wingsupfinal.ui.slideshow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AdapterPedExtras: RecyclerView.Adapter<PedExtrasViewHolder>() {

    private var dataList = mutableListOf<DatosPedExtras>()

    fun setListData(data: MutableList<DatosPedExtras>){
        dataList = data
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedExtrasViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        return PedExtrasViewHolder(inflate, parent)

    }

    override fun onBindViewHolder(holder: PedExtrasViewHolder, position: Int) {
        val datos: DatosPedExtras = dataList[position]
        holder.bind(datos)
    }

    override fun getItemCount(): Int = dataList.size

}
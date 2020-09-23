package com.wingsupfinal.ui.slideshow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AdapterSubExtras: RecyclerView.Adapter<PedSubExtrasViewHolder>() {

    private var dataList = mutableListOf<DatosSubExtras>()

    fun setListData(data: MutableList<DatosSubExtras>){
        dataList = data
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedSubExtrasViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        return PedSubExtrasViewHolder(inflate, parent)

    }

    override fun onBindViewHolder(holder: PedSubExtrasViewHolder, position: Int) {
        val data: DatosSubExtras = dataList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = dataList.size

}
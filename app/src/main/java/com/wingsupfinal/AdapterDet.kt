package com.wingsupfinal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AdapterDet: RecyclerView.Adapter<DetalleViewHolder>() {

    private var dataList = mutableListOf<DatosDetalle>()

    fun setListData(data: MutableList<DatosDetalle>){
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetalleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DetalleViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int=dataList.size


    override fun onBindViewHolder(holder: DetalleViewHolder, position: Int) {
        val dat: DatosDetalle = dataList[position]
        holder.bind(dat)
    }

}

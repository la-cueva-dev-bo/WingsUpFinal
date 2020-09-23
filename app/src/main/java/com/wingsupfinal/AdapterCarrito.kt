package com.wingsupfinal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AdapterCarrito: RecyclerView.Adapter<CarritoViewHolder>() {

    private var dataList = mutableListOf<DatosCarrito>()

    fun setListData(data: MutableList<DatosCarrito>){
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CarritoViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int=dataList.size


    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val dat: DatosCarrito = dataList[position]
        holder.bind(dat)
    }

}

package com.wingsupfinal.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wingsupfinal.ui.home.DatosExtras
import com.wingsupfinal.ui.home.ExtrasViewHolder

class AdapterProd: RecyclerView.Adapter<ProdViewHolder>() {

    private var dataList = mutableListOf<DatosProductos>()

    fun setListData(data: MutableList<DatosProductos>){
        dataList = data
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        return ProdViewHolder(inflate, parent)

    }

    override fun onBindViewHolder(holder: ProdViewHolder, position: Int) {
        val datos: DatosProductos = dataList[position]
        holder.bind(datos)
    }

    override fun getItemCount(): Int = dataList.size

}

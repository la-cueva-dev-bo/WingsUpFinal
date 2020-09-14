package com.wingsupfinal.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wingsupfinal.R


class ExtrasViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_extra, parent, false)){

    private var extraimg : ImageView? = null

    init{
        extraimg = itemView.findViewById(R.id.extraImg)
    }

    fun bind(datos: DatosExtras){

        extraimg?.let{
            Glide.with(itemView.context).load(datos.imgprod).into(extraimg!!)
        }
    }



}

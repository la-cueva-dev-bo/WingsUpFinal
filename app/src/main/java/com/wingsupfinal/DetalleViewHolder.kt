package com.wingsupfinal

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_detalle_cart.*

class DetalleViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_extra_detalle_cart, parent, false)){


    private var llave   : TextView? = null
    private var valor : TextView? = null

    init{

        llave = itemView.findViewById(R.id.llaveTextDet)
        valor = itemView.findViewById(R.id.valorTextDet)

    }

    @SuppressLint("SetTextI18n")
    fun bind(dat: DatosDetalle){

        llave?.text = dat.llave
        valor?.text = dat.valor


    }
}




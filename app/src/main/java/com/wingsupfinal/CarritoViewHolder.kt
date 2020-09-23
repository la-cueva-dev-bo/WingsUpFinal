package com.wingsupfinal

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_detalle_cart.*


class CarritoViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_carrito, parent, false)){

    private var imgprod : ImageView? = null
    private var cant    : TextView? = null
    private var total   : TextView? = null
    private var detalle : TextView? = null
    private var imgButt : ImageButton? = null




    init{

        imgprod = itemView.findViewById(R.id.imgProdCart)
        cant = itemView.findViewById(R.id.cantCartText)
        total = itemView.findViewById(R.id.totalCartText)
        detalle = itemView.findViewById(R.id.detalleCartText)
        imgButt = itemView.findViewById(R.id.reomveItemImageButton)


    }

    @SuppressLint("SetTextI18n")
    fun bind(dat: DatosCarrito){


        imgprod?.let {
            Glide.with(itemView.context).load(dat.datosCarrito["imgProd"].toString()).into(imgprod!!)
        }

        cant?.text = dat.datosCarrito["cantidad"].toString()
        total?.text = "${dat.datosCarrito["totalPedido"].toString()} Bs."

        imgButt?.setOnClickListener {
            val builder = AlertDialog.Builder(itemView.context)
            builder.setTitle("¡ATENCIÓN!")
            builder.setMessage("Está seguro de eliminar este producto?")
            builder.setPositiveButton("ACEPTAR"){dialog, _->
                val string = AppWingsUp.context.resources.getString(R.string.prefs_file)
                val prefs = itemView.context.getSharedPreferences(string, Context.MODE_PRIVATE)
                val userId = prefs.getString("userId", "")
                FirebaseFirestore.getInstance().collection("Carritos")
                    .document(userId!!).collection("carrito").document(dat.idCarrito.toString()).delete()

                dialog.dismiss()
            }
            builder.setNegativeButton("CANCELAR"){dialog, _->
                dialog.dismiss()
            }
            val alertRemove = builder.create()
            alertRemove.show()

        }

        detalle?.setOnClickListener {
            val itemDetalleDialog = LayoutInflater.from(itemView.context).inflate(R.layout.item_detalle_cart, null)
            val builderDet = AlertDialog.Builder(itemView.context).setView(itemDetalleDialog)
            val alertDetalle = builderDet.show()
            val adapterDet = AdapterDet()
            alertDetalle.closeDetCart.setOnClickListener {
                alertDetalle.dismiss()
            }
            val listDataEx = mutableListOf<DatosDetalle>()
            for(extras in dat.datosCarrito){
                val datosDetalleExtras = extras.key
                if(datosDetalleExtras != "idProducto" && datosDetalleExtras!="imgProd" && datosDetalleExtras!= "totalExtras"
                    && datosDetalleExtras!= "totalPedido" && datosDetalleExtras!= "cantidad"){
                    val cantExt = extras.value.toString()
                    val datosExtDet = DatosDetalle(datosDetalleExtras, cantExt)
                    listDataEx.add(datosExtDet)

                }

            }
            adapterDet.setListData(listDataEx)
            adapterDet.notifyDataSetChanged()

            alertDetalle.recyclerDetalleCart.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL,false)
            alertDetalle.recyclerDetalleCart.adapter = adapterDet

            alertDetalle.totExtCart.text = "${dat.datosCarrito["totalExtras"].toString()} Bs."
            alertDetalle.totGralCart.text = "${dat.datosCarrito["totalPedido"].toString()} Bs."
            alertDetalle.cantExtrDetTextView.text = dat.datosCarrito["cantidad"].toString()



        }







    }
}




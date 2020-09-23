package com.wingsupfinal.ui.slideshow

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.wingsupfinal.AppWingsUp
import com.wingsupfinal.R


class PedExtrasViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_choose_extra, parent, false)) {

    private var nombre: TextView? = null
    private var precio: TextView? = null
    private var mas      : ImageView? = null
    private var men      : ImageView? = null
    private var cant     : TextView? = null
    private var contador : Int = 0
    private var orden : String? = null
    private var cantidad : String? = null

    init {

        nombre = itemView.findViewById(R.id.nomExtra)
        precio = itemView.findViewById(R.id.precioExtra)
        mas = itemView.findViewById(R.id.plusIV)
        men = itemView.findViewById(R.id.removeIV)
        cant = itemView.findViewById(R.id.counterTV)

    }

    @SuppressLint("SetTextI18n")
    fun bind(datos: DatosPedExtras) {



        orden = datos.subcat.toString()
        if(orden!="Toppings"){
            val fb = FirebaseFirestore.getInstance().collection("Productos").document("Subextras")
                .collection(orden!!).document(datos.idExtra)
            fb.get().addOnSuccessListener { doc->
                cantidad = doc.get("cantidad").toString()
            }
        }else{
            val fb = FirebaseFirestore.getInstance().collection("Productos").document("Toppings")
                .collection("toppings").document(datos.idExtra)
            fb.get().addOnSuccessListener { doc->
                cantidad = doc.get("cantidad").toString()
            }
        }

        nombre?.text = datos.datos["nombre"].toString()
        precio?.text = "${datos.datos["precio"].toString()} Bs."

        mas?.setOnClickListener {

            val newcont = contador + 1
            contador = newcont
            val cantidadNum = cantidad!!.toInt()

            if(contador<=cantidadNum){
                cant?.text = "$contador"
                val string = AppWingsUp.context.resources.getString(R.string.prefs_file)
                val totpref = itemView.context.getSharedPreferences(string, Context.MODE_PRIVATE)
                val cantActTot = totpref.getString(datos.datos["nombre"].toString(), "0")
                val totActTot = totpref.getString("totalExtras", "0")
                val precioNum = datos.datos["precio"].toString().toDouble()
                val totActNum = totActTot!!.toDouble()
                val newTotItem = precioNum * contador
                val newtotextras = totActNum + newTotItem

                val nuevaCantExtra = cantidadNum - contador
                val newtotpref = itemView.context.getSharedPreferences(string, Context.MODE_PRIVATE).edit()
                newtotpref.putString(datos.datos["nombre"].toString(), contador.toString())
                newtotpref.putString("totalExtras", newtotextras.toString())
                newtotpref.apply()
                //Toast.makeText(itemView.context, "$newtotextras", Toast.LENGTH_SHORT).show()
            }else{
                val setCont = contador - 1
                contador = setCont
                Toast.makeText(itemView.context, "Lo sentimos, solo quedan $cantidad disponibles", Toast.LENGTH_SHORT).show()
            }

        }
        men?.setOnClickListener{
            if(contador!=0){
                val newcont = contador - 1
                contador = newcont
                cant?.text = "$contador"
                val cantidadNum = cantidad!!.toInt()
                val string = AppWingsUp.context.resources.getString(R.string.prefs_file)
                val totpref = itemView.context.getSharedPreferences(string, Context.MODE_PRIVATE)
                val cantActTot = totpref.getString(datos.datos["nombre"].toString(), "0")
                val newcantact = cantActTot!!.toDouble()
                val totActTot = totpref.getString("totalExtras", "0")
                val precioNum = datos.datos["precio"].toString().toDouble()
                val totActNum = totActTot!!.toDouble()
                val newTotItem = precioNum * 1
                val newtotextras = totActNum - newTotItem
                val nuevaCantExtra = newcantact - 1
                val newtotpref = itemView.context.getSharedPreferences(string, Context.MODE_PRIVATE).edit()
                newtotpref.putString(datos.datos["nombre"].toString(), nuevaCantExtra.toString())
                newtotpref.putString("totalExtras", newtotextras.toString())
                newtotpref.apply()
                //Toast.makeText(itemView.context, "$nuevaCantExtra", Toast.LENGTH_SHORT).show()
            }
        }

    }

}

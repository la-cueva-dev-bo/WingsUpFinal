package com.wingsupfinal

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.bumptech.glide.Glide


class PerfilFragment : Fragment() {

   // private var perfilUserName      : String? = null
   // private var emailUser           : String? = null
   // private var phone                : String? = null
   // private var imageUser           : String? = null
   // private var userId              : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     //  val preferences = requireActivity().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
     //  perfilUserName  = preferences.getString("userName", "")
     //  emailUser       = preferences.getString("emailUser", "")
     //  phone            = preferences.getString("phoneUser", "")
     //  imageUser       = preferences.getString("imgUser", "")
     //  userId          = preferences.getString("userId", "")

     //  retainInstance
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?



    ): View? {
        // Inflate the layout for this fragment


        val view = inflater.inflate(R.layout.fragment_perfil, container, false)
     //   val imagen      = view.findViewById<ImageButton>(R.id.imgViewPerfUser)
     //   val nombre      = view.findViewById<TextView>(R.id.txtUserName)
     //   val correo      = view.findViewById<TextView>(R.id.txtEmailUser)
     //   val telefono    = view.findViewById<TextView>(R.id.txtPhoneUser)
     //   val misPedidos  = view.findViewById<Button>(R.id.misPedButton)
     //   val close       = view.findViewById<Button>(R.id.cerrarSesionButton)
//
     //   imagen?.let {
     //       Glide.with(requireContext()).load("imgUser").into(imagen)
//
     //       nombre?.text = perfilUserName
     //       correo?.text = emailUser
     //       telefono?.text = phone
     //   }
//
     //   misPedidos.setOnClickListener{
//
     //   }
//
     //   close.setOnClickListener{


       // }

        return view
    }


}
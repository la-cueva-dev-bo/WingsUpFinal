package com.wingsupfinal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class PerfilFragment : Fragment() {

    private var perfilUserName      : String? = null
    private var emailUser           : String? = null
    private var fono                : String? = null
    private var imageUser           : String? = null
    private var userId              : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?



    ): View? {
        // Inflate the layout for this fragment


        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        return view
    }


}
package com.wingsupfinal

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_conf_ped.*
import java.util.concurrent.TimeUnit


class CarritoFragment : Fragment() {
   //

    private var user: String? = null
    private var userId: String? = null

    private val adapter = AdapterCarrito()

    private var cont : Int = 0

    private var confirmar: Button? = null
    private var crede: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        user = pref.getString("user", "")
        userId = pref.getString("userId", "")

        val fb = FirebaseFirestore.getInstance().collection("Carritos").document(userId!!).collection("carrito")
        fb.addSnapshotListener { value, error->
            if(error!=null){
                return@addSnapshotListener
            }
            val listDataCarrito = mutableListOf<DatosCarrito>()
                for(docs in value!!){
                    val idCarrito = docs.id
                    val datosCarritos = docs.data
                    val datosCartFinal = DatosCarrito(idCarrito, datosCarritos)
                    listDataCarrito.add(datosCartFinal)
                }
            adapter.setListData(listDataCarrito)
            adapter.notifyDataSetChanged()
            cont = adapter.itemCount
            if(cont==0){
                Toast.makeText(context, "No tiene productos en su carrito", Toast.LENGTH_SHORT).show()
                confirmar?.isVisible = false

            }else{
                confirmar?.isVisible = true
            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_carrito, container, false)

        val recyCarrito = view.findViewById<RecyclerView>(R.id.recyclerCarrito)
        confirmar = view.findViewById(R.id.confPedButton)
        recyCarrito.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyCarrito.adapter = adapter



        confirmar?.setOnClickListener{

            val fsVerificacion = FirebaseFirestore.getInstance().collection("Usuarios").document(userId!!)
            fsVerificacion.get().addOnSuccessListener { documents->
                if(documents.exists()){
                    val telfVer = documents.get("telefono").toString()
                    if(telfVer!="null"){
                        Toast.makeText(context, "verificado", Toast.LENGTH_LONG).show()
                    }else{
                        val builderVerPhone = AlertDialog.Builder(context)
                        builderVerPhone.setTitle("¡ATENCIÓN")
                        builderVerPhone.setMessage("Lo sentimos, usted no tiene registrado un numero de teléfono, debe ingresar uno para realizar un pedido. ¿Desea hacerlo ahora?")
                        builderVerPhone.setPositiveButton("ACEPTAR"){dialog, _ ->
                            val itemConfPed = LayoutInflater.from(activity).inflate(R.layout.item_conf_ped, null)
                            val alertVerPhone = AlertDialog.Builder(context).setView(itemConfPed)
                            val alerVerifi = alertVerPhone.show()
                            alerVerifi.closeVerifImageButton.setOnClickListener {
                                alerVerifi.dismiss()
                            }
                            alerVerifi.verPhoneButton.setOnClickListener {

                                val phoneNum = "+591${alerVerifi.telefonoEditText.text}"
                                val smsCode = "210290"
                                //val settings = FirebaseAuth.getInstance().firebaseAuthSettings
                                //settings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNum, smsCode)

                                val phoneAuth = PhoneAuthProvider.getInstance()
                                phoneAuth.verifyPhoneNumber(phoneNum, 60L, TimeUnit.SECONDS, requireActivity(),
                                object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                                    override fun onCodeSent(
                                        verificacionId: String,
                                        forceResendingToken: PhoneAuthProvider.ForceResendingToken

                                    ) {

                                        crede = verificacionId

                                    }
                                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                        Toast.makeText(context, "si: $crede", Toast.LENGTH_LONG).show()
                                    }

                                    override fun onVerificationFailed(e: FirebaseException) {
                                        Toast.makeText(context, "no: $e", Toast.LENGTH_LONG).show()
                                        if(e is FirebaseAuthInvalidCredentialsException){
                                            Toast.makeText(context, "no: $e", Toast.LENGTH_LONG).show()
                                        }else if (e is FirebaseTooManyRequestsException){
                                            Toast.makeText(context, "no: $e", Toast.LENGTH_LONG).show()
                                        }
                                    }

                                })
                                //Toast.makeText(context, phoneNum, Toast.LENGTH_LONG).show()
                            }

                            dialog.dismiss()
                        }
                        builderVerPhone.setNegativeButton("EN OTRO MOMENTO"){dialog, _->
                            dialog.dismiss()
                        }
                        val alertVeri = builderVerPhone.create()
                        alertVeri.show()

                    }
                }
            }

            //val itemConfPed = LayoutInflater.from(activity).inflate(R.layout.item_conf_ped, null)

        }


        return view
    }

    private fun enableUserManuallyInputCode() {

    }


}
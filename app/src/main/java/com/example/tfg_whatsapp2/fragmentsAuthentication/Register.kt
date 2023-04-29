package com.example.tfg_whatsapp2.fragmentsAuthentication

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.example.tfg_whatsapp2.R
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class Register : Fragment() {

    private lateinit var enterEmail : TextInputEditText
    private lateinit var enterPassword : TextInputEditText
    private lateinit var enterConfirmPassword : TextInputEditText
    private lateinit var registerButton : Button
    private lateinit var progressBar: ProgressBar

    private lateinit var fbAuth: FirebaseAuth
    private lateinit var fbStore: FirebaseFirestore
    private lateinit var db: DocumentReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        enterEmail = view.findViewById(R.id.etRegisterEmail)
        enterPassword = view.findViewById(R.id.etRegisterPassword)
        enterConfirmPassword = view.findViewById(R.id.etRegisterConfirmPassword)
        registerButton = view.findViewById(R.id.btnRegister)
        progressBar = view.findViewById(R.id.registerProgressBar)

        fbAuth = FirebaseAuth.getInstance()
        fbStore = FirebaseFirestore.getInstance()

        registerButton.setOnClickListener {
            val email = enterEmail.text.toString().trim()
            val password = enterPassword.text.toString().trim()
            val confirmPassword = enterConfirmPassword.text.toString().trim()
            if (TextUtils.isEmpty(email)){
                enterEmail.error = "El email es necesario"
            }else if(TextUtils.isEmpty(password)){
                enterPassword.error = "La contraseña es necesaria"
            }else if(password.length < 6){
                enterPassword.error = "La contraseña debe tener mas de 5 caracteres"
            }else if(password != confirmPassword){
                enterConfirmPassword.error = "Las contraseñas no son iguales"
            }else{
                progressBar.visibility = View.VISIBLE
                createAccount(email,password)
            }
        }
        return view
    }

    private fun createAccount(em: String, pass: String) {
        fbAuth.createUserWithEmailAndPassword(em,pass).addOnCompleteListener { task: Task<AuthResult> ->
            if (task.isSuccessful){
                val userinfo = fbAuth.currentUser?.uid
                db = fbStore.collection("users").document(userinfo.toString())
                val obj = mutableMapOf<String,String>()
                obj["userEmail"] = em
                obj["userPassword"] = pass
                obj["userName"] = ""
                obj["userStatus"] = ""
                db.set(obj).addOnSuccessListener {
                    Log.d("Exito", "Usuario Creado Correctamente")
                    progressBar.visibility = View.GONE
                }
            }else{
                Toast.makeText(context,"ERROR",Toast.LENGTH_LONG).show()
            }

        }
    }
}
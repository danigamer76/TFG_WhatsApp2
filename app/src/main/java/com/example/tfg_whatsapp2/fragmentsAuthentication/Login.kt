package com.example.tfg_whatsapp2.fragmentsAuthentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tfg_whatsapp2.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class Login : Fragment() {

    private lateinit var enterEmail: TextInputEditText
    private lateinit var enterPassword: TextInputEditText
    private lateinit var loginButton: Button
    private lateinit var googleButton: ImageButton
    private lateinit var progress: RelativeLayout
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var resultLaunch: ActivityResultLauncher<Intent>
    private val RC_SIGN_IN = 1011

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_login, container, false)
        enterEmail = view.findViewById(R.id.etLoginEmail)
        enterPassword = view.findViewById(R.id.etLoginPassword)
        loginButton = view.findViewById(R.id.btnLogin)
        googleButton = view.findViewById(R.id.btnGoogleLogin)
        progress = view.findViewById(R.id.login_progress_bar)

        loginButton.setOnClickListener {
            val email = enterEmail.text.toString().trim()
            val password = enterPassword.text.toString().trim()
            if (TextUtils.isEmpty(email)){
                enterEmail.error = "El email es necesario"
            }else if(TextUtils.isEmpty(password)){
                enterPassword.error = "La contraseña es necesaria"
            }else{
                progress.visibility = View.VISIBLE
                login(email,password)
            }

        }

        googleButton.setOnClickListener {
            createRequest()
        }
        resultLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if(result.resultCode == Activity.RESULT_OK){
                val launchData = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(launchData)
                try {
                    val account = task.getResult(ApiException::class.java)
                    Log.d("Gmail ID","firebaseAuthWith Google : $account")
                    firebaseAuthWithGoogle(account?.idToken)
                }catch (e:ApiException){
                    Log.w("Error", "Google Sing IN Failed", e)
                }

            }


        }
        return view
    }

    private fun createRequest() {
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = context.let { GoogleSignIn.getClient(it!!, googleSignInOptions) }
        resultLaunch.launch(Intent(mGoogleSignInClient.signInIntent))
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener {
                val acct = GoogleSignIn.getLastSignedInAccount(requireContext())
                if (acct != null){
                    val personName = acct.displayName!!
                    val personEmail = acct.email!!
                    val personPhoto = acct.photoUrl
                    val objLogin = mutableMapOf<String, String>()
                    objLogin["userEmail"] = personEmail
                    objLogin["userProfile"] = personPhoto.toString()
                    objLogin["userName"] = personName
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    FirebaseFirestore.getInstance().collection("users").document(userId.toString())
                        .set(objLogin).addOnSuccessListener {
                            Log.d("onSucess", "Successfully Google Login")
                        }
                }

            }
    }

    private fun login(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener{task->
            if (task.isSuccessful){
                Toast.makeText(context,"LoginSuccessfully",Toast.LENGTH_SHORT).show()
                progress.visibility = View.GONE
            }else{
                Toast.makeText(context,"ERROR",Toast.LENGTH_SHORT).show()

            }
        }
    }
}
package com.example.tfg_whatsapp2.fragmentsMenu

import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tfg_whatsapp2.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.lang.reflect.Array.get

class Profile : Fragment() {

    private lateinit var profileNameShow:TextView
    private lateinit var profileEmailShow:TextView
    private lateinit var profileStatusShow:TextView
    private lateinit var profilePicture:CircleImageView
    private lateinit var profilePictureAdd:ImageView
    private lateinit var profileNameEdit:TextInputLayout
    private lateinit var profileEmailEdit:TextInputLayout
    private lateinit var profileStatusEdit:TextInputLayout
    private lateinit var editName: TextInputEditText
    private lateinit var editEmail: TextInputEditText
    private lateinit var editStatus: TextInputEditText
    private lateinit var profileUpdate:Button
    private lateinit var profileSave:Button
    private lateinit var profileProgressBar: ProgressBar

    private lateinit var auth: FirebaseAuth
    private lateinit var fbStore: FirebaseFirestore
    private lateinit var db: DocumentReference
    private lateinit var userId: String

    private lateinit var image: ByteArray
    private lateinit var storageReference: StorageReference
    val register = registerForActivityResult(ActivityResultContracts.TakePicturePreview()){
        uploadImage(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        auth = FirebaseAuth.getInstance()
        fbStore = FirebaseFirestore.getInstance()
        userId = auth.currentUser!!.uid

        storageReference = FirebaseStorage.getInstance().reference.child("$userId/profilePhoto")

        profilePicture = view.findViewById(R.id.imgProfileImage)
        profilePictureAdd = view.findViewById(R.id.imgAddProfileImage)
        profileNameShow = view.findViewById(R.id.txtProfileName)
        profileNameEdit = view.findViewById(R.id.profile_name)
        editName = view.findViewById(R.id.etprofileName)
        profileEmailShow = view.findViewById(R.id.txtProfileEmail)
        profileEmailEdit = view.findViewById(R.id.profile_email)
        editEmail = view.findViewById(R.id.etprofileEmail)
        profileStatusShow = view.findViewById(R.id.txtProfileStatus)
        profileStatusEdit = view.findViewById(R.id.profile_Status)
        editStatus = view.findViewById(R.id.etprofileStatus)
        profileUpdate = view.findViewById(R.id.btnUpgradePerfil)
        profileSave = view.findViewById(R.id.btnSavePerfil)
        profileProgressBar = view.findViewById(R.id.profileProgressBar)

        profileUpdate.visibility = View.VISIBLE
        db = fbStore.collection("users").document(userId)
        db.addSnapshotListener{value,error->
            if (error!=null){
                Log.d("Error", "No se pueden Obtener los datos")
            }else{
                profileNameShow.text = value?.getString("userName")
                profileEmailShow.text = value?.getString("userEmail")
                profileStatusShow.text = value?.getString("userStatus")
                Picasso.get().load(value?.getString("userProfilePhoto")).error(R.drawable.profile_user).into(profilePicture)
            }
        }

        profileUpdate.setOnClickListener {
            profileNameShow.visibility = View.GONE
            profileEmailShow.visibility = View.GONE
            profileStatusShow.visibility = View.GONE
            profileNameEdit.visibility = View.VISIBLE
            profileEmailEdit.visibility = View.VISIBLE
            profileStatusEdit.visibility = View.VISIBLE
            profileSave.visibility = View.VISIBLE
            profileUpdate.visibility = View.GONE
            editName.text = Editable.Factory.getInstance().newEditable(profileNameShow.text.toString())
            editEmail.text = Editable.Factory.getInstance().newEditable(profileEmailShow.text.toString())
            editStatus.text = Editable.Factory.getInstance().newEditable(profileStatusShow.text.toString())
        }
        profileSave.setOnClickListener {
            profileNameShow.visibility = View.VISIBLE
            profileEmailShow.visibility = View.VISIBLE
            profileStatusShow.visibility = View.VISIBLE
            profileUpdate.visibility = View.VISIBLE
            profileNameEdit.visibility = View.GONE
            profileEmailEdit.visibility = View.GONE
            profileStatusEdit.visibility = View.GONE
            profileSave.visibility = View.GONE
            val obj = mutableMapOf<String,String>()
            obj["userName"] = editName.text.toString()
            obj["userEmail"] = editEmail.text.toString()
            obj["userStatus"] = editStatus.text.toString()
            db.set(obj).addOnSuccessListener {
                Log.d("Success","Data Successfully Updated")
            }
        }
        profilePictureAdd.setOnClickListener {
            capturePhoto()
        }
        return view
    }

    private fun capturePhoto() {
        register.launch(null)
    }

    private fun uploadImage(it: Bitmap?) {
        val baos = ByteArrayOutputStream()
        it?.compress(Bitmap.CompressFormat.JPEG,50,baos)
        image = baos.toByteArray()
        storageReference.putBytes(image).addOnSuccessListener{
            storageReference.downloadUrl.addOnSuccessListener {
                val obj = mutableMapOf<String,String>()
                obj["userProfilePhoto"] = it.toString()
                db.update(obj as Map<String,Any>).addOnSuccessListener {
                    Log.d("onSucess","ProfilePictureUploaded")
                }
            }
        }
    }
}
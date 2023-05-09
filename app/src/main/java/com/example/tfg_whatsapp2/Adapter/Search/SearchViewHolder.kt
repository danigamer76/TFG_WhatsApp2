package com.example.tfg_whatsapp2.Adapter.Search

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_whatsapp2.R
import com.example.tfg_whatsapp2.databinding.RecycleviewContactsBinding
import com.example.tfg_whatsapp2.modelo.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class SearchViewHolder(v: View): RecyclerView.ViewHolder(v) {
    val binding = RecycleviewContactsBinding.bind(v)

    fun render(u: UserModel){
        binding.txtContName.text = u.profileName
        binding.txtContEmail.text = u.profileEmail
        binding.txtContStatus.text = u.profileStatus
        Picasso.get().load(u.profilePicture).error(R.drawable.profile_user).into(binding.imgProfileImage)
        binding.btnAddFriend.visibility = View.VISIBLE

        FirebaseFirestore.getInstance()
            .collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
            .collection("friends").whereEqualTo(FieldPath.documentId(),u.profileId)
            .addSnapshotListener{snapshot,exception->
                if (exception != null) {
                    Log.e("onError", "Some Error Occured")
                } else {
                    if (!snapshot?.isEmpty!!) {
                        binding.btnAddFriend.visibility = View.GONE
                    }else{
                        binding.btnAddFriend.visibility = View.VISIBLE
                    }
                }
            }
        binding.btnAddFriend.setOnClickListener {
            val c = Calendar.getInstance(Locale.getDefault())
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            val timeStamp = "$hour:$minute"
            val obj = mutableMapOf<String,String>().also {
                it["time"] = timeStamp
            }
            val uid1 = FirebaseAuth.getInstance().currentUser?.uid.toString()
            val uid2 = u.profileId
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid1)
                .collection("friends").document(uid2).set(obj)
                .addOnSuccessListener {
                    Log.d("onSuccess","Successfully Added With ${u.profileId}")
                }
            val obj1 = mutableMapOf<String,ArrayList<String>>().also {
                it["uids"] = arrayListOf(uid1,uid2)
            }
            FirebaseFirestore.getInstance().collection("chats").document().set(obj1)
                .addOnSuccessListener {
                    Log.d("onSuccess","Successfully Chat Created With ${u.profileId}")
            }
        }

    }

}

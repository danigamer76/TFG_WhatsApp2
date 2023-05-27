package com.example.tfg_whatsapp2.Adapter.Search

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_whatsapp2.R
import com.example.tfg_whatsapp2.modelo.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val name: TextView = view.findViewById(R.id.txtName)
    val email: TextView = view.findViewById(R.id.txtEmail)
    val status: TextView = view.findViewById(R.id.txtStatus)
    val image: ImageView = view.findViewById(R.id.imgProfileImage)
    val addFriend: Button = view.findViewById(R.id.btAddFriend)

    private lateinit var uid1: String
    private lateinit var fstore: FirebaseFirestore

    fun bind(user: User) {
        name.text = user.profileName
        email.text = user.profileEmail
        status.text = user.profileStatus
        Picasso.get().load(user.profilePicture).error(R.drawable.profile_user).into(image)

        fstore = FirebaseFirestore.getInstance()
        uid1 = FirebaseAuth.getInstance().currentUser!!.uid
        fstore.collection("users").document(uid1)
            .collection("friends").whereEqualTo(FieldPath.documentId(), user.profileUid)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.d("", "")
                } else {
                    if (!snapshot!!.isEmpty) {
                        addFriend.visibility = View.GONE
                    } else {
                        addFriend.visibility = View.VISIBLE
                    }
                    addFriend.setOnClickListener {
                        updateChatRoom(user)


                }
            }
    }

    }
    private fun updateChatRoom(user: User) {
        val chatRoomId = generateChatroomID()

        val c = Calendar.getInstance(Locale.getDefault())
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val timeStamp = "$hour:$minute"

        val obj1 = mutableMapOf<String, ArrayList<String>>().also {
            it["uids"] = arrayListOf(uid1, user.profileUid)
        }

        val obj = mutableMapOf<String, String>().also {
            it["time"] = timeStamp
            it["chatRoomId"] = chatRoomId
        }


        fstore.collection("users").document(uid1).collection("friends")
            .document(user.profileUid).set(obj)
            .addOnSuccessListener {
                Log.d("taaa", "")
            }

        fstore.collection("chats").document(chatRoomId)
            .set(obj1)
            .addOnSuccessListener {
                Log.d("onSuccess", "Successfully Chat Created With")
            }
    }

    private fun generateChatroomID(): String {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val timestamp = System.currentTimeMillis()
        val randomNum = (0..9999).random() // Genera un número aleatorio entre 0 y 9999

        return "$userId-$timestamp-$randomNum"
    }
}



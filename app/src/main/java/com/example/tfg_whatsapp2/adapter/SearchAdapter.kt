package com.example.tfg_whatsapp2.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class SearchAdapter(val context: Context, private val searchList: ArrayList<User>): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    private lateinit var uid1 : String
    private lateinit var fstore : FirebaseFirestore
    class SearchViewHolder(view : View):RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.txtName)
        val email: TextView = view.findViewById(R.id.txtEmail)
        val status: TextView = view.findViewById(R.id.txtStatus)
        val image: ImageView = view.findViewById(R.id.imgProfileImage)
        val addFriend : Button = view.findViewById(R.id.btAddFriend)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val contactView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_contacts, parent, false)
        return SearchViewHolder(contactView)
    }
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val list = searchList[position]
        fstore = FirebaseFirestore.getInstance()
        uid1 = FirebaseAuth.getInstance().currentUser!!.uid
        holder.name.text = list.profileName
        holder.email.text = list.profileEmail
        holder.status.text = list.profileStatus
        Picasso.get().load(list.profilePicture).error(R.drawable.profile_user).into(holder.image)
        fstore.collection("users").document(uid1)
            .collection("friends").whereEqualTo(FieldPath.documentId(),list.profileUid).addSnapshotListener{ snapshot, exception->
                if(exception!=null)
                {
                    Log.d("","")
                }
                else
                {
                    if(!snapshot!!.isEmpty)
                    {
                        holder.addFriend.visibility = View.GONE
                    }
                    else
                    {
                        holder.addFriend.visibility = View.VISIBLE
                    }
                    holder.addFriend.setOnClickListener {
                        /*val c = Calendar.getInstance(Locale.getDefault())
                        val hour = c.get(Calendar.HOUR_OF_DAY)
                        val minute = c.get(Calendar.MINUTE)
                        val timeStamp = "$hour:$minute"
                        val obj = mutableMapOf<String, String>().also {
                            it["time"] = timeStamp
                        }*/
                        val obj1 = mutableMapOf<String, ArrayList<String>>().also {
                            it["uids"] = arrayListOf(uid1, list.profileUid)
                        }
                        fstore.collection("users").document(uid1).collection("friends").document(list.profileUid).set(obj1)
                            .addOnSuccessListener {
                                Log.d("onSuccess","ChatRoom Created")
                            }
                        fstore.collection("chats").document()
                            .set(obj1)
                            .addOnSuccessListener {
                                Log.d("onSuccess", "Successfully Chat Created With")
                            }
                        val obj2 = mapOf(
                            "chatid" to "0"
                        )
                        fstore.collection("chats").document()
                            .collection("count").document("chatid")
                            .set(obj2).addOnSuccessListener {
                                Log.d("onSuccess", "Successfully Chat Created With")
                            }
                    }
                }
            }
    }
    override fun getItemCount(): Int {
        return searchList.size
    }
}

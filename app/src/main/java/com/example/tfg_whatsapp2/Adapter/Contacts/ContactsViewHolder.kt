package com.example.tfg_whatsapp2.Adapter.Contacts

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_whatsapp2.MenuActivity
import com.example.tfg_whatsapp2.R
import com.example.tfg_whatsapp2.modelo.User
import com.squareup.picasso.Picasso

class ContactsViewHolder(view: View,val context: Context) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.txtName)
    private val email: TextView = view.findViewById(R.id.txtEmail)
    private val status: TextView = view.findViewById(R.id.txtStatus)
    private val image: ImageView = view.findViewById(R.id.imgProfileImage)
    private val userContent: CardView = view.findViewById(R.id.userContent)

    fun bind(user: User) {
        name.text = user.profileName
        email.text = user.profileEmail
        status.text = user.profileStatus
        Picasso.get().load(user.profilePicture).error(R.drawable.profile_user).into(image)

        userContent.setOnClickListener {
            val intent = Intent(context, MenuActivity::class.java).apply {
                putExtra("OptionName", "contactMessaging")
                putExtra("chatroomID", user.chatRoomId)
                putExtra("friendUID", user.profileUid)
                putExtra("friendName", user.profileName)
            }
            context.startActivity(intent)
        }
    }
}
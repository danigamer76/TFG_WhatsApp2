package com.example.tfg_whatsapp2.Adapter.Chats

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_whatsapp2.MenuActivity
import com.example.tfg_whatsapp2.R
import com.example.tfg_whatsapp2.modelo.ChatModal
import com.squareup.picasso.Picasso

class ChatsViewHolder(view: View,val context: Context) : RecyclerView.ViewHolder(view) {
    val name: TextView = view.findViewById(R.id.txtReceiverName)
    val message: TextView = view.findViewById(R.id.txtMessage)
    val image: ImageView = view.findViewById(R.id.imgChatImage)
    val content: CardView = view.findViewById(R.id.chatContent)

    fun bind(chatModal: ChatModal) {
        name.text = chatModal.receiver
        message.text = chatModal.message
        Picasso.get().load(chatModal.receiverImage).into(image)

        content.setOnClickListener {
            val intent = Intent(context, MenuActivity::class.java).apply {
                putExtra("OptionName", "chatMessaging")
                putExtra("chatroom", chatModal.docID)
                putExtra("receiverName", chatModal.receiver)
            }
            context.startActivity(intent)
        }
    }
}








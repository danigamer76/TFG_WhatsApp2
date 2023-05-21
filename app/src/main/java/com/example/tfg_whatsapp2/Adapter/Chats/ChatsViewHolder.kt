package com.example.tfg_whatsapp2.Adapter.Chats

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_whatsapp2.MenuActivity
import com.example.tfg_whatsapp2.databinding.RecycleviewChatBinding
import com.example.tfg_whatsapp2.modelo.ChatModel
import com.squareup.picasso.Picasso

class ChatsViewHolder(v: View): RecyclerView.ViewHolder(v) {
    val binding = RecycleviewChatBinding.bind(v)

    fun render(u: ChatModel){
        binding.txtReceiverName.text = u.receiver
        binding.txtMessage.text = u.message
        Picasso.get().load(u.receiverImage).into(binding.imgChatImage)
        binding.chatContent.setOnClickListener {
            val intent = Intent(binding.chatContent.context,MenuActivity::class.java).also {
                it.putExtra("OptionName","chatMessaging")
                it.putExtra("chatroom",u.docID)
                it.putExtra("receiverName",u.receiver)
            }
        }
    }
}

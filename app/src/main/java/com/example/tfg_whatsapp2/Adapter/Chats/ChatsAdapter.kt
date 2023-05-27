package com.example.tfg_whatsapp2.Adapter.Chats

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_whatsapp2.R
import com.example.tfg_whatsapp2.modelo.ChatModal

class ChatsAdapter(val context:Context,private val chatList: ArrayList<ChatModal>) :
    RecyclerView.Adapter<ChatsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.chat_view, parent, false)
        return ChatsViewHolder(view,context)
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        val list = chatList[position]
        holder.bind(list)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }
}
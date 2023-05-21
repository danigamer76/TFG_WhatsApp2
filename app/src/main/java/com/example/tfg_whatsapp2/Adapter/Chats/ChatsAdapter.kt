package com.example.tfg_whatsapp2.Adapter.Chats

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_whatsapp2.R
import com.example.tfg_whatsapp2.modelo.ChatModel
import com.example.tfg_whatsapp2.modelo.UserModel

class ChatsAdapter(
    val context: Context,
    val chatList:ArrayList<ChatModel>):RecyclerView.Adapter<ChatsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycleview_chat,parent,false)
        return ChatsViewHolder(v)
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.render(chatList[position])
    }

    override fun getItemCount() = chatList.size
}
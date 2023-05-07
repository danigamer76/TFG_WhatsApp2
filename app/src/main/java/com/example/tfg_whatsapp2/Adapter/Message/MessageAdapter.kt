package com.example.tfg_whatsapp2.Adapter.Message

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_whatsapp2.R
import com.example.tfg_whatsapp2.modelo.MessageModel
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(
    val context : Context,
    val messageList : ArrayList<MessageModel>):RecyclerView.Adapter<MessageViewHolder>(){
    private val left = 0
    private val right = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return if (viewType == right){
            val messageView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_sender,parent,false)
            return MessageViewHolder(messageView)
        }else{
            val messageView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_reciever,parent,false)
            return MessageViewHolder(messageView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].sender == FirebaseAuth.getInstance().currentUser?.uid.toString()){
            left
        }else{
            right
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.render(messageList[position])

    }

    override fun getItemCount() = messageList.size
    }

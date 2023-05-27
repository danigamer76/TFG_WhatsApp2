package com.example.tfg_whatsapp2.Adapter.Message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_whatsapp2.R
import com.example.tfg_whatsapp2.modelo.MessageModal
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(private val messageList: ArrayList<MessageModal>) :
    RecyclerView.Adapter<MessageViewHolder>() {

    private val left = 0
    private val right = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutRes = if (viewType == right) {
            R.layout.recycler_view_sender
        } else {
            R.layout.recycler_view_reciever
        }
        val messageView = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return MessageViewHolder(messageView)
    }

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].sender == FirebaseAuth.getInstance().currentUser?.uid.toString()) {
            left
        } else {
            right
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messageList[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}

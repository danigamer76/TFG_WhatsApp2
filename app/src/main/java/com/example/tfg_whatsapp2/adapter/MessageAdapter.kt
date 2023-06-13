package com.example.tfg_whatsapp2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_whatsapp2.R
import com.example.tfg_whatsapp2.modelo.MessageModal
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context : Context, private val messageList : ArrayList<MessageModal>):
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>(){
    private val left = 0
    private val right = 1
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageViewHolder {
        return if(viewType==right) {
            val messageView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_reciever,parent,false)
            MessageViewHolder(messageView)
        } else {
            val messageView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_sender,parent,false)
            MessageViewHolder(messageView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(messageList[position].sender==FirebaseAuth.getInstance().currentUser?.uid.toString())
        {
            left
        }
        else
        {
            right
        }
    }
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val list = messageList[position]
        holder.message.text = list.message
        holder.time.text = list.timeStamp
        //holder.id.text = list.messageID

        // Aplicar animaciones según el tipo de mensaje
        val animation: Animation = if (holder.itemViewType == left) {
            AnimationUtils.loadAnimation(context, R.anim.left_anim)
        } else {
            AnimationUtils.loadAnimation(context, R.anim.right_anim)
        }
        holder.itemView.startAnimation(animation)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
    class MessageViewHolder(view : View):RecyclerView.ViewHolder(view)
    {
        val message : TextView = view.findViewById(R.id.txtMessage)
        val time    : TextView = view.findViewById(R.id.txtTime)
    }
}

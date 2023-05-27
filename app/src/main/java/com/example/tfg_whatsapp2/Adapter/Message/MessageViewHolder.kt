package com.example.tfg_whatsapp2.Adapter.Message

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_whatsapp2.R
import com.example.tfg_whatsapp2.modelo.MessageModal

class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val message: TextView = view.findViewById(R.id.txtMessage)
    private val time: TextView = view.findViewById(R.id.txtTime)

    fun bind(message: MessageModal) {
        this.message.text = message.message
        this.time.text = message.timeStamp
    }
}

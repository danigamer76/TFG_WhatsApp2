package com.example.tfg_whatsapp2

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_whatsapp2.databinding.RecyclerViewRecieverBinding
import com.example.tfg_whatsapp2.databinding.RecycleviewContactsBinding
import com.example.tfg_whatsapp2.modelo.MessageModel

class MessageViewHolder(v: View):RecyclerView.ViewHolder(v) {

    val binding = RecyclerViewRecieverBinding.bind(v)

    fun render(m: MessageModel){
        binding.txtMessage.text = m.message
        binding.txtTime.text = m.timeStamp
    }

}

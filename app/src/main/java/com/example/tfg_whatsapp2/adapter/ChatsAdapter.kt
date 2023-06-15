package com.example.tfg_whatsapp2.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_whatsapp2.MenuActivity
import com.example.tfg_whatsapp2.R
import com.example.tfg_whatsapp2.modelo.ChatModal
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class ChatsAdapter(val context: Context, private val chatList: ArrayList<ChatModal>) :
    RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder>() {

    private val fstore: FirebaseFirestore = FirebaseFirestore.getInstance()


    class ChatsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.txtReceiverName)
        val message: TextView = view.findViewById(R.id.txtMessage)
        val image: ImageView = view.findViewById(R.id.imgChatImage)
        val content: CardView = view.findViewById(R.id.chatContent)
        val animation: Animation = AnimationUtils.loadAnimation(view.context, R.anim.right_anim).apply {
            startOffset = 20 // Establece el retraso de inicio en milisegundos
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_view, parent, false)
        return ChatsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        val list = chatList[position]
        userName(list.receiver,holder)
        holder.message.text = list.message
        val image = list.receiverImage
        if (image != "null") {
            Picasso.get().load(image).into(holder.image)
        }
        holder.content.setOnClickListener {
            val intent = Intent(context, MenuActivity::class.java).also {
                it.putExtra("OptionName", "chatMessaging")
                it.putExtra("chatroom", list.docID)
                it.putExtra("receiverName", list.receiver)
            }
            context.startActivity(intent)
        }
        holder.itemView.startAnimation(holder.animation)
    }


    override fun getItemCount(): Int {
        return chatList.size
    }

    private fun userName(idUser: String, holder: ChatsViewHolder) {
        holder.name.visibility = View.GONE
        holder.message.visibility = View.GONE
        holder.image.visibility = View.GONE
        holder.content.visibility = View.GONE
        // Buscar el nombre del receptor en la colección "users"
        fstore.collection("users").document(idUser)
            .get()
            .addOnSuccessListener { Snapshot ->
                val username =
                    Snapshot.getString("userName")
                        .toString()
                holder.name.visibility = View.VISIBLE

                holder.name.text = username
                holder.message.visibility = View.VISIBLE
                holder.image.visibility = View.VISIBLE
                holder.content.visibility = View.VISIBLE
            }
    }

    fun clearMessages() {
        chatList.clear()
        notifyDataSetChanged()
    }
}
package com.example.tfg_whatsapp2.Adapter.Contacts

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_whatsapp2.MenuActivity
import com.example.tfg_whatsapp2.R
import com.example.tfg_whatsapp2.databinding.RecycleviewContactsBinding
import com.example.tfg_whatsapp2.modelo.UserModel
import com.squareup.picasso.Picasso

class ContactsViewHolder(v: View):RecyclerView.ViewHolder(v) {

    val binding = RecycleviewContactsBinding.bind(v)

    fun render(u: UserModel){
        binding.txtContName.text = u.profileName
        binding.txtContEmail.text = u.profileEmail
        binding.txtContStatus.text = u.profileStatus
        Picasso.get().load(u.profilePicture).error(R.drawable.profile_user).into(binding.imgProfileImage)
        binding.userContent.setOnClickListener {
            val intent = Intent(binding.userContent.context, MenuActivity::class.java).also {
                it.putExtra("OptionName","contactsMessaging")
                it.putExtra("chatroom",u.chatRoomId)
                it.putExtra("friendUID",u.profileId)
                it.putExtra("friendName",u.profileName)
            }
            binding.userContent.context.startActivity(intent)
        }
    }
}

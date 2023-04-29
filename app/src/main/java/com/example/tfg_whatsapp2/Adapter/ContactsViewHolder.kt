package com.example.tfg_whatsapp2.Adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_whatsapp2.R
import com.example.tfg_whatsapp2.databinding.RecycleviewContactsBinding
import com.example.tfg_whatsapp2.modelo.User
import com.squareup.picasso.Picasso

class ContactsViewHolder(v: View):RecyclerView.ViewHolder(v) {

    val binding = RecycleviewContactsBinding.bind(v)

    fun render(u: User){
        binding.txtContName.text = u.profileName
        binding.txtContEmail.text = u.profileEmail
        binding.txtContStatus.text = u.profileStatus
        Picasso.get().load(u.profilePicture).error(R.drawable.profile_user).into(binding.imgProfileImage)
    }
}

package com.example.tfg_whatsapp2.Adapter.Contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_whatsapp2.R
import com.example.tfg_whatsapp2.modelo.UserModel

class ContactsAdapter(
    val context: Context,
    val contactList: ArrayList<UserModel>):RecyclerView.Adapter<ContactsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycleview_contacts,parent,false)
        return ContactsViewHolder(v)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.render(contactList[position])

    }

    override fun getItemCount() = contactList.size
}
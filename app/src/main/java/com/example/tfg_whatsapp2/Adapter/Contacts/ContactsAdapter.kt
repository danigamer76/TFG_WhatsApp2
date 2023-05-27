package com.example.tfg_whatsapp2.Adapter.Contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_whatsapp2.R
import com.example.tfg_whatsapp2.modelo.User

class ContactsAdapter(val context:Context,private val contactList: ArrayList<User>) :
    RecyclerView.Adapter<ContactsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val contactView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_contacts, parent, false)
        return ContactsViewHolder(contactView,context)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val list = contactList[position]
        holder.bind(list)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }
}
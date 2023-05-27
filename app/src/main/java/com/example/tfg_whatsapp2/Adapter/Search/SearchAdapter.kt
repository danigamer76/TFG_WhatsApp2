package com.example.tfg_whatsapp2.Adapter.Search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_whatsapp2.R
import com.example.tfg_whatsapp2.modelo.User

class SearchAdapter(private val searchList: ArrayList<User>) :
    RecyclerView.Adapter<SearchViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val contactView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_contacts, parent, false)
        return SearchViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val list = searchList[position]
        holder.bind(list)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

}

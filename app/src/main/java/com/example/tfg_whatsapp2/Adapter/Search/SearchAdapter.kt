package com.example.tfg_whatsapp2.Adapter.Search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_whatsapp2.R
import com.example.tfg_whatsapp2.modelo.UserModel

class SearchAdapter(
    val context: Context,
    private val searchList: ArrayList<UserModel>):RecyclerView.Adapter<SearchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycleview_contacts,parent,false)
        return SearchViewHolder(v)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.render(searchList[position])
    }

    override fun getItemCount() = searchList.size

}

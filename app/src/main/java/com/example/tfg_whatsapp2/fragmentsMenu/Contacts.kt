package com.example.tfg_whatsapp2.fragmentsMenu

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_whatsapp2.Adapter.Contacts.ContactsAdapter
import com.example.tfg_whatsapp2.R
import com.example.tfg_whatsapp2.modelo.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class Contacts : Fragment() {

    private lateinit var recyclerViewContacts: RecyclerView
    private lateinit var layoutManagerContacts: RecyclerView.LayoutManager
    private lateinit var adapterContacts: ContactsAdapter

    private lateinit var fbStore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private val contactInfo = arrayListOf<UserModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)
        recyclerViewContacts = view.findViewById(R.id.contactsRecyclerView)
        layoutManagerContacts = LinearLayoutManager(context as Activity)

        auth = FirebaseAuth.getInstance()
        fbStore = FirebaseFirestore.getInstance()
        fbStore.collection("users").get().addOnSuccessListener {
            if (!it.isEmpty){
                contactInfo.clear()
                val listContact = it.documents
                for(doc in listContact){
                    val contact = UserModel(
                        doc.id,
                        doc.getString("userName").toString(),
                        doc.getString("userEmail").toString(),
                        doc.getString("userStatus").toString(),
                        doc.getString("userProfilePhoto").toString())
                    contactInfo.add(contact)
                    adapterContacts = ContactsAdapter(context as Activity,contactInfo)
                    recyclerViewContacts.adapter = adapterContacts
                    recyclerViewContacts.layoutManager = layoutManagerContacts
                    recyclerViewContacts.addItemDecoration(
                        DividerItemDecoration(
                            recyclerViewContacts.context,
                            (layoutManagerContacts as LinearLayoutManager).orientation))
                }
            }
        }

        return view
    }
}
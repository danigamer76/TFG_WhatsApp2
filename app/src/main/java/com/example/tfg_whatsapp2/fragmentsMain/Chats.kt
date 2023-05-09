package com.example.tfg_whatsapp2.fragmentsMain

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
import com.example.tfg_whatsapp2.Adapter.Chats.ChatsAdapter
import com.example.tfg_whatsapp2.Adapter.Contacts.ContactsAdapter
import com.example.tfg_whatsapp2.R
import com.example.tfg_whatsapp2.modelo.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Chats : Fragment() {

    private lateinit var chatsRecyclerView: RecyclerView
    private lateinit var chatsLayoutManager: RecyclerView.LayoutManager
    private lateinit var chatsAdapter: ChatsAdapter

    private lateinit var fbStore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private val chatsInfo = arrayListOf<UserModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chats, container, false)
        chatsRecyclerView = view.findViewById(R.id.chatsContentRecyclerView)
        chatsLayoutManager = LinearLayoutManager(context as Activity)

        auth = FirebaseAuth.getInstance()
        fbStore = FirebaseFirestore.getInstance()
        fbStore.collection("chats").whereArrayContains("uids",auth.currentUser!!.uid).addSnapshotListener{snapshot, exception->
            if (exception!= null){
                Log.d("","")
            }else{
                if (!snapshot?.isEmpty!!){
                    chatsInfo.clear()
                    val list = snapshot.documents
                    for(doc in list){
                        val obj = UserModel(
                            doc.id,
                            doc.getString("userName").toString(),
                            doc.getString("userEmail").toString(),
                            doc.getString("userStatus").toString(),
                            doc.getString("userProfilePhoto").toString())
                        chatsInfo.add(obj)
                        chatsAdapter = ChatsAdapter(context as Activity,chatsInfo)
                        chatsRecyclerView.adapter = chatsAdapter
                        chatsRecyclerView.layoutManager = chatsLayoutManager
                        chatsRecyclerView.addItemDecoration(
                            DividerItemDecoration(
                                chatsRecyclerView.context,
                                (chatsLayoutManager as LinearLayoutManager).orientation)
                        )
                    }
                }
            }

        }
        return view
    }
}

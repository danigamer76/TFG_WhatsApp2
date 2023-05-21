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
import com.example.tfg_whatsapp2.modelo.ChatModel
import com.example.tfg_whatsapp2.modelo.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class Chats : Fragment() {

    private lateinit var chatsRecyclerView: RecyclerView
    private lateinit var chatsLayoutManager: RecyclerView.LayoutManager
    private lateinit var chatsAdapter: ChatsAdapter

    private lateinit var fbStore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private val chatsInfo = arrayListOf<ChatModel>()

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
                    for (doc in list){
                        val friendsID = doc.id
                        val chatRoomID = doc.getString("chatRoomID")
                        fbStore.collection("chats").document(friendsID).collection("message").orderBy("id",Query.Direction.DESCENDING).addSnapshotListener{messageSnapshot,exception ->
                            if (exception!=null){
                                Log.d("error","Some Error Ocurred")
                            }else{
                                val messageSnapshot = messageSnapshot?.documents
                                if (!messageSnapshot.isNullOrEmpty()) {
                                    val id = messageSnapshot[0]
                                    val message = id.get("message").toString()
                                    val reciver = id.get("reciver").toString()
                                    val obj = ChatModel(reciver, message, id.getString("").toString(),chatRoomID.toString())
                                    chatsInfo.add(obj)
                                }
                            }
                        }
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
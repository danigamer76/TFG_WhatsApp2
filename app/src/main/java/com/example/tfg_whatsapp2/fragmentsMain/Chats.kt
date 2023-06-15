package com.example.tfg_whatsapp2.fragmentsMain

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_whatsapp2.R
import com.example.tfg_whatsapp2.adapter.ChatsAdapter
import com.example.tfg_whatsapp2.modelo.ChatModal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Chats : Fragment() {
    private lateinit var chatsRecyclerView: RecyclerView
    private lateinit var chatsLayoutManager: RecyclerView.LayoutManager
    private lateinit var chatsAdapter: ChatsAdapter
    private lateinit var fstore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val chatsInfo = arrayListOf<ChatModal>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chats, container, false)
        chatsRecyclerView = view.findViewById(R.id.chatContentRecyclerView)
        chatsLayoutManager = LinearLayoutManager(context as Activity)
        chatsAdapter = ChatsAdapter(context as Activity, chatsInfo)

        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        return view
    }

    private fun mostrarChats() {
        fstore.collection("chats").whereArrayContainsAny(
            "uids",
            arrayListOf(auth.currentUser!!.uid)
        ).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.d("", "")
            } else {
                if (!snapshot?.isEmpty!!) {
                    chatsInfo.clear()
                    val list = snapshot.documents
                    for (doc in list) {
                        fstore.collection("chats").document(doc.id).collection("message")
                            .addSnapshotListener { messagesnapshot, exception ->
                                if (exception != null) {
                                    Log.d("error", "Some Error Occurred")
                                } else {
                                    if (!messagesnapshot!!.isEmpty) {
                                        val sortedList = messagesnapshot.documents.sortedByDescending {
                                            it.getString("messageId")?.toIntOrNull() ?: 0
                                        }
                                        val id = sortedList[0]
                                        val message = id.getString("message").toString()
                                        val receiver = id.getString("messageReceiver").toString()
                                        val sender = id.getString("messageSender").toString()
                                        Log.d("messageDocument", receiver)

                                        // Buscar el nombre del receptor en la colección "users"
                                        fstore.collection("users").document(receiver)
                                            .get()
                                            .addOnSuccessListener { receiverSnapshot ->

                                                val photoReceiver =
                                                    receiverSnapshot.getString("userProfilePhoto")
                                                        .toString()

                                                // Buscar el nombre del remitente en la colección "users"
                                                fstore.collection("users").document(sender)
                                                    .get()
                                                    .addOnSuccessListener { senderSnapshot ->
                                                        val photoSender =
                                                            senderSnapshot.getString("userProfilePhoto")
                                                                .toString()

                                                        val obj =
                                                            if (receiver == auth.currentUser!!.uid) {
                                                                ChatModal(
                                                                    sender,
                                                                    message,
                                                                    photoSender,
                                                                    doc.id
                                                                )
                                                            } else {
                                                                ChatModal(
                                                                    receiver,
                                                                    message,
                                                                    photoReceiver,
                                                                    doc.id
                                                                )
                                                            }

                                                        chatsInfo.add(obj)
                                                        chatsRecyclerView.adapter = chatsAdapter
                                                        chatsRecyclerView.layoutManager =
                                                            chatsLayoutManager
                                                        chatsRecyclerView.addItemDecoration(
                                                            DividerItemDecoration(
                                                                chatsRecyclerView.context,
                                                                (chatsLayoutManager as LinearLayoutManager).orientation
                                                            )
                                                        )
                                                    }
                                            }
                                    }
                                }
                            }
                    }
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        mostrarChats()

    }

    override fun onPause() {
        super.onPause()
        chatsAdapter.clearMessages()

    }
}

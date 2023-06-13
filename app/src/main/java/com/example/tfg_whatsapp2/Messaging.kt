package com.example.tfg_whatsapp2

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_whatsapp2.adapter.MessageAdapter
import com.example.tfg_whatsapp2.modelo.MessageModal
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import java.util.*

class Messaging : Fragment() {
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var sendMessageEditText: EditText
    private lateinit var sendMessageButton: FloatingActionButton
    private lateinit var fstore: FirebaseFirestore
    private lateinit var fauth: FirebaseAuth
    private lateinit var messageLayoutManager: RecyclerView.LayoutManager
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var db: DocumentReference
    private lateinit var userid: String
    private lateinit var friendID : String
    private lateinit var chatroomid : String
    private lateinit var chatID : String
    private lateinit var db1 : DocumentReference
    private lateinit var chatroomUID : String
    private val messageInfo = arrayListOf<MessageModal>()
    private var register : ListenerRegistration? = null
    private var register1 : ListenerRegistration? =null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_messaging, container, false)
        messageRecyclerView = view.findViewById(R.id.messageRecyclerView)
        sendMessageButton = view.findViewById(R.id.btSendMessage)
        sendMessageEditText = view.findViewById(R.id.etSendMessage)
        friendID = ""
        chatroomid = ""
        val values = arguments
        if (values != null) {
            val newFriendID = values.getString("friendName").toString()
            val newChatroomID = values.getString("documentID").toString()
            if (newFriendID != "null" && !newFriendID.isEmpty()) {
                friendID = newFriendID
                chatroomid = newChatroomID
            } else if (friendID.isEmpty()) {
                val newFriendID = values.getString("friendUID").toString()
                val newChatroomID = values.getString("chatRoomID").toString()
                if (!newFriendID.isEmpty()) {
                    friendID = newFriendID
                    chatroomid = newChatroomID
                }
            }
        }
        Log.d("logContactbundle", friendID)
        Log.d("logContactbundle", chatroomid)
        initialization(chatroomid)

        sendMessageButton.setOnClickListener{
            fetchChatRoomUID()
            fetchMessageID()
        }
        return view
    }
    private fun fetchChatRoomUID()
    {
        fstore.collection("chats").whereEqualTo(FieldPath.documentId(), chatroomid).get().addOnSuccessListener { query->
            if(!query.isEmpty)
            {
                chatroomUID = query.documents[0].id
                Log.d("chatroomid",chatroomUID)
                initialization(chatroomUID)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchingMessages(idMessages:String) {
        register1 = fstore.collection("chats").document(idMessages)
            .collection("message")
            .orderBy("messageId", Query.Direction.ASCENDING)
            .addSnapshotListener { chatSnapshot, exception ->
                if (exception != null) {
                    Log.d("", "")
                } else {
                    messageInfo.clear()
                    if (!chatSnapshot?.isEmpty!!) {
                        val listChat = chatSnapshot.documents
                        val sortedList = listChat.sortedBy { it.getString("messageId")?.toIntOrNull() }
                        for (chat in sortedList) {
                            val chatobj = MessageModal(
                                chat.getString("messageSender").toString(),
                                chat.getString("message").toString(),
                                chat.getString("messageTime").toString(),
                                chat.getString("messageId").toString()
                            )
                            messageInfo.add(chatobj)
                        }
                        messageRecyclerView.scrollToPosition(messageInfo.size - 1)
                        messageAdapter.notifyDataSetChanged()
                    }
                }
            }
    }


    private fun fetchMessageID() {
        db = fstore.collection("chats").document(chatroomid).collection("count").document("chatid")
        db.get()
            .addOnSuccessListener { documentSnapshot ->
                chatID = documentSnapshot.getString("chatid") ?: "0"
                sendMessage()
            }
            .addOnFailureListener { exception ->
                Log.d("fetchMessageID", "Error fetching chatid: $exception")
            }
    }


    private fun initialization(id : String) {
        fstore = FirebaseFirestore.getInstance()
        fauth = FirebaseAuth.getInstance()
        userid = fauth.currentUser?.uid.toString()
        messageLayoutManager = LinearLayoutManager(context)
        recyclerViewBuild(id)

    }

    private fun recyclerViewBuild(id:String) {
        messageAdapter = MessageAdapter(context as Activity, messageInfo)
        messageRecyclerView.adapter = messageAdapter
        messageRecyclerView.layoutManager = messageLayoutManager
        fetchingMessages(id)

    }

    private fun sendMessage() {
        register?.remove()
        val message = sendMessageEditText.text.toString()
        if (TextUtils.isEmpty(message)) {
            sendMessageEditText.error = "Enter some Message to Send"
        } else {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            val timeStamp = String.format("%d:%02d", hour, minute)
            val messageObject = mapOf(
                "message" to message,
                "messageId" to chatID,
                "messageSender" to userid,
                "messageReceiver" to friendID,
                "messageTime" to timeStamp
            )
            db1 = fstore.collection("chats").document(chatroomUID).collection("message").document()
            db1.set(messageObject)
                .addOnSuccessListener {
                    Log.d("onSuccess", "Successfully Send Message")
                }
                .addOnFailureListener { exception ->
                    Log.d("sendMessage", "Error sending message: $exception")
                }
            db.update("chatid", (chatID.toInt() + 1).toString())
                .addOnSuccessListener {
                    Log.d("onSuccess", "Successfully updated count messages")
                }
                .addOnFailureListener { exception ->
                    Log.d("sendMessage", "Error updating chatid: $exception")
                }
        }
        sendMessageEditText.text.clear() // Borrar el contenido del campo de texto
        hideKeyboard() // Cerrar el teclado
    }


    private fun hideKeyboard() {
        val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }


    override fun onDestroy() {
        register1?.remove()
        register?.remove()
        super.onDestroy()
    }

}
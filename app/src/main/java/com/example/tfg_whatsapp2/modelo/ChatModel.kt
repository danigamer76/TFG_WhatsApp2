package com.example.tfg_whatsapp2.modelo

data class ChatModel (
    val receiver : String,
    val message : String,
    val receiverImage : String,
    val docID: String?
)
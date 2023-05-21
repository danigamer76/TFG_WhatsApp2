package com.example.tfg_whatsapp2.modelo

data class UserModel(
    val profileId: String,
    val profileName:String,
    val profileEmail:String,
    val profileStatus:String,
    val profilePicture:String,
    val chatRoomId: String
)
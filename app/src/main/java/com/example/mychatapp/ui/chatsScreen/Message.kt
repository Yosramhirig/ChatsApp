package com.example.mychatapp.ui.chatsScreen

import android.net.Uri
import java.net.URI

data class Message(
    val from: String,
    val content: String,
    val uri: Uri?
)

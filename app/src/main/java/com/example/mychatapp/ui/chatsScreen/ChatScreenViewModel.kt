package com.example.mychatapp.ui.chatsScreen

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import javax.inject.Inject


class ChatScreenViewModel  @Inject constructor (): ViewModel() {
    private val database = FirebaseDatabase.getInstance()
    private val reference = database.getReference("messages")
    private  val auth = Firebase.auth

    var username by mutableStateOf("")

    var message by mutableStateOf(listOf<Message>())
        private set

    init {
        getMessage()
        auth.currentUser?.email.let{
            username = it!!
        }
    }

    private fun getMessage() {
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = mutableListOf<Message>()
                snapshot.children.forEach {msg ->
                   val from = msg.child("from").getValue(String::class.java).toString()
                    val content = msg.child("content").getValue(String::class.java).toString()
                    val uri = msg.child("image").getValue(Uri::class.java)
                    val conversation =Message(from, content, uri)
                    messages.add(conversation)
                }
                message = messages
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }


        }
        reference.addValueEventListener(postListener)
        Log.d("Tag", postListener.toString())
    }

    fun sendMessage(msg : String?, uri: Uri?) {
        if (msg != null) {
            if(msg.isNotBlank()){
                val newmsgref = reference.push()
                val newmsg = Message(username, msg,null)
                newmsgref.setValue(newmsg)
                Log.d("Tag", newmsg.toString())
            }
            }else{
            if(uri != null){
                val newmsgref = reference.push()
                val newmsg = Message(username, "emptyString",uri)
                newmsgref.setValue(newmsg)
            }
        }
    }
}
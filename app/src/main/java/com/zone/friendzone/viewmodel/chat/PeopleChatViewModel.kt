package com.zone.friendzone.viewmodel.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.zone.friendzone.data.model.Chat
import com.zone.friendzone.data.model.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.mlkit.nl.smartreply.SmartReply
import com.google.mlkit.nl.smartreply.TextMessage
import com.zone.friendzone.data.model.ChatModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.collections.map

class PeopleChatViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance()
    private val chatRef = database.getReference("chats")

    private val _chatState = MutableStateFlow(Chat())
    val chatState: StateFlow<Chat> get() = _chatState

    var currentMessage by mutableStateOf("")

    var smartReplies by mutableStateOf<List<String>>(emptyList())

    fun fetchMessages(chatId: String) {
        chatRef.child(chatId).child("messages").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = mutableMapOf<String, Message>()
                snapshot.children.forEach {
                    val message = it.getValue(Message::class.java)
                    message?.let { msg -> messages[it.key!!] = msg }
                }
                _chatState.value = chatState.value.copy(messages = messages)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
    fun generateSmartReplies(chatMessages: List<ChatModel>, currentUserId: String) {
        if (chatMessages.isEmpty()) {
            smartReplies = emptyList()
            return
        }
        val conversation = chatMessages.map { message ->
            if (message.senderId == currentUserId) {
                TextMessage.createForLocalUser(message.messageText, message.timestamp)
            } else {
                TextMessage.createForRemoteUser(message.messageText, message.timestamp, message.senderId)
            }
        }

        SmartReply.getClient().suggestReplies(conversation)
            .addOnSuccessListener { result ->
                smartReplies = result.suggestions.map { it.text }
            }
            .addOnFailureListener {
                smartReplies = emptyList()
            }
    }

}


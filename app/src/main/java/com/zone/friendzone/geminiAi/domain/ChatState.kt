package com.zone.friendzone.geminiAi.domain

import android.graphics.Bitmap
import com.zone.friendzone.geminiAi.data.Chat


data class ChatState(
    val chatList: MutableList<Chat> = mutableListOf(),
    val prompt : String = "",
    val bitmap: Bitmap? = null
)
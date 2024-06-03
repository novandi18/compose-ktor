package com.novandi.composektor.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novandi.composektor.websocket.ChatWebSocketClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.URI

class HomeViewModel: ViewModel() {
    private val _chats: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
    val chats: StateFlow<List<String>> = _chats.asStateFlow()

    var chat by mutableStateOf("")
        private set

    fun setOnChatChange(newChat: String) {
        chat = newChat
    }

    private val websocketClient = ChatWebSocketClient(URI("ws://YOUR_SERVER_URL")) { message ->
        viewModelScope.launch {
            _chats.value += message
        }
    }

    init {
        websocketClient.connect()
    }

    fun sendMessage(message: String) {
        websocketClient.sendMessage(message)
    }

    override fun onCleared() {
        websocketClient.close()
        super.onCleared()
    }
}
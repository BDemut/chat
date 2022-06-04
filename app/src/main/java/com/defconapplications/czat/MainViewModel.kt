package com.defconapplications.czat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.defconapplications.czat.api.Message

class MainViewModel : ViewModel() {
    var username by mutableStateOf("")
    var currentMessage by mutableStateOf("")

    var messages by mutableStateOf(
        listOf(
            Message("andrzej", "elo byki"),
            Message("janek", "cześć kolego"),
            Message("stefan", "cześć kolego")
        )
    ) //TODO pobierać z socketa

    fun send() {
        // TODO wysłać socketem Message(username, currentMessage)

        currentMessage = ""
    }
}
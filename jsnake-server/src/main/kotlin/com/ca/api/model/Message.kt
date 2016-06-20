package com.ca.api.model

data class Message(val type: MessageType, val message: String)

enum class MessageType(val type: String) {
    INFO("INFO"),
    WARNING("WARN"),
    ERROR("ERROR")
}
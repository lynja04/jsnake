package com.ca.api.model

data class APIResponse(val success: Boolean, val messages: MutableList<Message>, val data: MutableList<out Any>)
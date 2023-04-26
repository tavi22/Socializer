package com.example.socializer.model

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date

data class Chatlist (var id : String,
                     var initiator : User,
                     var responder : User,
                     var messages : List<Chat>? = ArrayList(),
                     var timestamp : String? = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'").format(Date())) {
}
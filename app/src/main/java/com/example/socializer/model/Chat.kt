package com.example.socializer.model

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

data class Chat (var id : String,
                 var sender : User,
                 var receiver : User,
                 var message : String,
                 var timestamp : String? = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'").format(Date()),
                 var isSeen : Boolean? = false) {
}
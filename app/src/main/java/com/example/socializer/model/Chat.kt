package com.example.socializer.model

import java.time.LocalDateTime
import java.util.*

data class Chat (var id : String,
                 var sender : User,
                 var receiver : User,
                 var message : String,
                 var timestamp : Calendar? = Calendar.getInstance(),
                 var isSeen : Boolean? = false) {
}
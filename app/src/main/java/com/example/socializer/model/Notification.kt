package com.example.socializer.model

import java.text.SimpleDateFormat
import java.util.*

data class Notification (var id : String,
                         var sender : String,
                         var receiver : String,
                         var action : String,
                         var timestamp : String? = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'").format(Date()))
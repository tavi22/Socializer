package com.example.socializer.model

import java.text.SimpleDateFormat
import java.util.*

data class Comment (var id : String,
                    var text : String,
                    var owner : User,
                    var post : String,
                    var timestamp : String? = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'").format(Date()))
package com.example.socializer.model

import java.text.SimpleDateFormat
import java.util.*

data class Vote (var owner : String? = null,
                 var post : String? = null,
                 val positive : Int? = 0,
                 var creationDate : String? = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'").format(Date()))
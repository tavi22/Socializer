package com.example.socializer.model

import java.text.SimpleDateFormat
import java.util.*

data class Forum (var title : String? = null,
                  var description : String? = null,
                  var logo : String? = null,
                  var owner : String? = null,
                  var members : List<String>? = emptyList(),
                  var creationDate : String? = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'").format(Date()))
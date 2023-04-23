package com.example.socializer.model

import java.util.*

data class Forum (var id : String,
                  var title : String,
                  var description : String? = null,
                  var logo : Int,
                  var owner : String,
                  var members : List<String>? = emptyList(),
                  var creationDate : Calendar? = Calendar.getInstance())
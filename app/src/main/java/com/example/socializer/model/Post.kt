package com.example.socializer.model

import android.net.Uri
import java.text.SimpleDateFormat
import java.util.*

data class Post (var id : String? = null,
                 var title : String? = null,
                 var description : String? = null,
                 var imageUri : String? = null,
                 var videoUri: String? = null,
                 var owner : String? = null,
                 var forum : String? = null,
                 var creationDate : String? = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'").format(Date()))
package com.example.socializer.model

import android.net.Uri
import java.util.*

data class Post (var id : String,
                 var title : String,
                 var description : String,
                 var imageUri : String? = null,
                 var videoUri: String? = null,
                 var owner : String,
                 var forum : Forum,
                 var creationDate : Calendar? = Calendar.getInstance())
package com.example.socializer.model

import android.net.Uri
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User (var username : String? = null,
                 var email : String? = null,
                 var description : String? = null,
                 var imageuri : String? = null,
                 var status : Boolean? = null)
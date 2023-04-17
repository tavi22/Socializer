package com.example.socializer.model

import android.net.Uri

data class User (var id : String? = null,
                 var username : String? = null,
                 var email : String? = null,
                 var description : String? = null,
                 var imageuri : Uri? = null,
                 var status : Boolean? = null)
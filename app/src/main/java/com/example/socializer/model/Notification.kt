package com.example.socializer.model

data class Notification (var id : String,
                         var sender : String,
                         var receiver : String,
                         var action : String)
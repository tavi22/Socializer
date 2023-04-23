package com.example.socializer.model

data class Comment (var id : String,
                    var text : String,
                    var owner : User,
                    var post : String)
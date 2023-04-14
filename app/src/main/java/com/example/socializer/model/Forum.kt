package com.example.socializer.model

data class Forum (var id : String,
                  var title : String,
                  var description : String,
                  var owner : String,
                  var members : List<String>)
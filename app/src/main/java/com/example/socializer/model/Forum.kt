package com.example.socializer.model

import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.*

data class Forum (var id : String? = null,
                  var title : String? = null,
                  var description : String? = null,
                  var logo : String? = null,
                  var owner : String? = null,
                  var members : List<String>? = emptyList(),
                  var creationDate : String? = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'").format(Date())) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(logo)
        parcel.writeString(owner)
        parcel.writeStringList(members)
        parcel.writeString(creationDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Forum> {
        override fun createFromParcel(parcel: Parcel): Forum {
            return Forum(parcel)
        }

        override fun newArray(size: Int): Array<Forum?> {
            return arrayOfNulls(size)
        }
    }
}
package com.app.usersapp.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val userImage: String, // url of the image
    val userFullName: String,
    val userPhone: String,
    val userEmail: String,
    @Ignore var marked: Boolean // the "marked" property isn't saved in the database, currently it is used to mark users for delete
) {
    constructor(id: Long, userImage: String, userFullName: String, userPhone: String, userEmail: String) :
            this(id, userImage, userFullName, userPhone, userEmail, false)
}


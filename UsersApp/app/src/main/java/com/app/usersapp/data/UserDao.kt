package com.app.usersapp.data

import androidx.room.*

@Dao
interface UserDao {
    // the users are gotten sorted by the full name
    @Query("SELECT * from users_table ORDER BY LOWER(userFullName) ASC")
    suspend fun getUsers(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(vararg user: User)
}
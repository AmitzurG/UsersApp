package com.app.usersapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UsersDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        // singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var database: UsersDatabase? = null

        fun getDatabase(context: Context): UsersDatabase {
            if (database != null) {
                return database as UsersDatabase
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, UsersDatabase::class.java, "users_database").build()
                database = instance
                return instance
            }
        }
    }
}
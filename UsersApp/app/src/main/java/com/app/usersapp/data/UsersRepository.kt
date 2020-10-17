package com.app.usersapp.data

class UsersRepository(private val userDao: UserDao) {
    // users from database
    suspend fun getUsers() = userDao.getUsers()

    // insert user to database
    suspend fun insert(user: User) = userDao.insert(user)

    // update the user in database
    suspend fun update(user: User) = userDao.update(user)

    // delete user from database
    suspend fun delete(users: List<User>) = userDao.delete(*users.toTypedArray())
}
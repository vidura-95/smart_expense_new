package com.example.smartexpense

class UserRepository(private val userDao: UserDao) {

    suspend fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }

    suspend fun insert(user: User) {
        userDao.insert(user)
    }
}

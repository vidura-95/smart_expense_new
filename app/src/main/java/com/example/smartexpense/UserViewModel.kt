package com.example.smartexpense

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest

class UserViewModel(app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.getInstance(app)
    private val userDao = db.userDao()

    fun addUser(username: String, password_raw: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val passwordHash = hashPassword(password_raw)
            userDao.insert(User(username = username, passwordHash = passwordHash))
        }
    }

    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}

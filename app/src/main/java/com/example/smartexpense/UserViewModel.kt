package com.example.smartexpense

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.security.MessageDigest

class UserViewModel(app: Application) : AndroidViewModel(app) {
    private val userRepository: UserRepository

    init {
        val userDao = AppDatabase.getInstance(app).userDao()
        userRepository = UserRepository(userDao)
    }

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    fun addUser(username: String, password_raw: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val passwordHash = hashPassword(password_raw)
            userRepository.insert(User(username = username, passwordHash = passwordHash))
        }
    }

    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    fun login(username: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userRepository.getUserByUsername(username)

            if (user != null) {
                val enteredHash = hashPassword(password)
                if (enteredHash == user.passwordHash) {
                    _loginError.value = null
                    // Switch to main thread for UI operations
                    launch(Dispatchers.Main) {
                        onSuccess()
                    }
                } else {
                    _loginError.value = "Invalid username or password"
                }
            } else {
                _loginError.value = "Invalid username or password"
            }
        }
    }
}

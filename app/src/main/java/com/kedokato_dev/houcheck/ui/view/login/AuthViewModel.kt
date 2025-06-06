package com.kedokato_dev.houcheck.ui.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kedokato_dev.houcheck.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val sessionId: String) : LoginState()
    data class Error(val message: String) : LoginState()
}
@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState


    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Username or password cannot be empty")
            return
        }

        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            val result = repository.login(username, password)
            _loginState.value = result.fold(
                onSuccess = { sessionId -> LoginState.Success(sessionId) },
                onFailure = { error -> LoginState.Error(error.message ?: "Unknown error") }
            )
        }
    }

    fun getSessionId(): String? {
        return repository.getSessionId()
    }

    fun logout() {
        repository.clearSession()
    }
}
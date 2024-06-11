package com.kocelanetwork.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kocelanetwork.core.common.Resource
import com.kocelanetwork.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginState = Channel<AuthState>()
    val loginState = _loginState.receiveAsFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).collect { result ->
                when(result){
                    is Resource.Success ->{_loginState.send(AuthState(data = "LogIn Successfully"))}
                    is Resource.Loading ->{_loginState.send(AuthState(isLoading = true))}
                    is Resource.Error ->{_loginState.send(AuthState(error = result.message.toString()))}
                }
            }
        }
    }
}

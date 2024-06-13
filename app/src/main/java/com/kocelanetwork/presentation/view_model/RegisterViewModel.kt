package com.kocelanetwork.presentation.view_model

import android.util.Log
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
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _registerState = Channel<AuthState>()
    val registerState = _registerState.receiveAsFlow()

    suspend fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            authRepository.register(email, password, name).collect { result ->
                when(result){
                    is Resource.Success ->{
                            Log.d("RegisterViewModel", "Registration successful: ${result.data}")
                            _registerState.send(AuthState(data = result.data?.message))
                    }
                    is Resource.Loading ->{
                        Log.d("RegisterViewModel", "Loading")
                        _registerState.send(AuthState(isLoading = true))
                    }
                    is Resource.Error ->{
                        val errorMessage = when(result.message){
                            "Email already registered" -> "Email already registered"
                            "User already exists" -> "User already exists"
                            else -> "Registration Failed Check Your Credentials!!!"
                        }
                        Log.d("RegisterViewModel", errorMessage)
                        _registerState.send(AuthState(error = errorMessage))
                    }
                }
            }
        }
    }
}

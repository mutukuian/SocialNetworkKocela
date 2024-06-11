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
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _registerState = Channel<AuthState>()
    val registerState = _registerState.receiveAsFlow()


    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            authRepository.register(email, password, name).collect { result ->
                when(result){
                    is Resource.Success ->{_registerState.send(AuthState(data = "Registration Successfully"))}
                    is Resource.Loading ->{_registerState.send(AuthState(isLoading = true))}
                    is Resource.Error ->{_registerState.send(AuthState(error = result.message.toString()))}
                }
            }
        }
    }
}

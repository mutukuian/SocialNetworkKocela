package com.kocelanetwork.data.api_service


import com.kocelanetwork.domain.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>
}

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val status: String, val message: String, val token: String)

data class RegisterRequest(val email: String, val password: String, val name: String)
data class RegisterResponse(val status: String, val message: String, val userData: User)

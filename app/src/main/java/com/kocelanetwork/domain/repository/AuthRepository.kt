package com.kocelanetwork.domain.repository


import com.kocelanetwork.core.common.Resource
import com.kocelanetwork.data.api_service.RegisterResponse
import com.kocelanetwork.domain.models.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Flow<Resource<Result<User>>>
    suspend fun register(email: String, password: String, name: String):Flow<Resource< RegisterResponse>>
}

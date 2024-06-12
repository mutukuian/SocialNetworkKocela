package com.kocelanetwork.presentation.view_model

data class AuthState (
    val isLoading: Boolean = false,
    val data:String?= "",
    val error:String?= null,
//    val username: String? = null
)



package com.kocelanetwork.domain.models

data class User(
    val id: String,
    val email: String,
    val name: String,
    val token: String
)

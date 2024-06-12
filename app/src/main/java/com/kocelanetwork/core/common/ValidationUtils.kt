package com.kocelanetwork.core.common

object ValidationUtils {

    // Email regex pattern
    private val emailRegex = Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")

    // Password regex pattern: at least 8 characters, one lowercase, one uppercase, one digit, one special character
    private val passwordRegex = Regex("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,}$")

    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && emailRegex.matches(email)
    }

    fun isValidPassword(password: String): Boolean {
        return password.isNotBlank() && passwordRegex.matches(password)
    }
}

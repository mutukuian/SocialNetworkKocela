package com.kocelanetwork.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.kocelanetwork.R
import com.kocelanetwork.core.common.ValidationUtils
import com.kocelanetwork.presentation.view_model.AuthState
import com.kocelanetwork.presentation.view_model.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Login : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_scree)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        loginButton = findViewById(R.id.login_button)
        signupButton = findViewById(R.id.signup_button)
        progressBar = findViewById(R.id.progress_bar)

        lifecycleScope.launchWhenStarted {
            viewModel.loginState.collect { authState ->
                handleAuthState(authState)
            }
        }

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (!ValidationUtils.isValidEmail(email)) {
                showToast("Please enter a valid email address")
                return@setOnClickListener
            }

            if (!ValidationUtils.isValidPassword(password)) {
                showToast("Password must be at least 8 characters long and include at least one lowercase letter, one uppercase letter, one numeric digit, and one special character")
                return@setOnClickListener
            }

            viewModel.login(email, password)
        }

        signupButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun handleAuthState(authState: AuthState) {
        if (authState.isLoading) {
            progressBar.visibility = ProgressBar.VISIBLE
        } else {
            progressBar.visibility = ProgressBar.GONE
            authState.error?.let { error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
            authState.data?.let { data ->
                Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
                if(data == "LogIn Successfully") {
                    navigateToHomeScreen()
                }
            }
        }
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(this, HomeScreen::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

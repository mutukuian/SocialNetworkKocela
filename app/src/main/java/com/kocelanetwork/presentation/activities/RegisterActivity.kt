package com.kocelanetwork.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.kocelanetwork.presentation.view_model.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val viewModel: RegisterViewModel by viewModels()

    private lateinit var emailInput: EditText
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var signupButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.reg_screen)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        emailInput = findViewById(R.id.email_input)
        usernameInput = findViewById(R.id.username_input)
        passwordInput = findViewById(R.id.password_input)
        signupButton = findViewById(R.id.signup_button)
        progressBar = findViewById(R.id.progress_bar)

        // Using lifecycleScope to collect the Flow
        lifecycleScope.launchWhenStarted {
            viewModel.registerState.collect { authState ->
                handleAuthState(authState)
            }
        }

        signupButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val name = usernameInput.text.toString().trim()

            if (!ValidationUtils.isValidEmail(email)) {
                showToast("Please enter a valid email address")
                return@setOnClickListener
            }

            if (!ValidationUtils.isValidPassword(password)) {
                showToast("Password must be at least 8 characters long and include at least one lowercase letter, one uppercase letter, one numeric digit, and one special character")
                return@setOnClickListener
            }

            if (name.isBlank()) {
                showToast("Please enter your name")
                return@setOnClickListener
            }

            lifecycleScope.launch {
                viewModel.register(email, password, name)
            }

        }
    }

    private fun handleAuthState(authState: AuthState) {
        if (authState.isLoading) {
            // Show loading indicator
            progressBar.visibility = ProgressBar.VISIBLE
        } else {
            progressBar.visibility = ProgressBar.GONE
            authState.error?.let { error ->
                Log.d("RegisterActivity", "Registration error: $error")
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
            authState.data?.let { data ->
                Log.d("RegisterActivity", "Registration successful: $data")
                Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
                if (data == "Registration successful") {
                    navigateToLoginScreen()
                }

            }
        }
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

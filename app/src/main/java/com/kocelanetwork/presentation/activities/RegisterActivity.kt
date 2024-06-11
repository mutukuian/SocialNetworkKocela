package com.kocelanetwork.presentation.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.kocelanetwork.R
import com.kocelanetwork.presentation.view_model.AuthState
import com.kocelanetwork.presentation.view_model.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val viewModel: RegisterViewModel by viewModels()

    private lateinit var emailInput: EditText
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var signupButton: Button

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
            viewModel.register(email, password, name)
        }
    }

    private fun handleAuthState(authState: AuthState) {
        if (authState.isLoading) {
            // Show loading indicator
        } else {
            authState.error?.let { error ->
                // Show error message
                // Example: Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
            authState.data?.let { data ->
                // Handle successful registration
                // Example: Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
                // Navigate to MainActivity or perform necessary actions
            }
        }
    }
}

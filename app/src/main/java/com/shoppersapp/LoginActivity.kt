package com.shoppersapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.shoppersapp.model.LoginRequest
import com.shoppersapp.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var loginIdentifierEt: EditText
    private lateinit var loginPasswordEt: EditText
    private lateinit var loginBtn: Button

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginIdentifierEt = findViewById(R.id.loginIdentifierEditText)
        loginPasswordEt = findViewById(R.id.loginPasswordEditText)
        loginBtn = findViewById(R.id.loginButton)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        loginViewModel.loginSuccess.observe(this) { userId ->
            if (!userId.isNullOrBlank()) {
                Toast.makeText(this, "Login successful! User ID: $userId", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LandingPageActivity::class.java)
                intent.putExtra("userId", userId.toInt())
                startActivity(intent)
                finish()
            }
        }

        loginViewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        loginBtn.setOnClickListener {
            val identifier = loginIdentifierEt.text.toString()
            val password = loginPasswordEt.text.toString()

            if (identifier.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            } else {
                val request = LoginRequest(identifier, password)
                loginViewModel.loginUser(request)
            }
        }
    }
}
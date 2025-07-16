package com.shoppersapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.shoppersapp.model.RegisterRequest
import com.shoppersapp.viewmodel.RegisterViewModel

class RegistrationActivity : AppCompatActivity() {

    private lateinit var firstNameEt: EditText
    private lateinit var lastNameEt: EditText
    private lateinit var dobEt: EditText
    private lateinit var phoneEt: EditText
    private lateinit var addressEt: EditText
    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var registerBtn: Button
    private lateinit var loginRedirectTv: TextView
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        firstNameEt = findViewById(R.id.firstNameEditText)
        lastNameEt = findViewById(R.id.lastNameEditText)
        dobEt = findViewById(R.id.dobEditText)
        phoneEt = findViewById(R.id.phoneEditText)
        addressEt = findViewById(R.id.addressEditText)
        emailEt = findViewById(R.id.emailEditText)
        passwordEt = findViewById(R.id.passwordEditText)
        registerBtn = findViewById(R.id.registerButton)
        loginRedirectTv = findViewById(R.id.switchToLoginTextView)

        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        registerViewModel.registrationSuccess.observe(this) { success ->
            if (success == true) {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        registerViewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        registerBtn.setOnClickListener {
            val request = RegisterRequest(
                firstNameEt.text.toString(),
                lastNameEt.text.toString(),
                dobEt.text.toString(),
                phoneEt.text.toString(),
                addressEt.text.toString(),
                emailEt.text.toString(),
                passwordEt.text.toString()
            )
            registerViewModel.registerUser(request)
        }

        loginRedirectTv.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
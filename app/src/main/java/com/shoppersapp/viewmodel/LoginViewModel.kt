package com.shoppersapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shoppersapp.api.UserApi
import com.shoppersapp.model.LoginRequest
import com.shoppersapp.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val userApi = UserApi.create()

    private val _loginSuccess = MutableLiveData<String?>()
    val loginSuccess: LiveData<String?> = _loginSuccess

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun loginUser(request: LoginRequest) {
        userApi.loginUser(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    _loginSuccess.value = response.body()?.userId?.toString()
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Login failed: ${response.message()}"
                    _loginSuccess.value = null
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _errorMessage.value = "Login error: ${t.message}"
                _loginSuccess.value = null
            }
        })
    }
}
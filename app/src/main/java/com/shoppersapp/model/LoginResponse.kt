package com.shoppersapp.model;

data class LoginResponse(
    val userId: Integer,
    val token: String?
)
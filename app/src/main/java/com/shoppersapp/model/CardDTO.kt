package com.shoppersapp.model

data class CardDTO(
    val longCardNumber: String,
    val cvv: String?,
    val expiry: String
)
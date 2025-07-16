package com.shoppersapp.model

import java.math.BigDecimal

data class TransactionRequest(
    val accountNumber: String,
    val sortCode: String,
    val longCardNumber: String? = null,
    val cvv: String? = null,
    val transactionType: String,
    val amount: BigDecimal
)
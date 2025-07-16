package com.shoppersapp.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionDTO(
    val transactionId: Int,
    val accountNumber: String,
    val sortCode: String,
    val amount: BigDecimal,
    val transactionType: String,
    val startingBalance: BigDecimal,
    val closingBalance: BigDecimal,
    val createdAt: String
)
package com.shoppersapp.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction(
    val amount: BigDecimal,
    val type: String,
    val timestamp: String,
    val startingBalance: BigDecimal,
    val closingBalance: BigDecimal
)
package com.shoppersapp.model

import java.math.BigDecimal

data class AccountDTO(
    val accountNumber: String,
    val sortCode: String,
    val balance: BigDecimal,
    val interestAccrued: BigDecimal
)
package com.shoppersapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.shoppersapp.model.CardDTO
import com.shoppersapp.model.TransactionRequest
import com.shoppersapp.viewmodel.AccountViewModel
import java.math.BigDecimal

class LandingPageActivity : AppCompatActivity() {

    private lateinit var amountInput: EditText
    private lateinit var depositBtn: Button
    private lateinit var withdrawBtn: Button
    private lateinit var purchaseBtn: Button
    private lateinit var accrueInterestBtn: Button
    private lateinit var transactionsBtn: Button

    private lateinit var accountNumberTextView: TextView
    private lateinit var sortCodeTextView: TextView
    private lateinit var balanceTextView: TextView
    private lateinit var interestAccruedTextView: TextView

    private lateinit var cardNumberTextView: TextView
    private lateinit var cardCvvTextView: TextView
    private lateinit var cardExpiryTextView: TextView

    private var currentCard: CardDTO? = null
    private lateinit var accountViewModel: AccountViewModel

    private var loggedInUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        loggedInUserId = intent.getIntExtra("userId", -1)
        if (loggedInUserId == -1) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        amountInput = findViewById(R.id.amountInput)
        depositBtn = findViewById(R.id.depositBtn)
        withdrawBtn = findViewById(R.id.withdrawBtn)
        purchaseBtn = findViewById(R.id.purchaseBtn)
        accrueInterestBtn = findViewById(R.id.accrueInterestBtn)
        transactionsBtn = findViewById(R.id.transactionsBtn)

        accountNumberTextView = findViewById(R.id.accountNumberTextView)
        sortCodeTextView = findViewById(R.id.sortCodeTextView)
        balanceTextView = findViewById(R.id.balanceTextView)
        interestAccruedTextView = findViewById(R.id.interestAccruedTextView)

        cardNumberTextView = findViewById(R.id.cardNumberTextView)
        cardCvvTextView = findViewById(R.id.cvvTextView)
        cardExpiryTextView = findViewById(R.id.expiryDateTextView)

        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)

        accountViewModel.account.observe(this) { accountDTO ->
            accountDTO?.let {
                accountNumberTextView.text = "Account Number: ${it.accountNumber}"
                sortCodeTextView.text = "Sort Code: ${it.sortCode}"
                balanceTextView.text = "Balance: £%.2f".format(it.balance)
                interestAccruedTextView.text = "Available rewards: £%.2f".format(it.interestAccrued)
                accountViewModel.fetchCard(it.accountNumber, it.sortCode)
            }
        }

        accountViewModel.card.observe(this) { cardDTO ->
            currentCard = cardDTO
            if (cardDTO != null) {
                cardNumberTextView.text = "Card Number: ${cardDTO.longCardNumber}"
                cardCvvTextView.text = "CVV: ${cardDTO.cvv}"
                cardExpiryTextView.text = "Expiry: ${cardDTO.expiry}"
            } else {
                cardNumberTextView.text = "Card Number: N/A"
                cardCvvTextView.text = "CVV: N/A"
                cardExpiryTextView.text = "Expiry: N/A"
            }
        }

        accountViewModel.errorMessage.observe(this) { error ->
            error?.let { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
        }

        accountViewModel.fetchAccount(loggedInUserId)

        depositBtn.setOnClickListener { handleTransaction("DEPOSIT") }
        withdrawBtn.setOnClickListener { handleTransaction("WITHDRAWAL") }
        purchaseBtn.setOnClickListener { handlePurchase() }
        accrueInterestBtn.setOnClickListener { handleAccrueInterest() }
        transactionsBtn.setOnClickListener { openTransactionsScreen() }

        accountViewModel.depositSuccess.observe(this) { success ->
            if (success == true) {
                Toast.makeText(this, "Deposit successful!", Toast.LENGTH_SHORT).show()
                accountViewModel.fetchAccount(loggedInUserId)
            }
        }
        accountViewModel.withdrawSuccess.observe(this) { success ->
            if (success == true) {
                Toast.makeText(this, "Withdrawal successful!", Toast.LENGTH_SHORT).show()
                accountViewModel.fetchAccount(loggedInUserId)
            }
        }
        accountViewModel.purchaseSuccess.observe(this) { success ->
            if (success == true) {
                Toast.makeText(this, "Purchase successful!", Toast.LENGTH_SHORT).show()
                accountViewModel.fetchAccount(loggedInUserId)
            }
        }
        accountViewModel.accrualSuccess.observe(this) { success ->
            if (success == true) {
                Toast.makeText(this, "Accrual successful!", Toast.LENGTH_SHORT).show()
                accountViewModel.fetchAccount(loggedInUserId)
            }
        }

        accountViewModel.depositError.observe(this) { errorMsg ->
            errorMsg?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }
        accountViewModel.withdrawError.observe(this) { errorMsg ->
            errorMsg?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }
        accountViewModel.purchaseError.observe(this) { errorMsg ->
            errorMsg?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }
        accountViewModel.accrualError.observe(this) { errorMsg ->
            errorMsg?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }
    }

    private fun handleTransaction(type: String) {
        val amountText = amountInput.text.toString()
        if (amountText.isBlank()) {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
            return
        }
        val amount = amountText.toBigDecimalOrNull()
        if (amount == null || amount <= BigDecimal.ZERO) {
            Toast.makeText(this, "Enter a valid positive amount", Toast.LENGTH_SHORT).show()
            return
        }

        val account = accountViewModel.account.value
        if (account == null) {
            Toast.makeText(this, "Account data not loaded", Toast.LENGTH_SHORT).show()
            return
        }

        val transactionRequest = TransactionRequest(
            accountNumber = account.accountNumber,
            sortCode = account.sortCode,
            amount = amount,
            transactionType = type
        )
        when (type) {
            "DEPOSIT" -> accountViewModel.deposit(transactionRequest)
            "WITHDRAWAL" -> accountViewModel.withdraw(transactionRequest)
        }
    }

    private fun handlePurchase() {
        val amountText = amountInput.text.toString()
        if (amountText.isBlank()) {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
            return
        }
        val amount = amountText.toBigDecimalOrNull()
        if (amount == null || amount <= BigDecimal.ZERO) {
            Toast.makeText(this, "Enter a valid positive amount", Toast.LENGTH_SHORT).show()
            return
        }

        val account = accountViewModel.account.value
        if (account == null) {
            Toast.makeText(this, "Account data not loaded", Toast.LENGTH_SHORT).show()
            return
        }

        val card = currentCard
        if (card == null) {
            Toast.makeText(this, "Card details not loaded", Toast.LENGTH_SHORT).show()
            return
        }

        val transactionRequest = TransactionRequest(
            accountNumber = account.accountNumber,
            sortCode = account.sortCode,
            longCardNumber = card.longCardNumber,
            cvv = card.cvv,
            amount = amount,
            transactionType = "PURCHASE"
        )

        accountViewModel.purchase(transactionRequest)
    }

    private fun handleAccrueInterest() {
        val account = accountViewModel.account.value
        if (account == null) {
            Toast.makeText(this, "Account data not loaded", Toast.LENGTH_SHORT).show()
            return
        }
        accountViewModel.accrueInterest()
    }

    private fun openTransactionsScreen() {
        val account = accountViewModel.account.value
        if (account == null) {
            Toast.makeText(this, "Account data not loaded", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, TransactionsActivity::class.java).apply {
            putExtra("accountNumber", account.accountNumber)
            putExtra("sortCode", account.sortCode)
        }
        startActivity(intent)
    }
}

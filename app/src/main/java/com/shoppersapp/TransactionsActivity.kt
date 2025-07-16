package com.shoppersapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shoppersapp.adapter.TransactionAdapter
import com.shoppersapp.model.Transaction
import com.shoppersapp.model.TransactionDTO
import com.shoppersapp.viewmodel.TransactionsViewModel
import android.util.Log

class TransactionsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransactionAdapter
    private val viewModel: TransactionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)

        val accountNumber = intent.getStringExtra("accountNumber") ?: return
        val sortCode = intent.getStringExtra("sortCode") ?: return

        recyclerView = findViewById(R.id.transactionsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TransactionAdapter(emptyList())
        recyclerView.adapter = adapter

        viewModel.transactions.observe(this, Observer { transactions ->
            Log.d("TransactionsActivity", "Received transactions: $transactions")
            adapter.updateData(transactions)
        })

        viewModel.error.observe(this, Observer { errorMsg ->
            errorMsg?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.loadTransactions(accountNumber, sortCode)
    }
}
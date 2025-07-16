package com.shoppersapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shoppersapp.api.UserApi
import com.shoppersapp.model.AccountRequest
import com.shoppersapp.model.Transaction
import com.shoppersapp.model.TransactionDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
class TransactionsViewModel : ViewModel() {

    private val userApi = UserApi.create()

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> get() = _transactions

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun loadTransactions(accountNumber: String, sortCode: String) {
        Log.d("TransactionsViewModel", "Starting loadTransactions for account: $accountNumber, sortCode: $sortCode")

        userApi.getTransactionsByAccount(accountNumber, sortCode)
            .enqueue(object : Callback<List<TransactionDTO>> {

                override fun onResponse(
                    call: Call<List<TransactionDTO>>,
                    response: Response<List<TransactionDTO>>
                ) {
                    Log.d("TransactionsViewModel", "onResponse called")
                    Log.d("TransactionsViewModel", "Response code: ${response.code()}, message: ${response.message()}")

                    if (response.isSuccessful) {
                        val dtos = response.body()
                        if (dtos != null) {
                            Log.d("TransactionsViewModel", "Received ${dtos.size} DTOs")
                            Log.v("TransactionsViewModel", "DTO contents: $dtos")
                            val mapped = mapDtosToTransactions(dtos)
                            Log.d("TransactionsViewModel", "Mapped transactions count: ${mapped.size}")
                            _transactions.postValue(mapped)
                            _error.postValue(null)
                        } else {
                            Log.w("TransactionsViewModel", "Response body is null")
                            _error.postValue("No transactions found (empty response).")
                        }
                    } else {
                        Log.e("TransactionsViewModel", "Response not successful: ${response.errorBody()?.string()}")
                        _error.postValue("Failed to load transactions. Server error ${response.code()}.")
                    }
                }

                override fun onFailure(call: Call<List<TransactionDTO>>, t: Throwable) {
                    Log.e("TransactionsViewModel", "onFailure called: ${t.message}", t)
                    _error.postValue("Network or conversion error: ${t.message}")
                }
            })
        }

    fun mapDtosToTransactions(dtos: List<TransactionDTO>): List<Transaction> {
        return dtos.map {
            Transaction(
                amount = it.amount,
                type = it.transactionType,
                timestamp = it.createdAt,
                startingBalance = it.startingBalance,
                closingBalance = it.closingBalance
            )
        }
    }
}
package com.shoppersapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shoppersapp.api.UserApi
import com.shoppersapp.model.AccountDTO
import com.shoppersapp.model.CardDTO
import com.shoppersapp.model.TransactionRequest
import com.shoppersapp.model.TransactionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountViewModel : ViewModel() {

    private val userApi = UserApi.create()

    private val _account = MutableLiveData<AccountDTO?>()
    val account: LiveData<AccountDTO?> = _account

    private val _card = MutableLiveData<CardDTO?>()
    val card: LiveData<CardDTO?> get() = _card

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _depositSuccess = MutableLiveData<Boolean>()
    val depositSuccess: LiveData<Boolean> = _depositSuccess

    private val _depositError = MutableLiveData<String?>()
    val depositError: LiveData<String?> = _depositError

    private val _withdrawSuccess = MutableLiveData<Boolean>()
    val withdrawSuccess: LiveData<Boolean> = _withdrawSuccess

    private val _withdrawError = MutableLiveData<String?>()
    val withdrawError: LiveData<String?> = _withdrawError

    private val _purchaseSuccess = MutableLiveData<Boolean>()
    val purchaseSuccess: LiveData<Boolean> = _purchaseSuccess

    private val _purchaseError = MutableLiveData<String?>()
    val purchaseError: LiveData<String?> = _purchaseError

    private val _accrualSuccess = MutableLiveData<Boolean>()
    val accrualSuccess: LiveData<Boolean> = _accrualSuccess

    private val _accrualError = MutableLiveData<String?>()
    val accrualError: LiveData<String?> = _accrualError

    fun fetchAccount(userId: Int) {
        userApi.getAccount(userId).enqueue(object : Callback<AccountDTO> {
            override fun onResponse(call: Call<AccountDTO>, response: Response<AccountDTO>) {
                if (response.isSuccessful && response.body() != null) {
                    _account.value = response.body()
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Failed to load account: ${response.message()}"
                    _account.value = null
                }
            }

            override fun onFailure(call: Call<AccountDTO>, t: Throwable) {
                _errorMessage.value = "Network error: ${t.message}"
                _account.value = null
            }
        })
    }


    fun fetchCard(accountNumber: String, sortCode: String) {
        userApi.getCardDetails(accountNumber, sortCode).enqueue(object : Callback<CardDTO> {
            override fun onResponse(call: Call<CardDTO>, response: Response<CardDTO>) {
                if (response.isSuccessful && response.body() != null) {
                    _card.value = response.body()
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Failed to load card: ${response.message()}"
                    _card.value = null
                }
            }

            override fun onFailure(call: Call<CardDTO>, t: Throwable) {
                _errorMessage.value = "Network error: ${t.message}"
                _card.value = null
            }
        })
    }

    fun deposit(request: TransactionRequest) {
        userApi.sendTransaction(request).enqueue(object : Callback<TransactionResponse> {
            override fun onResponse(call: Call<TransactionResponse>, response: Response<TransactionResponse>) {
                if (response.isSuccessful) {
                    _depositSuccess.postValue(true)
                } else {
                    _depositSuccess.postValue(false)
                    _depositError.postValue("Deposit failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                _depositSuccess.postValue(false)
                _depositError.postValue("Deposit error: ${t.message}")
            }
        })
    }

    fun withdraw(request: TransactionRequest) {
        userApi.sendTransaction(request).enqueue(object : Callback<TransactionResponse> {
            override fun onResponse(call: Call<TransactionResponse>, response: Response<TransactionResponse>) {
                if (response.isSuccessful) {
                    _withdrawSuccess.postValue(true)
                } else {
                    _withdrawSuccess.postValue(false)
                    _withdrawError.postValue("Withdraw failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                _withdrawSuccess.postValue(false)
                _withdrawError.postValue("Withdraw error: ${t.message}")
            }
        })
    }

    fun purchase(request: TransactionRequest) {
        Log.i("API Call", "Sending request")
        userApi.sendTransaction(request).enqueue(object : Callback<TransactionResponse> {
            override fun onResponse(call: Call<TransactionResponse>, response: Response<TransactionResponse>) {
                if (response.isSuccessful) {
                    _purchaseSuccess.postValue(true)
                } else {
                    Log.e("API Response", "Error response: ${response.errorBody()?.string()}")
                    _purchaseSuccess.postValue(false)
                    _purchaseError.postValue("Purchase failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                _purchaseSuccess.postValue(false)
                _purchaseError.postValue("Purchase error: ${t.message}")
            }
        })
    }

    fun accrueInterest() {
        userApi.accrueInterest().enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    _accrualSuccess.postValue(true)
                } else {
                    _accrualError.postValue("Interest accrual failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _accrualError.postValue("Interest accrual error: ${t.message}")
            }
        })
    }
}
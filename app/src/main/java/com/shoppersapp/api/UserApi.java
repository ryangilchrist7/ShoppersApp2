package com.shoppersapp.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import com.shoppersapp.model.AccountDTO;
import com.shoppersapp.model.AccountRequest;
import com.shoppersapp.model.CardDTO;
import com.shoppersapp.model.RegisterRequest;
import com.shoppersapp.model.LoginResponse;
import com.shoppersapp.model.LoginRequest;
import com.shoppersapp.model.TransactionDTO;
import com.shoppersapp.model.TransactionRequest;
import com.shoppersapp.model.TransactionResponse;

import java.util.List;


public interface UserApi {
    @POST("user/register")
    Call<Void> registerUser(@Body RegisterRequest request);

    @POST("user/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);

    @POST("/api/transaction")
    Call<TransactionResponse> sendTransaction(@Body TransactionRequest request);
    @GET("accounts/{userId}")
    Call<AccountDTO> getAccount(@Path("userId") Integer userId);

    @GET("accounts/{accountNumber}/{sortCode}")
    Call<CardDTO> getCardDetails(
            @Path("accountNumber") String accountNumber,
            @Path("sortCode") String sortCode
    );

    @POST("/api/bank/accrue-interest")
    Call<Void> accrueInterest();

    @GET("/api/transactions/{accountNumber}/{sortCode}")
    Call<List<TransactionDTO>> getTransactionsByAccount(
            @Path("accountNumber") String accountNumber,
            @Path("sortCode") String sortCode);

    static UserApi create() {
        return RetrofitClient.getInstance().create(UserApi.class);
    }
}
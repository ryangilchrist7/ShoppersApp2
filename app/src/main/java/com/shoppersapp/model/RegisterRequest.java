package com.shoppersapp.model;

public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String dateOfBirth; // ISO format e.g. "2001-05-20"
    private String phoneNumber;
    private String address;
    private String email;
    private String password;

    public RegisterRequest(String firstName, String lastName, String dateOfBirth, String phoneNumber,
                           String address, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.password = password;
    }
}
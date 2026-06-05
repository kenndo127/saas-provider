package com.naijajug.saasurlshortner.service;

import com.naijajug.saasurlshortner.dto.request.CreateUserRequest;
import com.naijajug.saasurlshortner.dto.request.LoginRequest;
import com.naijajug.saasurlshortner.dto.response.AuthResponse;
import com.naijajug.saasurlshortner.dto.response.ResponseWrapper;

public interface UserService {
    ResponseWrapper<AuthResponse> signup(CreateUserRequest payload);
    ResponseWrapper<AuthResponse> login(LoginRequest payload);
    void logOut();
}

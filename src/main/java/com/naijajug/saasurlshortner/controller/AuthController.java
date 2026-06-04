package com.naijajug.saasurlshortner.controller;

import com.naijajug.saasurlshortner.dto.request.CreateUserRequest;
import com.naijajug.saasurlshortner.dto.request.LoginRequest;
import com.naijajug.saasurlshortner.dto.response.AuthResponse;
import com.naijajug.saasurlshortner.dto.response.ResponseWrapper;
import com.naijajug.saasurlshortner.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class    AuthController {

    private final UserService userService;

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/signup")
    public ResponseWrapper<AuthResponse> addNewUser(
            @RequestBody CreateUserRequest payload
    ){
        return userService.signup(payload);
    }

    @PostMapping("/login")
    public  ResponseWrapper<AuthResponse> authenticateAndGetToken(
            @RequestBody LoginRequest payload
    ) {
        return  userService.login(payload);
    }
}

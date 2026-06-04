package com.naijajug.saasurlshortner.service.impl;

import com.naijajug.saasurlshortner.auth.JwtService;
import com.naijajug.saasurlshortner.dto.request.CreateUserRequest;
import com.naijajug.saasurlshortner.dto.request.LoginRequest;
import com.naijajug.saasurlshortner.dto.response.AuthResponse;
import com.naijajug.saasurlshortner.dto.response.ResponseWrapper;
import com.naijajug.saasurlshortner.exceptions.ResourceNotFoundException;
import com.naijajug.saasurlshortner.model.UserModel;
import com.naijajug.saasurlshortner.repository.UserModelRepository;
import com.naijajug.saasurlshortner.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserModelRepository userModelRepository;
    private  final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    @Transactional
    public ResponseWrapper<AuthResponse> signup(CreateUserRequest payload) {
        log.info("AUTH LOG: signup payload: {}", payload);
        UserModel user = UserModel.builder()
                .name(payload.getName())
                .email(payload.getEmail())
                .password(passwordEncoder.encode(payload.getPassword()))
                .roles(payload.getRoles())
                .build();
        UserModel savedUser = userModelRepository.save(user);
        String token = jwtService.generateToken(payload.getEmail(), payload.getRoles());

        return buildAuthResponse(savedUser.getId(), token, "Signup successful", HttpStatusCode.valueOf(HttpStatus.CREATED.value()));
    }

    @Override
    public ResponseWrapper<AuthResponse> login(LoginRequest payload) {
        Optional<UserModel> userModelOptional = userModelRepository.findByEmail(payload.getEmail());
        if (userModelOptional.isEmpty())
            throw new ResourceNotFoundException("User not found!");
        UserModel user = userModelOptional.get();
        //the last four codes can be collapsed into a line
        log.info("User details {}", user);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(payload.getEmail(), payload.getPassword())
        );

        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(payload.getEmail(), user.getRoles());
            return  buildAuthResponse(user.getId(),token,"Login Successful",HttpStatusCode.valueOf(HttpStatus.OK.value()));
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    private ResponseWrapper<AuthResponse> buildAuthResponse(UUID id, String token, String message, HttpStatusCode statusCode){
        AuthResponse response = new AuthResponse(id, token);
           return ResponseWrapper.<AuthResponse>builder()
                   .data(response)
                   .message(message)
                   .statusCode(statusCode)
                   .build();
    }
}

package com.naijajug.saasurlshortner.dto.response;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private UUID userID;
    private String token;
}

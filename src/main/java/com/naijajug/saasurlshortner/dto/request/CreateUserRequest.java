package com.naijajug.saasurlshortner.dto.request;

import com.naijajug.saasurlshortner.enums.Roles;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    private String name;
    private String email;
    private String password;
    private Set<Roles> roles;
}

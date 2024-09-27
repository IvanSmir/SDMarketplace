package com.fiuni.authapi.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class LoginResponse {
    private String token;

    @Setter
    private long expiresIn;

}
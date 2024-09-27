package com.fiuni.marketplacefreelancer.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class MissingOrInvalidCredentialsException extends RuntimeException {
    public MissingOrInvalidCredentialsException(String message) {
        super(message);
    }
}
